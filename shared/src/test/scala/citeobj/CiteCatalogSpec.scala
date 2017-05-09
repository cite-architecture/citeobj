package edu.holycross.shot.citeobj
import org.scalatest.FlatSpec
import edu.holycross.shot.cite._


/**
*/
class CiteCatalogSpec extends FlatSpec {

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


  "The catalog of a CITE data collection" should "list all cataloged properties" in {
    val catalog = CiteCatalog(Vector(ccDef))
    val expectedProperties = Set(Cite2Urn("urn:cite2:hmt:speeches.v1.passage:"), Cite2Urn("urn:cite2:hmt:speeches.v1.speaker:"), Cite2Urn("urn:cite2:hmt:speeches.v1.sequence:"),
    Cite2Urn("urn:cite2:hmt:speeches.v1.label:")
  )

    assert (catalog.properties == expectedProperties)
  }

  it should "list all cataloged collections" in {
    val catalog = CiteCatalog(Vector(ccDef))
    val expectedCollections = Set(Cite2Urn("urn:cite2:hmt:speeches.v1:"))
    assert (catalog.urns == expectedCollections)
  }
  it should "support urn twiddling" in {
    val catalog = CiteCatalog(Vector(ccDef))

    val speakerProperty = Cite2Urn("urn:cite2:hmt:speeches.v1.speaker:")
    val speakerCatalog = catalog ~~ speakerProperty
    assert (speakerCatalog.size == 1)

    val speechCollection =  Cite2Urn("urn:cite2:hmt:speeches.v1:")
    val speechCatalog = catalog ~~ speechCollection
  }

  it should "support selecting a collection definition by collection URN" in {
    val catalog = CiteCatalog(Vector(ccDef))
    val collectionDef = catalog.collection(Cite2Urn("urn:cite2:hmt:speeches.v1:"))
    collectionDef match {
      case cdef : Option[CiteCollectionDef] => assert(true)
      case _ => fail("Should have found a CiteCollectionDef")
    }
  }


  it should "stringify to CEX format" in pending


}
