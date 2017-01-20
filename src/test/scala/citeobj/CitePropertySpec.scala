package edu.holycross.shot.citeobj

import org.scalatest.FlatSpec
import edu.holycross.shot.cite._
import scala.io.Source

/**
*/
class CitePropertySpec extends FlatSpec {

  "A CITE property" should "have a string value for name" in {
    val prop = CiteProperty("side", "recto")
    prop.propertyName match {
      case s: String => assert(true)
      case _ => fail("Should have found a string value for name")
    }
  }
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
