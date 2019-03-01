package edu.holycross.shot.citeobj

import edu.holycross.shot.cite._

import scala.collection.mutable.ArrayBuffer
import scala.scalajs.js
import scala.scalajs.js.annotation._


import edu.holycross.shot.cex._

/** Catalog defining structure of all collections in a repository.
*
* @param collections Defintions of structure of individual collections.
*/
@JSExportAll  case class CiteCatalog(collections: Vector[CiteCollectionDef]) {

  /** Number of collections in the repository.
  */
  def size : Int = collections.size

  /** True if no collections in the repository.
  */
  def isEmpty: Boolean = collections.isEmpty

  /** Set of URNs identifying all collections in the repository.
  */
  def urns = collections.map(_.urn).toSet

  /** Select a specific [[CiteCollectionDef]] by URN value.
  */
  def collection(urn: Cite2Urn) :  Option[CiteCollectionDef] = {
    val filtered = collections.filter(_.urn == urn)
    filtered.size match  {
      case 0 => None
      case 1 => Some(filtered(0))
      case _ => throw CiteObjectException("Duplicate URNs found in catalog: " + urn)
    }
  }

  /** True if collection is ordered.
  *
  * @param coll Collection to test.
  */
  def isOrdered(urn : Cite2Urn) : Boolean = {

    collection(urn.dropProperty) match {
      case None => throw CiteObjectException(s"No collection ${urn} cataloged.")
      case u: Option[CiteCollectionDef] => {
        u.get.orderingProperty match {
          case None => false
          case seqProp : Option[Cite2Urn] => true
        }
      }
    }
  }



  /** Set of URNs identifying all properties in the repository.
  */
  def properties = {
    val propertyUrns = collections.flatMap(_.propertyDefs.map(_.urn))
    val labelUrns = collections.map(_.labelProperty)
    val orderingUrns = collections.map(_.orderingProperty).flatten

    val propertySet = (propertyUrns ++ labelUrns ++ orderingUrns).toSet
    propertySet
  }

    /** Serialize the CiteCatalog to CEX
    *
    **/
    def cex:String = {
      val collectionsHeader:String = """#!citecollections
          |URN#Description#Labelling property#Ordering property#License""".stripMargin

      val cat:Vector[CiteCollectionDef] = this.collections
      val cexDefs:String = collectionsHeader + "\n" + cat.map(_.cex).mkString("\n")

      val cexPropertyBlocks:String = {
        cat.map(propertyDefs2CEX(_)).mkString("\n")
      }

      val stringVec:Vector[String] = Vector(
          cexDefs,
          cexPropertyBlocks
      )

      stringVec.mkString("\n\n")
    }

    private def propertyDefs2CEX(cd: CiteCollectionDef):String = {
      val citePropertiesHeader:String = """#!citeproperties
          |Property#Label#Type#Authority list""".stripMargin
      val header:String = citePropertiesHeader
      val propDefs:Vector[CitePropertyDef] = cd.propertyDefs
      val propVec:Vector[String] = propDefs.map(pd => {
        s"""${pd.urn}#${pd.label}#${pd.propertyType.cex}#${pd.vocabularyList.mkString(",")}"""
      })
      val cexBlock:String = (Vector(header) ++ propVec).mkString("\n")
      cexBlock
    }





  /** Find property definition for a given property.
  *
  * @param propertyUrn Property to find definition for.
  */
  def propertyDefinition(propertyUrn: Cite2Urn): Option[CitePropertyDef] = {
    val propertyUrns = collections.flatMap(_.propertyDefs.filter(_.urn == propertyUrn.dropSelector))
    propertyUrns.size match {
      case 0 => None
      case 1 => Some(propertyUrns(0))
      case 2 => throw CiteObjectException(s"Property reference in ${propertyUrn} ambiguous: found ${propertyUrns.size} matches.")
    }
  }

  /** Create a new catalog composed of entries
  * matching a given URN.
  *
  * @param filterUrn URN to match against.
  * @return A new CiteCatalog containing only entries
  * matching `filterUrn`.
  */
  def ~~(filterUrn: Cite2Urn) : CiteCatalog = {
    CiteCatalog(collections.filter(_.urn ~~ filterUrn))
  }
}

/** Object providing factory methods for
* creating CiteCatalog instances from various
* kinds of sources.
*/
object CiteCatalog {

  /** Create a Vector of tuples from parsed CEX source.
  *  Each tuple has the core data for a catalog entry that will be
  * joined with individual property definitions to form a complete
  * [[CiteCollectionDef]].
  *
  * @param cex Parsed CEX source.
  * @param columnDelimiter String delimiter separating columns of CEX data.
  */
  def collectionTuplesFromCex(cex: CexParser, columnDelimiter: String = "#"):
  Vector[(edu.holycross.shot.cite.Cite2Urn, String, Option[edu.holycross.shot.cite.Cite2Urn], Option[edu.holycross.shot.cite.Cite2Urn], String)] = {

    val catalogVector = cex.blockVector("citecollections")
    val stripped = catalogVector.flatMap(_.split("\n").drop(1).toVector)
    val columnsByRows = stripped.map(_.split("#").toVector)
    val collectionTuples = columnsByRows.map(arr => collectionTuple(arr) )
    collectionTuples

  }


  /** Create a Vector of [[CitePropertyDef]] objects from parsed CEX source.
  *
  * @param cex Parsed CEX source.
  * @param columnDelimiter String delimiter separating columns of CEX data.
  * @param listDelimiter String delimiter used to separate items in lists.
  */
  def propertyDefListFromCex(cex: CexParser, columnDelimiter: String = "#", listDelimiter: String = ","): Vector[CitePropertyDef] = {
    val propertyVector = cex.blockVector("citeproperties")
    // Flatten vector of blocks to a Vector of Strings with a single property def per line
    // and headers stripped out
    val stripped = propertyVector.flatMap(_.split("\n").drop(1).toVector)
    // Split individual rows up into columns of data
    val vov = stripped.map(_.split("#").toVector)
    // convert to CitePropertyDef
    val propDefList = vov.map(CiteCatalog.propertyDefinition(_, ","))
    propDefList
  }

  /** Create catalog object from a String in cex format.
  *
  * @param src String of cex-format catalog description.
  * @param columnDelimiter String delimiting columns.
  * @param listDelimiter String delimiting items in optional controlled vocabulary
  * list.
  */
  def apply(src: String, columnDelimiter: String = "#", listDelimiter: String = "," ) : CiteCatalog = {

    val buffer = ArrayBuffer[CiteCollectionDef]()
    val cex  = CexParser(src)
    val collectionTuples = collectionTuplesFromCex(cex, columnDelimiter)
    val propDefList = propertyDefListFromCex(cex, listDelimiter)

    for (c <- collectionTuples)  {
      val urn = c._1
      val applicable = propDefList.filter(_.urn ~~ urn)
      buffer += CiteCollectionDef(
        urn = c._1,
        collectionLabel = c._2,
        labellingProperty = c._3,
        orderingProperty= c._4,
        license= c._5,
        propertyDefs = applicable )
    }
    CiteCatalog(buffer.toVector)
  }



    /** Convert string content of a collection line in
    * CEX catalog format to a tuple of objects.
    *
    * @param columns Array of Strings.
    */
    def collectionTuple(columns: Vector[String]) = {
      val urn = Cite2Urn(columns(0))
      val collectionLabel = columns(1)
      val labelProperty = {
        if (columns(2).isEmpty) {
          None
        } else {
          Some(Cite2Urn(columns(2)))
        }
      }
      val orderingProperty = {
        if (columns(3).isEmpty) {
          None
        } else {
          Some(Cite2Urn(columns(3)))
        }
      }
      val rights = columns(4)

      val tup = (urn,collectionLabel,labelProperty,orderingProperty,rights)
      tup
    }

    /** Convert string content of a single property line in
    * CEX catalog format to a tuple of objects.
    *
    * @param columns Vector of Strings.
    */
    def propertyDefinition(columns: Vector[String], listDelimiter: String = ","): CitePropertyDef = {
      val urn = Cite2Urn(columns(0))
      val label = columns(1)
      val vocabList = {
        columns.size match {
          case 4 => columns(3).split(listDelimiter).toVector
          case _  => Vector[String]()
        }
      }
      val propertyType = typeForString(columns(2), (vocabList.size > 0))

      CitePropertyDef(urn,label,propertyType,vocabList)
    }


  /** Determine [[CitePropertyType]] based on string name and presence of controlled vocabulary list.
  *
  * @param s Value of  `@type` attribute in a `citeProperty` XML element.
  * @param vocablist True if property has a controlled vocabulary list.
  */
  def typeForString(s: String, vocabList: Boolean): CitePropertyType = {
    if (vocabList) {
      s.toLowerCase match {
        case "string" => ControlledVocabType
        case _ => throw CiteObjectException("Controlled vocabulary lists only allowed with string type data.")
      }

    } else {
      s.toLowerCase match {
        case "cite2urn" => Cite2UrnType
        case "ctsurn" => CtsUrnType
        case "number" => NumericType
        case "string" => StringType
        case "boolean" => BooleanType
        case _ => throw CiteObjectException("Unrecognized attribute value for string: " + s)
      }
    }
  }
}
