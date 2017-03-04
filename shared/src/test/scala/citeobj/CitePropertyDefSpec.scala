package edu.holycross.shot.citeobj

import org.scalatest.FlatSpec
import edu.holycross.shot.cite._


/**
*/
class CitePropertyDefSpec extends FlatSpec {

  "A CITE property specification" should "have a human readable label" in {
    val citePropertyDef = CitePropertyDef("text", CtsUrnType)
    assert (citePropertyDef.propertyName == "text")
  }

  it should "have a property type" in {
    val citePropertyDef = CitePropertyDef("text", CtsUrnType)
    assert (citePropertyDef.propertyType == CtsUrnType)
  }


}
