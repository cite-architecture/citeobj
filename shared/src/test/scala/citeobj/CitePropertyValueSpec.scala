package edu.holycross.shot.citeobj

import org.scalatest.FlatSpec
import edu.holycross.shot.cite._


/**
*/
class CitePropertyValueSpec extends FlatSpec {

  "A CITE property value" should "have a URN" in {
    val citeValue =
    CitePropertyValue(Cite2Urn("urn:cite2:hmt:speeches.v1.passage:speech4"),CtsUrn("urn:cts:greekLit:tlg0012.tlg001:1.85-1.91"))

    citeValue.urn match {
      case u: Cite2Urn => assert(true)
      case _ => fail("Did not find a URN for Cite property value.")
    }
  }

  it should "throw an exception if the URN include a property-level collection component" in {
    try {
      val citeValue =  CitePropertyValue(Cite2Urn("urn:cite2:hmt:speeches.v1:speech4"),CtsUrn("urn:cts:greekLit:tlg0012.tlg001:1.85-1.91"))
    } catch {
      case ce: CiteException => assert(ce.message == "No property defined in urn:cite2:hmt:speeches.v1:speech4")
      case t: Throwable => fail("Should have thrown CiteException, but threw " + t)
    }

  }
  it should "throw an exception if the URN does not include an object selector" in {
    try {
      val citeValue =  CitePropertyValue(Cite2Urn("urn:cite2:hmt:speeches.v1.speech:"),CtsUrn("urn:cts:greekLit:tlg0012.tlg001:1.85-1.91"))
    } catch {
      case ce: CiteException => assert(ce.message == "No object component defined in urn:cite2:hmt:speeches.v1.speech:")
      case t: Throwable => fail("Should have thrown CiteException, but threw " + t)
    }
  }

}
