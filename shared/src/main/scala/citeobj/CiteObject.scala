package edu.holycross.shot.citeobj

import edu.holycross.shot.cite._

import scala.scalajs.js
import js.annotation.JSExport

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
  def propertyList: Vector[CitePropertyImplementation]
}


/** Representation of a single citable object.  Note that this can easily be
* constructed from the properties in a set of [[CiteCollectionData]] by URN twiddling, but
* since the [[CiteObject]] representation elevates `urn` and `label` properties into
* object members, its `propertyList` will have two items fewer than the properties
* resulting from twiddling on a [[CiteCollectionData]] or from consulting the
* [[CiteCatalog]] corresponding to the [[CiteCollectionData]].
*
* @param urn Identifier required by [[BaseCitable]] trait.
* @param label Human-readable string required by [[BaseCitable]] trait.
* @param propertyList Possibly empty vector of any further [[CitePropertyValue]]s
* belonging to this object.
*/
@JSExport  case class CiteObject(
  urn: Cite2Urn,
  label: String,
  propertyList: Vector[CitePropertyImplementation]) extends BaseCitable {

    /** Find value of a given property.
    *
    * @param propertyUrn Property to find value for.
    */
    def propertyValue(propertyUrn: Cite2Urn): Any = {
      val matches = propertyList.filter(_.urn == propertyUrn)
      require(matches.size == 1, s"Exception: found s${matches.size} match(es) for ${propertyUrn}")
      matches(0).propertyValue
    }


    /** True if any property of the object matches the given value.
    *
    * @param pValue Value to test for.
    */
    def valueEquals(pValue: Any): Boolean = {
      val matching = propertyList.filter(_.propertyValue == pValue)
      (matching.size > 0)
    }
}
