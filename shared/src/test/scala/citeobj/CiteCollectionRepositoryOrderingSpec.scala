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

  it should "return the ordering value for an object" in {
    val objZero = repo.citableObject(Cite2Urn("urn:cite2:hmt:speeches.v1:speech1"))
    val objOne = repo.citableObject(Cite2Urn("urn:cite2:hmt:speeches.v1:speech2"))
    val objTwo = repo.citableObject(Cite2Urn("urn:cite2:hmt:speeches.v1:speech3"))
    val sortProp = Cite2Urn("urn:cite2:hmt:speeches.v1.sequence:")
    assert( repo.sortValue(objZero,sortProp).toInt == 1 )
    assert( repo.sortValue(objOne,sortProp).toInt == 2 )
    assert( repo.sortValue(objTwo,sortProp).toInt == 3 )
  }

  it should "correctly do sortValue for real data" in {
    val newCex = """#!citecollections
URN#Description#Labelling property#Ordering property#License
urn:cite2:hmt:msA.v1:#Pages of the Venetus A manuscript#urn:cite2:hmt:msA.v1.label:#urn:cite2:hmt:msA.v1.sequence:#CC-attribution-share-alike

#!citeproperties
Property#Label#Type#Authority list
urn:cite2:hmt:msA.v1.urn:#URN#Cite2Urn#
urn:cite2:hmt:msA.v1.label:#Label#String#
urn:cite2:hmt:msA.v1.siglum:#Manuscript siglum#String#
urn:cite2:hmt:msA.v1.sequence:#Page sequence#Number#
urn:cite2:hmt:msA.v1.rv:#Recto or Verso#String#recto,verso
urn:cite2:hmt:msA.v1.codex:#Codex URN#Cite2Urn#
urn:cite2:hmt:msA.v1.text:#Text#CtsUrn#
urn:cite2:hmt:msA.v1.image:#Image#Cite2Urn#
urn:cite2:hmt:msA.v1.imageROI:#ImageROI#Cite2Urn#

#!citedata
siglum#sequence#urn#rv#label#codex#text#image#imageROI
msA#1#urn:cite2:hmt:msA.v1:1r#recto#Marcianus Graecus Z. 454 (= 822) (Venetus A) folio 1r#urn:cite2:hmt:codex:msA#urn:cts:greekLit:tlg0012.tlg001.allen:1.1-1.25#urn:cite2:hmt:vaimg.v1:VA012RN_0013#urn:cite2:hmt:vaimg.v1:VA012RN_0013@0.208,0.2087,0.086,0.0225
msA#2#urn:cite2:hmt:msA.v1:1v#verso#Marcianus Graecus Z. 454 (= 822) (Venetus A) folio 1v#urn:cite2:hmt:codex:msA#urn:cts:greekLit:tlg0012.tlg001.allen:1.26-1.50#urn:cite2:hmt:vaimg.v1:VA012VN_0514#urn:cite2:hmt:vaimg.v1:VA012VN_0514@0.4975,0.2141,0.3814,0.4793
msA#3#urn:cite2:hmt:msA.v1:2r#recto#Marcianus Graecus Z. 454 (= 822) (Venetus A) folio 2r#urn:cite2:hmt:codex:msA#urn:cts:greekLit:tlg0012.tlg001.allen:1.51-1.75#urn:cite2:hmt:vaimg.v1:VA013RN_0014#urn:cite2:hmt:vaimg.v1:VA013RN_0014@0.1722,0.2329,0.3874,0.4793
msA#4#urn:cite2:hmt:msA.v1:2v#verso#Marcianus Graecus Z. 454 (= 822) (Venetus A) folio 2v#urn:cite2:hmt:codex:msA#urn:cts:greekLit:tlg0012.tlg001.msA:1.76-1.100#urn:cite2:hmt:vaimg.v1:VA013VN_0515#urn:cite2:hmt:vaimg.v1:VA013VN_0515@0.4805,0.2239,0.3994,0.4763
msA#5#urn:cite2:hmt:msA.v1:3r#recto#Marcianus Graecus Z. 454 (= 822) (Venetus A) folio 3r#urn:cite2:hmt:codex:msA#urn:cts:greekLit:tlg0012.tlg001.msA:1.101-1.125#urn:cite2:hmt:vaimg.v1:VA014RN_0015#urn:cite2:hmt:vaimg.v1:VA014RN_0015@0.1802,0.2344,0.3844,0.4823
"""

    val newCollectionUrn = Cite2Urn("urn:cite2:hmt:msA.v1:")
    val newRepo = CiteCollectionRepository(newCex,"#",",")

    val objZero = newRepo.citableObject(Cite2Urn("urn:cite2:hmt:msA.v1:1r"))
    val objOne = newRepo.citableObject(Cite2Urn("urn:cite2:hmt:msA.v1:1v"))
    val objTwo = newRepo.citableObject(Cite2Urn("urn:cite2:hmt:msA.v1:2r"))
    val sortProp = Cite2Urn("urn:cite2:hmt:msA.v1.sequence:")
    assert( repo.sortValue(objZero,sortProp).toInt == 1 )
    assert( repo.sortValue(objOne,sortProp).toInt == 2 )
    assert( repo.sortValue(objTwo,sortProp).toInt == 3 )

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
