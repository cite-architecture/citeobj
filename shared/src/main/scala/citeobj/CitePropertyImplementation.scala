package edu.holycross.shot.citeobj

import edu.holycross.shot.cite._

import scala.scalajs.js
import js.annotation.JSExport

/** Definition of the structure of single CITE property.
*
* @param urn URN for the property.  Note that this must include a collection
* component at the property level, and must *not* include an object selector.
* @param label Human-readable label for the property.
* @param propertyType [[CitePropertyType]] applying to this property.
* @param vocabularyList Possibly empty Vector with a controlled vocabulary list
* for this property.  Note that the list can only be non-empty if the [[CitePropertyType]]
* for this property is `StringType`.
*/
@JSExport  case class CitePropertyImplementation(urn: Cite2Urn,
  label: String,
  propertyDef: CitePropertyDef,
  propertyValue: Any
) {
  require(validValue(propertyValue,propertyDef))
}
