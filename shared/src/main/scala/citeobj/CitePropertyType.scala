package edu.holycross.shot.citeobj

import edu.holycross.shot.cite._

/** Sealed trait for explicit enumeration of CITE property types.
*/
sealed abstract trait CitePropertyType

/** Type of all CtsUrn values.*/
case object CtsUrnType extends CitePropertyType

/** Type of all Cite2Urn values.*/
case object Cite2UrnType extends CitePropertyType

/** Type of all numeric values.*/
case object NumericType extends CitePropertyType

/** Type of all boolean values.*/
case object BooleanType extends CitePropertyType

/** Type of all unconstrained String values.*/
case object StringType extends CitePropertyType

/** Type of all String values limited to values in an explicitly defined
* set of values (or vocabulary list).
*/
case object ControlledVocabType extends CitePropertyType
