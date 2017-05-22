package edu.holycross.shot.citeobj
import org.scalatest.FlatSpec
import edu.holycross.shot.cite._


/**
*/
class CiteCollectionRepositoryMatchingSpec extends FlatSpec {


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
msA#4#urn:cite2:hmt:msA.v1:2r#recto#Marcianus Graecus Z. 454 (= 822) (Venetus A) folio 2 recto#urn:cite2:hmt:codex:msA
msA#3#urn:cite2:hmt:msA.v1:2v#verso#Marcianus Graecus Z. 454 (= 822) (Venetus A) folio 2 verso#urn:cite2:hmt:codex:msA
msA#1#urn:cite2:hmt:msA.v1:1r#recto#Marcianus Graecus Z. 454 (= 822) (Venetus A) folio 1r#urn:cite2:hmt:codex:msA
msA#2#urn:cite2:hmt:msA.v1:1v#verso#Marcianus Graecus Z. 454 (= 822) (Venetus A) folio 1v#urn:cite2:hmt:codex:msA
msA#5#urn:cite2:hmt:msA.v1:3r#recto#Marcianus Graecus Z. 454 (= 822) (Venetus A) folio 3 recto#urn:cite2:hmt:codex:msA
msA#6#urn:cite2:hmt:msA.v1:3v#verso#Marcianus Graecus Z. 454 (= 822) (Venetus A) folio 3 verso#urn:cite2:hmt:codex:msA
"""
  val repo = CiteCollectionRepository(cex,"#",",")


  "A Cite Collection repository" should "match an object-level URN on a single object" in {
    val objectUrn = Cite2Urn("urn:cite2:hmt:msA:1r")
    val selection = repo ~~ objectUrn
    assert(selection.size == 1)
  }

  it should "match a version-level URN on a single object" in {
    val objectUrn = Cite2Urn("urn:cite2:hmt:msA.v1:1r")
    val selection = repo ~~ objectUrn
    assert(selection.size == 1)
  }

  it should "match a property-level URN on a single object" in {
    val objectUrn = Cite2Urn("urn:cite2:hmt:msA.v1.siglum:1r")
    val selection = repo ~~ objectUrn
    assert(selection.size == 1)
  }

  it should "match an object-level URN on a range" in pending
  it should "match a version-level URN on a range" in pending
  it should "match a property-level URN on a range" in pending

  it should "throw an exception if matching a range URN on an unordered collection" in pending




}
