package edu.holycross.shot.citeobj

import org.scalatest.FlatSpec
import edu.holycross.shot.cite._



/**
*/
class CitePropertyValueObjectSpec extends FlatSpec {

  "The CITEPropertyValue object" should "form a CitePropertyValue for a String" in {
    val propDef = CitePropertyDef(Cite2Urn("urn:cite2:hmt:speeches.v1.contents:"),"Text of speech",StringType)
    val propValue = CitePropertyValue.valueForString("I remember, long ago, ....", propDef)
    assert (propValue == "I remember, long ago, ....")
  }
  it should "form a CitePropertyValue for a String with controlled vocabulary" in {
    val propDef = CitePropertyDef(Cite2Urn("urn:cite2:hmt:speeches.v1.speechType:"),"Type of speech",ControlledVocabType,Vector("battle speech", "let's eat dinner","yo mama"))
    val propValue = CitePropertyValue.valueForString("battle speech", propDef)

    assert (propValue == "battle speech")
  }

  it should "throw an exception if controlled vocabulary does not match" in {
    val propDef = CitePropertyDef(Cite2Urn("urn:cite2:hmt:speeches.v1.speechType:"),"Type of speech",ControlledVocabType,Vector("battle speech", "let's eat dinner","yo mama"))

    try {
      val propValue = CitePropertyValue.valueForString("Battle speeches", propDef)
      fail ("Should not have formed property value")
    } catch {
      case coe: CiteObjectException => assert(coe.message == "Value Battle speeches is not in controlled vocabulary battle speech, let's eat dinner, yo mama")
      case e: Throwable => fail("Should have thrown a CiteObjectException: " + e)
    }
  }


  it should "form a CitePropertyValue for a CtsUrn" in {
    val propDef = CitePropertyDef(Cite2Urn("urn:cite2:hmt:speeches.v1.passage:"),"Text passage",CtsUrnType)
    val propValue = CitePropertyValue.valueForString("urn:cts:greekLit:tlg0012.tlg001:1.21-1.46", propDef)
    assert(propValue == CtsUrn("urn:cts:greekLit:tlg0012.tlg001:1.21-1.46"))
  }
  it should "throw an exception if the CtsUrn is not syntactically valid" in {
    val propDef = CitePropertyDef(Cite2Urn("urn:cite2:hmt:speeches.v1.passage:"),"Text passage",CtsUrnType)

    try {
      val propValue = CitePropertyValue.valueForString("malformed:urn:value:", propDef)
      fail ("Should not have formed property value")
    } catch {
      case iae: IllegalArgumentException => assert(iae.getMessage() == "requirement failed: Invalid URN syntax: too few components in malformed:urn:value:")
      case e: Throwable => fail("Should have thrown a CiteObjectException: " + e)
    }
  }
  it should "form a CitePropertyValue for a Cite2Urn" in {
    val propDef = CitePropertyDef(Cite2Urn("urn:cite2:hmt:speeches.v1.speaker:"),"Speaker",Cite2UrnType)

    val propValue = CitePropertyValue.valueForString("urn:cite2:hmt:people.release1:persname1", propDef)
    assert (propValue == Cite2Urn("urn:cite2:hmt:people.release1:persname1"))
  }
  it should "throw an exception if the Cite2Urn is not syntactically valid" in {
    val propDef = CitePropertyDef(Cite2Urn("urn:cite2:hmt:speeches.v1.speaker:"),"Speaker",Cite2UrnType)


    try {
      val propValue = CitePropertyValue.valueForString("malformed:urn:value:", propDef)
      fail ("Should not have formed property value")
    } catch {
      case iae: IllegalArgumentException => assert(iae.getMessage() == "requirement failed: wrong number of components in  malformed:urn:value: - 3")
      case e: Throwable => fail("Should have thrown a CiteObjectException: " + e)
    }

  }
  it should "form a Double for a NumericType" in {
    val propDef = CitePropertyDef(Cite2Urn("urn:cite2:hmt:speeches.v1.sequence:"),"Sequence",NumericType)
    val propValue = CitePropertyValue.valueForString("2", propDef)
    assert(propValue == 2)
  }
  it should "throw an exception if the string is not parseable as a number" in {
    val propDef = CitePropertyDef(Cite2Urn("urn:cite2:hmt:speeches.v1.sequence:"),"Sequence",NumericType)

    try {
      val propValue = CitePropertyValue.valueForString("2a", propDef)
      fail ("Should not have formed property value")
    } catch {
      case nfe : NumberFormatException => assert (nfe.getMessage() == """For input string: "2a"""")
      case iae: IllegalArgumentException => assert(iae.getMessage() == "")
      case e: Throwable => fail("Should have thrown a CiteObjectException: " + e)
    }
  }

  it should "form a boolean for a BooleanType" in {
    val propDef = CitePropertyDef(Cite2Urn("urn:cite2:hmt:speeches.v1.textReuse:"),"Appears in text reuse",BooleanType)

    val propValue = CitePropertyValue.valueForString("true", propDef)
    assert (propValue == true)
  }
  it should "form a boolean if the string is not recognizable as a boolean" in {
    val propDef = CitePropertyDef(Cite2Urn("urn:cite2:hmt:speeches.v1.textReuse:"),"Appears in text reuse",BooleanType)
    try {
      val propValue = CitePropertyValue.valueForString("possibly", propDef)
      fail ("Should not have formed property value")
    } catch {
      
      case iae: IllegalArgumentException => assert(iae.getMessage() == """For input string: "possibly"""")
      case e: Throwable => fail("Should have thrown a CiteObjectException: " + e)
    }


  }

}
