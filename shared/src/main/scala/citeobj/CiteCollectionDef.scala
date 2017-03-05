package edu.holycross.shot.citeobj

import edu.holycross.shot.cite._


case class CiteCollectionDef(
  urn: Cite2Urn,
  label: String,
  orderingProperty: Option[Cite2Urn] = None,
  propertyDefs: Vector[CitePropertyDef]) {

  def isOrdered: Boolean = {
    orderingProperty match {
      case u: Some[Cite2Urn] => true
      case _ => false
    }
  }
}
