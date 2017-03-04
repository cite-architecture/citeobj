package edu.holycross.shot.citeobj
import org.scalatest.FlatSpec
import edu.holycross.shot.cite._
import scala.io.Source

/**
*/
class CiteCollectionSpec extends FlatSpec {


  "A CITE collection" should "offer a constructor signature for instantiating a corpus from a delimited text file" in  pending


  it should  "have a vector of cite objects" in pending /* {
    val urn = Cite2Urn("urn:cite2:hmt:msA.v1:12r")
    val props = Vector(
      CiteProperty("folio", "12"),
      CiteProperty("side", "recto"),
      CiteProperty("pageseq", 25),
      CiteProperty("isSkin", true)
    )
    val obj = CiteObject(urn,props)
    val coll = CiteCollection( Vector(obj), true)
    assert (coll.citeObjects.size == 1)
  }
  it should "indicate whether the collection is ordered" in {
    val urn = Cite2Urn("urn:cite2:hmt:msA.v1:12r")
    val props = Vector(
      CiteProperty("folio", "12"),
      CiteProperty("side", "recto"),
      CiteProperty("pageseq", 25),
      CiteProperty("isSkin", true)
    )
    val obj = CiteObject(urn,props)
    val coll = CiteCollection( Vector(obj), true)
    assert (coll.isOrdered)
  }
  it should "default to non-ordered collection" in {
    val urn = Cite2Urn("urn:cite2:hmt:msA.v1:12r")
    val props = Vector(
      CiteProperty("folio", "12"),
      CiteProperty("side", "recto"),
      CiteProperty("pageseq", 25),
      CiteProperty("isSkin", true)
    )
    val obj = CiteObject(urn,props)
    val coll = CiteCollection( Vector(obj))
    assert (coll.isOrdered == false)
  }
*/

    /*
    val relationSet = CiteRelationSet("src/test/resources/venA-Iliad-1.tsv")
    relationSet match {
      case oc: CiteRelationSet => assert(true)
      case  _ => fail("Should have created a CiteRelationSet")
    }
    assert (relationSet.relations.size == 611)
    */



}
