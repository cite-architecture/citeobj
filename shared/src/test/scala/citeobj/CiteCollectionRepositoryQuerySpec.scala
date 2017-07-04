package edu.holycross.shot.citeobj
import org.scalatest.FlatSpec
import edu.holycross.shot.cite._


/**
*/
class CiteCollectionRepositoryQuerySpec extends FlatSpec {


  val cex = """#!citecollections
urn:cite2:hmt:msA.v1:#Pages of the Venetus A manuscriptscript#urn:cite2:hmt:msA.v1.label:#urn:cite2:hmt:msA.v1.sequence:#CC-attribution-share-alike
urn:cite2:hmt:vaImg.v1.:#Images of the Venetus A manuscriptscript#urn:cite2:hmt:vaImg.v1.caption:##CC-attribution-share-alike


#!citeproperties
// surfaces
urn:cite2:hmt:msA.v1.urn:#URN#Cite2Urn#
urn:cite2:hmt:msA.v1.label:#Label#String#
urn:cite2:hmt:msA.v1.siglum:#Manuscript siglum#String#
urn:cite2:hmt:msA.v1.sequence:#Page sequence#Number#
urn:cite2:hmt:msA.v1.rv:#Recto or Verso#String#recto,verso
urn:cite2:hmt:msA.v1.codex:#Codex URN#Cite2Urn#
//images
urn:cite2:hmt:vaImg.v1.urn:#URN#Cite2Urn#
urn:cite2:hmt:vaImg.v1.caption:#Caption#String#

#!citedata
// surfaces
siglum#sequence#urn#rv#label#codex
msA#3#urn:cite2:hmt:msA.v1:2r#recto#Marcianus Graecus Z. 454 (= 822) (Venetus A) folio 2 recto#urn:cite2:hmt:codex:msA
msA#4#urn:cite2:hmt:msA.v1:2v#verso#Marcianus Graecus Z. 454 (= 822) (Venetus A) folio 2 verso#urn:cite2:hmt:codex:msA
msA#1#urn:cite2:hmt:msA.v1:1r#recto#Marcianus Graecus Z. 454 (= 822) (Venetus A) folio 1r#urn:cite2:hmt:codex:msA
msA#2#urn:cite2:hmt:msA.v1:1v#verso#Marcianus Graecus Z. 454 (= 822) (Venetus A) folio 1v#urn:cite2:hmt:codex:msA
msA#5#urn:cite2:hmt:msA.v1:3r#recto#Marcianus Graecus Z. 454 (= 822) (Venetus A) folio 3 recto#urn:cite2:hmt:codex:msA
msA#6#urn:cite2:hmt:msA.v1:3v#verso#Marcianus Graecus Z. 454 (= 822) (Venetus A) folio 3 verso#urn:cite2:hmt:codex:msA

#!citedata
//images
urn#caption
urn:cite2:hmt:vaImg.v1:imgA#Image of a page: overview
urn:cite2:hmt:vaImg.v1:imgB#Detailed image of a page
urn:cite2:hmt:vaImg.v1:imgC#Detailed image of a second page
"""



  val repo = CiteCollectionRepository(cex,"#",",")
  "A Cite Collection repository" should "find a string in records" in  {
    val details = repo.stringContains("Detailed", true)
    assert(details.size == 2)
  }

  it should "respect case in searching strings" in {
    val details = repo.stringContains("detailed", true)
    assert(details.isEmpty)
    val detailsInsensitive = repo.stringContains("detailed", false)
    assert(detailsInsensitive.size == 2)
  }

  it should "search properties in unqualified string search" in {
    val rectos = repo.stringContains("recto")
    assert(rectos.size == 3)
  }

  it should "find strings in specific property" in {
    val rvProperty = Cite2Urn("urn:cite2:hmt:msA.v1.rv:")
    val rectos  = repo.stringContains(rvProperty,"recto", true)
  }




}
