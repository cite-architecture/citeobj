package edu.holycross.shot.citeobj
import org.scalatest.FlatSpec
import edu.holycross.shot.cite._


/**
*/
class CiteObjectSpec extends FlatSpec {

  val propsVector: Vector[CitePropertyDef] = Vector(
    CitePropertyDef(Cite2Urn("urn:cite2:hmt:speeches.v1.passage:"),"Text passage",CtsUrnType),
    CitePropertyDef(Cite2Urn("urn:cite2:hmt:speeches.v1.speaker:"),"Speaker",Cite2UrnType),
    CitePropertyDef(Cite2Urn("urn:cite2:hmt:speeches.v1.sequence:"),"Sequence",NumericType)
  )


  val ccDef = CiteCollectionDef(
    Cite2Urn("urn:cite2:hmt:speeches.v1:"),
    "Speeches in the Iliad",
    None,
    Some(Cite2Urn("urn:cite2:hmt:speeches.v1.sequence:")),
    propsVector
  )


  "A CITE object" should "be construction by a repository" in pending


}
