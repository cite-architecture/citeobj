package edu.holycross.shot.citeobj

import org.scalatest.FlatSpec
import edu.holycross.shot.cite._



/**
*/
class CitePropertyValueObjectSpec extends FlatSpec {


  val propsVector  = Vector(

    CitePropertyDef(Cite2Urn("urn:cite2:hmt:speeches.v1.passage:"),"Text passage",CtsUrnType),

    CitePropertyDef(Cite2Urn("urn:cite2:hmt:speeches.v1.speaker:"),"Speaker",Cite2UrnType),

    CitePropertyDef(Cite2Urn("urn:cite2:hmt:speeches.v1.sequence:"),"Sequence",NumericType) ,

    CitePropertyDef(Cite2Urn("urn:cite2:hmt:speeches.v1.speechType:"),"Type of speech",ControlledVocabType,Vector("battle speech", "let's eat dinner","yo mama")),

    CitePropertyDef(Cite2Urn("urn:cite2:hmt:speeches.v1.contents:"),"Text of speech",StringType)

  )

  val ccDef = CiteCollectionDef(
    Cite2Urn("urn:cite2:hmt:speeches.v1:"),
    "Speeches in the Iliad",
    None,
    Some(Cite2Urn("urn:cite2:hmt:speeches.v1.sequence:")),
    propsVector
  )



  "The CITEPropertyValue object" should "form a CitePropertyValue for a String" in {
    val propValue = CitePropertyValue.valueForString("I remember, long ago, ....", StringType)
    assert (propValue == "I remember, long ago, ....")
  }
  it should "form a CitePropertyValue for a String with controlled vocabulary" in pending
  it should "form a CitePropertyValue for a CtsUrn" in pending
  it should "form a CitePropertyValue for a Cite2Urn" in pending
  it should "form a CitePropertyValue for a NumericType" in pending
  it should "form a CitePropertyValue for a BooleanType" in pending

}
