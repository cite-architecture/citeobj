package edu.holycross.shot.citeobj

import org.scalatest.FlatSpec
import edu.holycross.shot.cite._
import scala.io.Source

/**
*/
class CiteObjectSpec extends FlatSpec {

  "A CITE Object" should "have a Cite2Urn" in  pending /*{
    val urn = Cite2Urn("urn:cite2:hmt:msA.v1:12r")
    val props = Vector(
      CiteProperty("folio", "12"),
      CiteProperty("side", "recto"),
      CiteProperty("pageseq", 25),
      CiteProperty("isSkin", true)
    )
    val obj = CiteObject(urn,props)
    obj.urn match {
      case u: Cite2Urn => assert(true)
      case _ => fail("Should have found a Cite2Urn")
    }
  }

  it should "have a non-empty list of properties" in {
    val urn = Cite2Urn("urn:cite2:hmt:msA.v1:12r")
    val props = Vector(
      CiteProperty("folio", "12"),
      CiteProperty("side", "recto"),
      CiteProperty("pageseq", 25),
      CiteProperty("isSkin", true)
    )
    val obj = CiteObject(urn,props)
    assert (obj.properties.nonEmpty)
  }*/
}
