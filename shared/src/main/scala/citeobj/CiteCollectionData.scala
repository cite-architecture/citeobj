package edu.holycross.shot.citeobj

import edu.holycross.shot.cite._

/** All data values for a repository of citable
* objects and properties.
*
* @param data vector of property values. Because [[CitePropertyValue]]s have URNs, they can be grouped
* by object using URN twiddling.
*/
case class CiteCollectionData (data: Vector[CitePropertyValue]) {

  /** Filter collection data by identifying URN.
  */
  def ~~(filterUrn: Cite2Urn): CiteCollectionData = {
    CiteCollectionData(data.filter(_.urn ~~ filterUrn))
  }

  def ++(collection2 : CiteCollectionData): CiteCollectionData = {
    CiteCollectionData(this.data ++ collection2.data)
  }

  def --(collection2 : CiteCollectionData): CiteCollectionData = {
    CiteCollectionData(this.data diff collection2.data)
  }

  /** Number of property vaues in the collection.
  */
  def size: Int = {
    data.size
  }

  /** True if there are no property values.
  */
  def isEmpty: Boolean = {
    data.isEmpty
  }

  /** Find URNs for each object in the collection.
  */
  def objects = {
    data.map(_.urn.dropProperty).distinct.toSet
  }

  /** Find URNs for each property value in the collection.
  */
  def properties = {
    data.map(_.urn.dropSelector).distinct.toSet
  }

  /** Value for a single property value.
  * It is a CiteObjectException if propUrn does not
  * identify exactly 1 property value.
  *
  * @param propUrn URN identifying a [[CitePropertyValue]]
  */
  def propertyValue(propUrn: Cite2Urn) = {
    val selectProperty  = this  ~~ propUrn
    selectProperty.size match {
      case 1 =>
      case 0 => throw CiteObjectException("No property value found matching " + propUrn)
      case _ => throw CiteObjectException("Too general URN: found " + selectProperty.size + " matches.")
    }
  }


}
