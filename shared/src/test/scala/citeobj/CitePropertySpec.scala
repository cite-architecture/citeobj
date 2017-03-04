package edu.holycross.shot.citeobj

import org.scalatest.FlatSpec
import edu.holycross.shot.cite._


/**
*/
class CitePropertySpec extends FlatSpec {

  "A CITE property" should "recognize CtsUrn types" in {
    val citeProperty = CiteProperty("text",CtsUrnType,CtsUrn("urn:cts:greekLit:tlg0012.tlg001:"))

    val expectedValue = CtsUrn("urn:cts:greekLit:tlg0012.tlg001:")
    val expectedName = "text"
    val expectedType = CtsUrnType

    assert(citeProperty.propertyValue == expectedValue)
    assert(citeProperty.propertyDef.propertyName == expectedName)
    assert(citeProperty.propertyDef.propertyType == expectedType)
  }
  it should "throw an exception if a the value  for a CtsUrn type is not a CtsUrn" in {
    try {
      val citeProperty = CiteProperty("text",CtsUrnType,"urn:cts:greekLit:tlg0012.tlg001:")
      fail("Should have thrown a CiteObjectException")
    } catch {
      case coex: CiteObjectException => assert (coex.message == "value urn:cts:greekLit:tlg0012.tlg001: is not a CtsUrn")
      case _ : Throwable => fail("Should have thrown a CiteObjectException")
    }
  }

  it should "recognize Cite2Urn types" in {
    val citeProperty = CiteProperty("personal name",Cite2UrnType,Cite2Urn("urn:cite2:hmt:pers:pers1"))

    val expectedValue = Cite2Urn("urn:cite2:hmt:pers:pers1")
    val expectedName = "personal name"
    val expectedType = Cite2UrnType

    assert(citeProperty.propertyValue == expectedValue)
    assert(citeProperty.propertyDef.propertyName == expectedName)
    assert(citeProperty.propertyDef.propertyType == expectedType)
  }

  it should "throw an exception if a the value  for a Cite2Urn type is not a Cite2Urn" in {
    try {
      val citeProperty = CiteProperty("personal name",Cite2UrnType,"urn:cite2:hmt:pers:pers1")
      fail("Should have thrown a CiteObjectException")
    } catch {
      case coex: CiteObjectException => assert (coex.message == "value urn:cite2:hmt:pers:pers1 is not a Cite2Urn")
      case _ : Throwable => fail("Should have thrown a CiteObjectException")
    }
  }




  it should "recognize numeric types" in {
    val citeProperty = CiteProperty("sequence",NumericType,12)

    val expectedValue = 12
    val expectedName = "sequence"
    val expectedType = NumericType

    assert(citeProperty.propertyValue == expectedValue)
    assert(citeProperty.propertyDef.propertyName == expectedName)
    assert(citeProperty.propertyDef.propertyType == expectedType)
  }

  it should "throw an exception if a the value  for a numeric type is not a number" in {
    try {
      val citeProperty = CiteProperty("sequence",NumericType,"12")
      fail("Should have thrown a CiteObjectException")
    } catch {
      case coex: CiteObjectException => assert (coex.message == "value 12 is not a Numeric")
      case _ : Throwable => fail("Should have thrown a CiteObjectException")
    }
  }



  it should "recognize boolean types" in {
    val citeProperty = CiteProperty("validated",BooleanType,true)

    val expectedValue = true
    val expectedName = "validated"
    val expectedType = BooleanType

    assert(citeProperty.propertyValue == expectedValue)
    assert(citeProperty.propertyDef.propertyName == expectedName)
    assert(citeProperty.propertyDef.propertyType == expectedType)
  }

  it should "throw an exception if a the value  for a boolean type is not a boolean" in {
    try {
        val citeProperty = CiteProperty("validated",BooleanType,"true")
      fail("Should have thrown a CiteObjectException")
    } catch {
      case coex: CiteObjectException => assert (coex.message == "value true is not a Boolean")
      case _ : Throwable => fail("Should have thrown a CiteObjectException")
    }
  }



  it should "recognize string types" in {
    val citeProperty = CiteProperty("page",StringType,"12r")

    val expectedValue = "12r"
    val expectedName = "page"
    val expectedType = StringType

    assert(citeProperty.propertyValue == expectedValue)
    assert(citeProperty.propertyDef.propertyName == expectedName)
    assert(citeProperty.propertyDef.propertyType == expectedType)
  }

  it should "throw an exception if a the value  for a string type is not a string" in {
    try {
      val citeProperty = CiteProperty("page",StringType,12)
      fail("Should have thrown a CiteObjectException")
    } catch {
      case coex: CiteObjectException => assert (coex.message == "value 12 is not a String")
      case _ : Throwable => fail("Should have thrown a CiteObjectException")
    }
  }


  /* {
    val prop = CiteProperty("side", "recto")
    prop.propertyName match {
      case s: String => assert(true)
      case _ => fail("Should have found a string value for name")
    }
  }*/
  "A CITE property" should "have a value" in pending

  /* "accept a CTS URN for its first relation" in {
    val il1_1 = CtsUrn("urn:cts:greekLit:tlg0012.tlg001.msA:1.1")
    val img = Cite2Urn("urn:cite2:hmt:vaimg.r1:VA012RN_0013@0.0611,0.2252,0.4675,0.0901")

    val triple = CiteTriple(il1_1, "appears on", img)

    triple.urn1 match {
      case cts: CtsUrn => assert(true)
      case _ => fail("Should have found a CtsUrn")
    }
  }*/




}
