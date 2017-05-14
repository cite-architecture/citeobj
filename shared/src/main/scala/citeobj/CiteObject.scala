package edu.holycross.shot.citeobj

import edu.holycross.shot.cite._


/** Trait defining the minimum functionality of a discrete citable object. */
trait BaseCitable {

  /** URN uniquely identifying this object.  The URN must include
  * an object selector component, and its collection hierarachy must be at the version level.
  */
  def urn: Cite2Urn


  /** Human-readable string labelling this object. */
  def label: String

  /** Vector of any further [[CitePropertyValue]]s belonging to this object.
  * Note that the Vector may be empty.
  */
  def propertyList: Vector[CitePropertyValue]
}


/** Representation of a single citable object.  Note that this can easily be
* constructed from the properties in a set of [[CiteCollectionData]] by URN twiddling, but
* since the [[CiteObject]] representation elevates `urn` and `label` properties into
* object members, its `propertyList` will have two items fewer than the properties
* resulting from twiddling on a [[CiteCollectionData]] or from consulting the
* [[CiteCatalog]] corresponding to the [[CiteCollectionData]].
*
* @param urn Identifier required by [[BaseObject]] trait.
* @param label Human-readable string required by [[BaseObject]] trait.
* @param propertyList Possibly empty vector of any further [[CitePropertyValue]]s
* belonging to this object.
*/
case class CiteObject(urn: Cite2Urn, label: String, propertyList: Vector[CitePropertyValue]) extends BaseCitable {
  //
}
