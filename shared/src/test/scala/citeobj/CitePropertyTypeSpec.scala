package edu.holycross.shot.citeobj

import org.scalatest.FlatSpec
import edu.holycross.shot.cite._


/**
*/
class CitePropertyTypeSpec extends FlatSpec {

  "CitePropertyTypes" should "be fully enumerated" in {
    for (cpt <- List(CtsUrnType,Cite2UrnType,StringType, BooleanType,NumericType,ControlledVocabType)) {
      val citePropType: CitePropertyType = cpt
    }
  }
}
