package edu.holycross.shot.citeobj

import edu.holycross.shot.cite._


sealed abstract trait CitePropertyType

case object CtsUrnType extends CitePropertyType
case object Cite2UrnType extends CitePropertyType
case object NumericType extends CitePropertyType
case object BooleanType extends CitePropertyType
case object StringType extends CitePropertyType
case object ControlledVocabType extends CitePropertyType
