package edu.holycross.shot.citeobj

import edu.holycross.shot.cite._

import scala.scalajs.js
import scala.scalajs.js.annotation._

/** Catalog entry defining a single CITE Collection.
*
* @param urn URN for the collection.
* @param collectionLabel Readable label for the collection.
* @param labellingProperty Optionally specified URN for property labelling individual objects.  If None, a default will be created by the [[labelProperty]] function.
* @param orderingProperty URN for the property sequencing objects, if the collection is ordered.
* @param license Statement of licensing terms applying to the collection as a whole.
* @param propertyDefs [[CitePropertyDef]]s for all properties appearing in the collection.
*/
@JSExportAll  case class CiteCollectionDef(
  urn: Cite2Urn,
  collectionLabel: String,
  propertyDefs: Vector[CitePropertyDef],
  license: String = "public domain",
  labellingProperty: Option[Cite2Urn] = None,
  orderingProperty: Option[Cite2Urn] = None
) {


  /** URN for required labelling property.
  * If not set by constructor, defaults to a
  * property named "label" in this collection.
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

  def cex:String = {
    val urn:String = this.urn.toString
    val collectionLabel:String = this.collectionLabel
    val labelProperty:String = this.labelProperty.toString
    val orderingProperty:String = {
      this.orderingProperty match {
        case Some(op) => op.toString
        case None => ""
        }
      }
      val license:String = this.license
      val stringVec:Vector[String] = Vector(urn, collectionLabel, labelProperty,orderingProperty,license)
      stringVec.mkString("#")
    }

}
