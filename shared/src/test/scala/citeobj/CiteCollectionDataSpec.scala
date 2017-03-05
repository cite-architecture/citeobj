package edu.holycross.shot.citeobj
import org.scalatest.FlatSpec
import edu.holycross.shot.cite._


/**
*/
class CiteCollectionDataSpec extends FlatSpec {

  val dataV: Vector[CiteProperty] = Vector(
    CiteProperty(Cite2Urn("urn:cite2:hmt:speeches.v1.speaker:speech1"),Cite2Urn("urn:cite2:hmt:pers:pers22")),
    CiteProperty(Cite2Urn("urn:cite2:hmt:speeches.v1.speaker:speech4"),Cite2Urn("urn:cite2:hmt:pers:pers1")),
    CiteProperty(Cite2Urn("urn:cite2:hmt:speeches.v1.passage:speech4"),CtsUrn("urn:cts:greekLit:tlg0012.tlg001:1.85-1.91"))
  )

  "A CITE collection" should  "have a vector of cite properties" in {

  }
  it should "support selecting all properties of an object using URN twiddling" in {
    val dataCollection = CiteCollectionData(dataV)

    val speech4 = dataCollection ~~ Cite2Urn("urn:cite2:hmt:speeches:speech4")
    assert(speech4.data.size == 2 )
    val props = List("speaker", "passage")
    for (prop <- speech4.data) {
      assert (props.contains(prop.urn.property))
    }
  }


  it should "support  selecting all instances of a property using URN twiddling" in {
    val dataCollection = CiteCollectionData(dataV)
    val speakers = dataCollection ~~ Cite2Urn("urn:cite2:hmt:speeches.v1.speaker:")
    assert(speakers.data.size == 2 )
    val speakerIds = List(Cite2Urn("urn:cite2:hmt:pers:pers1"), Cite2Urn("urn:cite2:hmt:pers:pers22"))

    for (speaker <- speakers.data) {
      assert (speakerIds.contains(speaker.propertyValue))
    }
  }


}
