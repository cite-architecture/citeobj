package edu.holycross.shot.citeobj
import org.scalatest.FlatSpec
import edu.holycross.shot.cite._


/**
*/
class CiteCollectionRepositoryOrderingSpec extends FlatSpec {


  val cex = """#!citecatalog
collection#urn:cite2:hmt:msA.v1:#Pages of the Venetus A manuscriptscript#urn:cite2:hmt:msA.v1.label:#urn:cite2:hmt:msA.v1.sequence:#CC-attribution-share-alike

property#urn:cite2:hmt:msA.v1.urn:#URN#Cite2Urn#
property#urn:cite2:hmt:msA.v1.label:#Label#String#
property#urn:cite2:hmt:msA.v1.siglum:#Manuscript siglum#String#
property#urn:cite2:hmt:msA.v1.sequence:#Page sequence#Number#
property#urn:cite2:hmt:msA.v1.rv:#Recto or Verso#String#recto,verso
property#urn:cite2:hmt:msA.v1.codex:#Codex URN#Cite2Urn#

#!citedata
siglum#sequence#urn#rv#label#codex
msA#3#urn:cite2:hmt:msA.v1:2r#recto#Marcianus Graecus Z. 454 (= 822) (Venetus A) folio 2r#urn:cite2:hmt:codex:msA
msA#1#urn:cite2:hmt:msA.v1:1r#recto#Marcianus Graecus Z. 454 (= 822) (Venetus A) folio 1r#urn:cite2:hmt:codex:msA
msA#2#urn:cite2:hmt:msA.v1:1v#verso#Marcianus Graecus Z. 454 (= 822) (Venetus A) folio 1v#urn:cite2:hmt:codex:msA
"""
  val repo = CiteCollectionRepository(cex,"#",",")
  val collectionUrn = Cite2Urn("urn:cite2:hmt:msA.v1:")

  "A Cite Collection repository" should "recognize an ordered collection" in {
    assert(repo isOrdered Cite2Urn("urn:cite2:hmt:msA.v1:"))
  }
  it should "find the first CiteObject in an ordered collection" in {
    val expected = repo.citableObject(Cite2Urn("urn:cite2:hmt:msA.v1:1r"))
    assert(repo.first(collectionUrn) == expected)
  }

  it should "find the last CiteObject in an ordered collection" in {
    val expected = repo.citableObject(Cite2Urn("urn:cite2:hmt:msA.v1:2r"))
    assert(repo.last(collectionUrn) == expected)
  }


  it should "find the index of an object in an ordered vector" in {
    val obj = repo.citableObject(Cite2Urn("urn:cite2:hmt:msA.v1:2r"))
    assert(repo.indexOf(obj) == 2)
  }
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


}
