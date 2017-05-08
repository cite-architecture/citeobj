package edu.holycross.shot.citeobj

import edu.holycross.shot.cite._
import scala.xml._
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

  /** Create a catalog from a (presumably very long) String.
  *
  * @param xml String of XML validating against CITE Catalog schema.
  */
  def fromXmlString(xml: String): CiteCatalog = {
    fromNodeSeq(XML.loadString(xml))
  }


  /** Create a catalog from the parsed root of an XML document validating against CITE Catalog schema.
  *
  * @param root Root node of a CiteCatalog.
  */
  def fromNodeSeq(root: NodeSeq): CiteCatalog = {
    var collectionDefs = ArrayBuffer[CiteCollectionDef]()

    val collectionNodes = root \\ "citeCollection"
    for (coll <- collectionNodes) {
      val cdef = collectionDefFromXml(coll)
      collectionDefs += cdef
    }
    CiteCatalog(collectionDefs.toVector)
  }




  /** Create a [[CiteCollectionDef]] from an XML node.
  *
  * @param collectionNode Root node  of a CITE Collection in the CITE Catalog schema (element `citeCollection`).
  */
  def collectionDefFromXml(collectionNode: scala.xml.Node) : CiteCollectionDef  = {
    val urnNode = collectionNode \  "@urn"
    val urn = Cite2Urn(urnNode.text)

    val titleNode = collectionNode \\ "title"
    val title = titleNode.text

    val labelNode = collectionNode \  "@label"
    val label = Some(Cite2Urn("urn:cite2:" + urn.namespace + ":" + urn.collectionComponent + "." + labelNode.text + ":"))

    val orderingNode = collectionNode \ "orderedBy" \ "@property"
    val orderingProperty = {
      orderingNode.size match {
        case 1 => Some(Cite2Urn(urn.toString + orderingNode.text))
        case _ => None
      }
    }

    var propertyDefs = ArrayBuffer[CitePropertyDef]()
    val propertyNodes = collectionNode \ "citeProperty"

    for (propNode <- propertyNodes) {
      val propDef = propDefFromXml(propNode,urn)
      propertyDefs += propDef
    }
    CiteCollectionDef(urn,title,label,orderingProperty,propertyDefs.toVector)
  }



  /** Create a [[CitePropertyDef]] from an XML node.
  *
  * @param collectionNode Root node  of a CITE property in the CITE Catalog schema (element `citeProperty`).
  * @param urnBase String value of the collection's URN, used to form explicit URNs for properties.
  */
  def propDefFromXml(propertyNode: NodeSeq, baseUrn: Cite2Urn): CitePropertyDef = {
    var vocabList = ArrayBuffer[String]()
    val vocabNodes = propertyNode \\ "value"
    for (vocabItem <- vocabNodes) {
      vocabList += vocabItem.text
    }

    val nameNode = propertyNode \  "@name"
    val pName = nameNode.text

    val typeNode = propertyNode \  "@type"
    val pType = typeForString(typeNode.text, (vocabList.size > 0))

    val labelNode = propertyNode \  "@label"
    val pLabel = labelNode.text

    val urn = baseUrn.addProperty(pName)
    CitePropertyDef(urn,pLabel,pType,vocabList.toVector)
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
