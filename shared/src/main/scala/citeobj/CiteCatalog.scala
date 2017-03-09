package edu.holycross.shot.citeobj

import edu.holycross.shot.cite._
import scala.xml._


case class CiteCatalog(collections: Vector[CiteCollectionDef]) {

  def size : Int = collections.size
  def isEmpty: Boolean = collections.isEmpty

  def urns = collections.map(_.urn).toSet

  def properties = {
    val propertyUrns = collections.flatMap(_.propertyDefs.map(_.urn))
    val labelUrns = collections.map(_.labelProperty)
    val orderingUrns = collections.map(_.orderingProperty).flatten

    val propertySet = (propertyUrns ++ labelUrns ++ orderingUrns).toSet
    propertySet
  }


  def ~~(filterUrn: Cite2Urn) : CiteCatalog = {
    CiteCatalog(collections.filter(_.urn ~~ filterUrn))
  }
}

// factory methods
object CiteCatalog {
  def fromXmlString(xml: String) = {
    fromNodeSeq(XML.loadString(xml))
  }

  def fromNodeSeq(root: NodeSeq) = {

    //println("ROOT = " + root)

    val collectionNodes = root \\ "citeCollection"
    println("Found " + collectionNodes.size + " collections")
    for (coll <- collectionNodes) {
      val cdef = collectionDefFromXml(coll)
    }

    /*
    what we need:
    urn
    labelling string 9for collection
    label property
    ordering property
    property defs
    */
  }
  def collectionDefFromXml(collectionNode: scala.xml.Node) /*: CiteCollectionDefSpec */ = {
    val labelNode = collectionNode \  "@label"
    val urnNode = collectionNode \  "@urn"
    val urn = Cite2Urn(urnNode.text)
    val label = Cite2Urn("urn:cite2:" + urn.namespace + ":" + urn.collectionComponent + "." + labelNode.text + ":")

    val titleNode = collectionNode \\ "title"
    val title = titleNode.text
    println(title)
    println("URN " + urn + " and LABLE: " + label)

    val orderingNode = collectionNode \ "orderedBy" \ "@property"
    println(orderingNode)
    val orderingProperty = {
      orderingNode.size match {
        case 1 => Some(Cite2Urn(urn.toString + orderingNode.text))
        case _ => None
      }
    }
    println ("ordering: " + orderingProperty)
    val propertyNodes = collectionNode \ "citeProperty"

    for (propNode <- propertyNodes) {
      propDefFromXml(  propNode)
    }
  }

  def propDefFromXml(nodeSeq: NodeSeq) = {

  }
}
