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




}
