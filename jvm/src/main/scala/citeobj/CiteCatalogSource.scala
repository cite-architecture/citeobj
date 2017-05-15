package edu.holycross.shot.citeobj

import edu.holycross.shot.cite._
import scala.io.Source
import java.io._
import scala.xml._

import scala.collection.mutable.ArrayBuffer

/** Factory for creating CITE collection catalogs
* from various sources.
*/
object CiteCatalogSource {

  /** Create a CITE collection catalog from a file.
  *
  * @param f Name of file with XML catalog data
  * validating against the CITE collection schema.
  */
  def fromFile(f: String) : CiteCatalog = {
    val root = XML.loadFile(f)
    fromNodeSeq(root)
  }


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

    val rights = "insert rights statement"
    CiteCollectionDef(urn,title,propertyDefs.toVector,
      labellingProperty = label,
      orderingProperty = orderingProperty,
      license = rights)
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
    val pType = CiteCatalog.typeForString(typeNode.text, (vocabList.size > 0))

    val labelNode = propertyNode \  "@label"
    val pLabel = labelNode.text

    val urn = baseUrn.addProperty(pName)
    CitePropertyDef(urn,pLabel,pType,vocabList.toVector)
  }
}
