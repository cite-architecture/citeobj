package edu.holycross.shot.citeobj

import edu.holycross.shot.cite._

/** Sealed trait for explicit enumeration of CITE property types.
*/
sealed abstract trait CitePropertyType  {
	def cex:String 
}

/** Type of all CtsUrn values.*/
case object CtsUrnType extends CitePropertyType  {
	def cex:String = "CtsUrn"
}

/** Type of all Cite2Urn values.*/
case object Cite2UrnType extends CitePropertyType {
	def cex:String = "Cite2Urn"
}

/** Type of all numeric values.*/
case object NumericType extends CitePropertyType {
	def cex:String = "Number"
}

/** Type of all boolean values.*/
case object BooleanType extends CitePropertyType {
	def cex:String = "Boolean"
}

/** Type of all unconstrained String values.*/
case object StringType extends CitePropertyType {
	def cex:String = "String"
}

/** Type of all String values limited to values in an explicitly defined
* set of values (or vocabulary list).
*/
case object ControlledVocabType extends CitePropertyType {
	def cex:String = "String"
}
