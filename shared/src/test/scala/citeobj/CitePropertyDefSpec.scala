package edu.holycross.shot.citeobj

import org.scalatest.FlatSpec
import edu.holycross.shot.cite._


/**
*/
class CitePropertyDefSpec extends FlatSpec {

  "A CITE property definition" should "have a property type" in {
    val citePropertyDef = CitePropertyDef(Cite2Urn("urn:cite2:hmt:speeches.v1.passage:"),"Text passage", CtsUrnType)
    assert (citePropertyDef.propertyType == CtsUrnType)
  }
  it should "have a urn" in {
    val citePropertyDef = CitePropertyDef(Cite2Urn("urn:cite2:hmt:speeches.v1.passage:"),"Text passage", CtsUrnType)
    assert (citePropertyDef.urn == Cite2Urn("urn:cite2:hmt:speeches.v1.passage:"))
  }
  it should "have a human-readable label" in {
    val citePropertyDef = CitePropertyDef(Cite2Urn("urn:cite2:hmt:speeches.v1.passage:"),"Text passage", CtsUrnType)
    assert (citePropertyDef.label == "Text passage")
  }

}
