package edu.holycross.shot.citeobj

import org.scalatest.FlatSpec
import edu.holycross.shot.cite._




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

  it should "ensure that the property URN has no object selector" in {
    try {
      val citePropertyDef = CitePropertyDef(Cite2Urn("urn:cite2:hmt:speeches.v1.passage:psg1"),"Text passage", CtsUrnType)
      fail("Should not have created property definition")
    } catch {
      case iae: IllegalArgumentException => assert (iae.getMessage() == "requirement failed: Property definition cannot include object selector: urn:cite2:hmt:speeches.v1.passage:psg1")
      case e: Throwable => fail("Should have created IllegalArgumentException: " + e)
    }
  }
  it should "ensure that the property URN has a collection component 2ith propert-level identifier" in {
    try {
      val citePropertyDef = CitePropertyDef(Cite2Urn("urn:cite2:hmt:speeches.v1:"),"Text passage", CtsUrnType)
      fail("Should not have created property definition")
    } catch {
      case iae: CiteObjectException => assert (iae.getMessage() == "URN must include property part: urn:cite2:hmt:speeches.v1:")
      case e: Throwable => fail("Should have created CiteObjectException: " + e)
    }
  }
}
