package edu.holycross.shot.citeobj

import edu.holycross.shot.cite._

import scala.collection.mutable.ArrayBuffer


/** Catalog defining structure of all collections in a repository.
*
* @param collections Defintions of structure of individual collections.
*/
case class CiteCatalog(collections: Vector[CiteCollectionDef]) {

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

  /** Set of URNs identifying all properties in the repository.
  */
  def properties = {
    val propertyUrns = collections.flatMap(_.propertyDefs.map(_.urn))
    val labelUrns = collections.map(_.labelProperty)
    val orderingUrns = collections.map(_.orderingProperty).flatten

    val propertySet = (propertyUrns ++ labelUrns ++ orderingUrns).toSet
    propertySet
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

  /** Convert string content of a collection line in
  * CEX catalog format to a tuple of objects.
  *
  * @param columns Array of Strings.
  */
  def collectionTuple(columns: Vector[String]) = {
    println(columns(0))
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
    (urn,collectionLabel,labelProperty,orderingProperty,rights)
    ///(urn,collectionLabel,rights)
/*
[4:35]
collection,urn:cite2:hmt:msA.v1:,Folios of the Venetus A manuscript,collection,urn:cite2:hmt:msA.v1.label:,collection,urn:cite2:hmt:msA.v1.sequence:

[4:36]
property,collection,urn:cite2:hmt:msA.v1.rv:,Recto or verso,String,recto#verso

[4:36]
property,collection,urn:cite2:hmt:msA.v1.sequence,Page sequence,Numeric,
    */
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
    val columnsByRows = src.split("\n").toVector.map(_.split(columnDelimiter).toVector)
    val collectionEntries = columnsByRows.filter(_(0) == "collection")
    val collectionTuples = collectionEntries.map(arr => collectionTuple(arr.drop(1)) )

    val propertyEntries = columnsByRows.filter(_(0)== "property")

    CiteCatalog(buffer.toVector)
  }

  /** Determing Cite property type based on string name and presence of controlled vocabulary list.
  *
  * @param s Value of  `@type` attribute in a `citeProperty` XML element.
  * @param vocablist True if property has a controlled vocabulary list.
  */
  def typeForString(s: String, vocabList: Boolean): CitePropertyType = {
    if (vocabList) {
      s match {
        case "string" => ControlledVocabType
        case _ => throw CiteException("Controlled vocabulary lists only allowed with string type data.")
      }

    } else {
      s match {
        case "cite2urn" => Cite2UrnType
        case "number" => NumericType
        case "string" => StringType
        case _ => throw CiteException("Unrecognized attribute value for string: " + s)
      }
    }
  }
}
