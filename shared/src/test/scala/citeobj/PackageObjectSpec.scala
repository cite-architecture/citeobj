package edu.holycross.shot.citeobj
import org.scalatest.FlatSpec
import edu.holycross.shot.cite._

import edu.holycross.shot.cite._

/**
*/
class PackageObjectSpec extends FlatSpec {

  "The package object" should "convert CSV text to a map"  in {
    val src = """C1,C2,C3
r1a,r1b,r1c
r2a,r2b,r2c
"""
    val mapped = mapsForDelimited(src,",")
    val r1 = mapped(0)
    assert(r1("C1") == "r1a")
    assert(r1("C3") == "r1c")
  }

  it should "throw and exception if row size is not equal to header size" in {
    val src = """C1,C2,C3
r1a,r1b,r1c
r2a,r2b,r2c,error
r3a,r3b,r3c
"""
    try {
      val mapped = mapsForDelimited(src,",")
      fail("Should not have created map")
    } catch {
      case coe: CiteObjectException => {
        //println(coe)
        assert(true)
      }
      case t: Throwable => fail("Should have thrown CiteObjectException, but threw " + t)
    }
  }

  it should "determine if a value is valid for a CtsUrn property definition" in {
    val propDef = CitePropertyDef(Cite2Urn("urn:cite2:hmt:speeches.v1.passage:"),"Text passage", CtsUrnType)
    assert(validValue(CtsUrn("urn:cts:greekLit:tlg0012.tlg001:1.7"), propDef))
    assert(validValue("urn:cts:greekLit:tlg0012.tlg001:1.7", propDef) == false)
  }
  it should "determine if a value is valid for a Cite2Urn property definition" in {
    val propDef = CitePropertyDef(Cite2Urn("urn:cite2:hmt:speeches.v1.speech:"),"Speech", Cite2UrnType)
    val okUrn = Cite2Urn("urn:cite2:hmt:speeches.v1.speech:s1")
    assert(validValue(okUrn, propDef))
    assert(validValue("urn:cite2:hmt:speeches.v1.speech:", propDef) == false)
  }
  it should "determine if a value is valid for a Numeric property definition" in {
    val propDef = CitePropertyDef(Cite2Urn("urn:cite2:hmt:speeches.v1.seq:"),"Sequence", NumericType)
    assert(validValue(3,propDef))
    assert(validValue("3",propDef) == false)
  }
  it should "determine if a value is valid for a Boolean property definition" in {
    val propDef = CitePropertyDef(Cite2Urn("urn:cite2:hmt:speeches.v1.paragraphed:"),"Marked by paragraphos", BooleanType)
    assert(validValue(true,propDef))
    assert(validValue("true", propDef) == false)
  }
  it should "determine if a value is valid for a String property definition" in {
    val propDef = CitePropertyDef(Cite2Urn("urn:cite2:hmt:speeches.v1.label:"),"Label", StringType)
    assert(validValue("3",propDef))
    assert(validValue(true,propDef) == false)
  }
  it should "determine if a value is valid for a controlled vocabulary property definition" in {
    val propDef = CitePropertyDef(Cite2Urn("urn:cite2:hmt:speeches.v1.type:"),"Type of speech",ControlledVocabType, Vector("battle", "council"))
    assert(validValue("battle",propDef))
    assert(validValue("Battle",propDef) == false)
  }

}
