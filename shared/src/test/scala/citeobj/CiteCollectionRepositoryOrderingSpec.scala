package edu.holycross.shot.citeobj
import org.scalatest.FlatSpec
import edu.holycross.shot.cite._


/**
*/
class CiteCollectionRepositoryOrderingSpec extends FlatSpec {


  val cex = """#!citecollections
URN#Description#Labelling property#Ordering property#License
urn:cite2:hmt:speeches.v1:#Speeches in the Iliad#urn:cite2:hmt:speeches.v1.label:#urn:cite2:hmt:speeches.v1.sequence:#CC-attribution-share-alike

#!citeproperties
Property#Label#Type#Authority list
urn:cite2:hmt:speeches.v1.urn:#URN#Cite2Urn#
urn:cite2:hmt:speeches.v1.speaker:#Speaker#Cite2Urn#
urn:cite2:hmt:speeches.v1.label:#Label#String#
urn:cite2:hmt:speeches.v1.passage:#Passage#CtsUrn#
urn:cite2:hmt:speeches.v1.sequence:#Speech Sequence#Number#

#!citedata
urn#speaker#label#passage#sequence
urn:cite2:hmt:speeches.v1:speech1#urn:cite2:hmt:pers:pers22#Speech 1#urn:cts:greekLit:tlg0012.tlg001:1.26-1.32#1
urn:cite2:hmt:speeches.v1:speech3#urn:cite2:hmt:pers:pers22#Speech 3#urn:cts:greekLit:tlg0012.tlg001:1.26-1.32#3
urn:cite2:hmt:speeches.v1:speech2#urn:cite2:hmt:pers:pers22#Speech 2#urn:cts:greekLit:tlg0012.tlg001:1.26-1.32#2
urn:cite2:hmt:speeches.v1:speech4#urn:cite2:hmt:pers:pers1#Speech 4#urn:cts:greekLit:tlg0012.tlg001:1.85-1.91#4
"""

  val collectionUrn = Cite2Urn("urn:cite2:hmt:speeches.v1:")
  val repo = CiteCollectionRepository(cex,"#",",")

  "A Cite Collection repository" should "recognize an ordered collection" in  {
      assert(repo isOrdered Cite2Urn("urn:cite2:hmt:speeches.v1:"))
  }

  it should "find the first CiteObject in an ordered collection" in {
    val expected = repo.citableObject(Cite2Urn("urn:cite2:hmt:speeches.v1:speech1"))
    assert(repo.first(collectionUrn) == expected)
  }

  it should "find the last CiteObject in an ordered collection" in {
    val expected = repo.citableObject(Cite2Urn("urn:cite2:hmt:speeches.v1:speech4"))
    assert(repo.last(collectionUrn) == expected)
  }


  it should "find the index of an object in an ordered vector" in {
    val obj = repo.citableObject(Cite2Urn("urn:cite2:hmt:speeches.v1:speech1"))
    assert(repo.indexOf(obj) == 0)
  }

  it should "find the index of another object in an ordered vector" in {
    val obj = repo.citableObject(Cite2Urn("urn:cite2:hmt:speeches.v1:speech2"))
    assert(repo.indexOf(obj) == 1)
  }

  it should "preserve order when twiddling on the repo" in {
    val objs = repo ~~ Cite2Urn("urn:cite2:hmt:speeches.v1:")
    val objZero = repo.citableObject(Cite2Urn("urn:cite2:hmt:speeches.v1:speech1"))
    val objOne = repo.citableObject(Cite2Urn("urn:cite2:hmt:speeches.v1:speech2"))
    val objTwo = repo.citableObject(Cite2Urn("urn:cite2:hmt:speeches.v1:speech3"))
    val objThree = repo.citableObject(Cite2Urn("urn:cite2:hmt:speeches.v1:speech4"))

    assert(objs.size == 4)
    assert(objs(3) == objThree)
    assert(objs(1) == objOne)
    assert(objs(2) == objTwo)
    assert(objs(0) == objZero)

  }
  /*
  it should "find the CiteObject following a given element in an ordered collection" in {
      val obj1 = repo.citableObject(Cite2Urn("urn:cite2:hmt:msA.v1:1r"))
      val expected = repo.citableObject(Cite2Urn("urn:cite2:hmt:msA.v1:1v"))
      assert (repo.next(obj1) == Some(expected))
  }
  it should "find the CiteObject preceding a given element in an ordered collection" in {
    val expected = repo.citableObject(Cite2Urn("urn:cite2:hmt:msA.v1:1r"))
    val obj1 = repo.citableObject(Cite2Urn("urn:cite2:hmt:msA.v1:1v"))
    assert (repo.prev(obj1) == Some(expected))
  }


  it should "create a citable object for an identifier" in {
    val u = Cite2Urn("urn:cite2:hmt:msA.v1:1v")
    val citableObj = repo.citableObject(u)
    assert(citableObj.urn == u)
  }

  it should "create a vector of citable objects for all data in the repository" in {
    val v = repo.citableObjects
    assert (v.size == 3)
  }

  it should "create a vector of citable objects for an ordered collection" in  {
    val u = Cite2Urn("urn:cite2:hmt:msA.v1:")
    val v = repo.citableObjects(u)
    assert (v(0).urn == Cite2Urn("urn:cite2:hmt:msA.v1:1r"))
  }

  it should "find the sequence value of a CiteObject given its orderingProperty " in {
    val u = Cite2Urn("urn:cite2:hmt:msA.v1:2r")
    val orderProp = Cite2Urn("urn:cite2:hmt:msA.v1.sequence:")
    val obj = repo.citableObject(u)
    assert (repo.sortValue(obj,orderProp) == 3.0)
  }

  it should "find the sequence value of a CiteObject even if not given its orderingProperty " in {
    val u = Cite2Urn("urn:cite2:hmt:msA.v1:2r")
    val obj = repo.citableObject(u)
    assert (repo.sortValue(obj) == 3.0)
  }

*/
}
