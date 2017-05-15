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
msA#1#urn:cite2:hmt:msA.v1:1r#recto#Marcianus Graecus Z. 454 (= 822) (Venetus A) folio 1r#urn:cite2:hmt:codex:msA
msA#2#urn:cite2:hmt:msA.v1:1v#verso#Marcianus Graecus Z. 454 (= 822) (Venetus A) folio 1v#urn:cite2:hmt:codex:msA
msA#3#urn:cite2:hmt:msA.v1:2r#recto#Marcianus Graecus Z. 454 (= 822) (Venetus A) folio 2r#urn:cite2:hmt:codex:msA
"""
  val repo = CiteCollectionRepository(cex,"#",",")

  "A Cite Collection repository" should "recognize an ordered collection" in {
    assert(repo isOrdered Cite2Urn("urn:cite2:hmt:msA.v1:"))
  }
  it should "find the first CiteObject in an ordered collection" in pending
  it should "find the last CiteObject in an ordered collection" in pending
  it should "find the CiteObject following a given element in an ordered collection" in pending
  it should "find the CiteObject preceding a given element in an ordered collection" in pending
  it should "convert an ordered collection to ordered Vector of CiteObjects" in pending
  // and maybe all of those for properties?


  it should "create a citable object for an identifier" in {
    val u = Cite2Urn("urn:cite2:hmt:msA.v1:1v")
    val citableObj = repo.citableObject(u)
    assert(citableObj.urn == u)
  }

}
