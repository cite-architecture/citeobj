package edu.holycross.shot.citeobj

import edu.holycross.shot.cite._


case class CiteCollectionDef(
  urn: Cite2Urn,
  collectionLabel: String,
  labellingProperty: Option[Cite2Urn] = None,
  orderingProperty: Option[Cite2Urn] = None,
  propertyDefs: Vector[CitePropertyDef]) {


  /** URN for required labelling property.
  * If not set by constructor, defaults to a
  * property named "label" in this colleciton.
  */
  def labelProperty: Cite2Urn = {
    labellingProperty match {
      case urn: Some[Cite2Urn] => urn.get
      case None => {
        Cite2Urn("urn:cite2:" + urn.namespace + ":" + urn.collectionComponent + ".label:")
      }

    }
  }



  /** True if collection is ordered.
  */
  def isOrdered: Boolean = {
    orderingProperty match {
      case u: Some[Cite2Urn] => true
      case _ => false
    }
  }
}
