package edu.holycross.shot.citeobj
import org.scalatest.FlatSpec
import edu.holycross.shot.cite._


/**
*/
class CiteCollectionDefSpec extends FlatSpec {

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
    "CC attribution-share-alike",
    propsVector
  )


  "The definition of a CITE data collection" should  "have a URN" in {
    ccDef.urn match {
      case u: Cite2Urn => assert(true)
      case _ => "Could not find a URN for the CITE collection in its definition"
    }
  }

  it should "have a property for a human-readable label" in {
    ccDef.labelProperty match {
      case u: Cite2Urn  => assert(true)
      case _ => "Could not find a label for the CITE collection in its definition"
    }
  }

  it should "have a vector of Cite property definitions" in {
    ccDef.propertyDefs match {
      case v: Vector[CitePropertyDef]  => assert(true)
      case _ => "Could not find property definitions for the CITE collection"
    }
  }

  it should "indicate whether or not the collection is ordered" in {
    assert (ccDef.isOrdered)
  }

  it should "default to assuming collections are not ordered" in pending

  it should "identify the ordering property in ordered collections" in {
    assert (ccDef.isOrdered)
    ccDef.orderingProperty match {
      case orderProp: Some[Cite2Urn] => assert(true)
      case _ => "Could not find ordering property for an ordered CITE collection"
    }
  }


}
