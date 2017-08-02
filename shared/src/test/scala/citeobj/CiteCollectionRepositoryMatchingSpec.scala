package edu.holycross.shot.citeobj
import org.scalatest.FlatSpec
import edu.holycross.shot.cite._


/**
*/
class CiteCollectionRepositoryMatchingSpec extends FlatSpec {


  val cex = """#!citecollections
URN#Description#Labelling property#Ordering property#License
urn:cite2:hmt:msA.v1:#Pages of the Venetus A manuscriptscript#urn:cite2:hmt:msA.v1.label:#urn:cite2:hmt:msA.v1.sequence:#CC-attribution-share-alike
urn:cite2:hmt:vaImg.v1:#Images of the Venetus A manuscriptscript#urn:cite2:hmt:vaImg.v1.caption:##CC-attribution-share-alike

#!citeproperties
Property#Label#Type#Authority list
urn:cite2:hmt:msA.v1.urn:#URN#Cite2Urn#
urn:cite2:hmt:msA.v1.label:#Label#String#
urn:cite2:hmt:msA.v1.siglum:#Manuscript siglum#String#
urn:cite2:hmt:msA.v1.sequence:#Page sequence#Number#
urn:cite2:hmt:msA.v1.rv:#Recto or Verso#String#recto,verso
urn:cite2:hmt:msA.v1.codex:#Codex URN#Cite2Urn#

urn:cite2:hmt:vaImg.v1.urn:#URN#Cite2Urn#
urn:cite2:hmt:vaImg.v1.caption:#Caption#String#

#!citedata
siglum#sequence#urn#rv#label#codex
msA#3#urn:cite2:hmt:msA.v1:2r#recto#Marcianus Graecus Z. 454 (= 822) (Venetus A) folio 2 recto#urn:cite2:hmt:codex:msA
msA#4#urn:cite2:hmt:msA.v1:2v#verso#Marcianus Graecus Z. 454 (= 822) (Venetus A) folio 2 verso#urn:cite2:hmt:codex:msA
msA#1#urn:cite2:hmt:msA.v1:1r#recto#Marcianus Graecus Z. 454 (= 822) (Venetus A) folio 1r#urn:cite2:hmt:codex:msA
msA#2#urn:cite2:hmt:msA.v1:1v#verso#Marcianus Graecus Z. 454 (= 822) (Venetus A) folio 1v#urn:cite2:hmt:codex:msA
msA#5#urn:cite2:hmt:msA.v1:3r#recto#Marcianus Graecus Z. 454 (= 822) (Venetus A) folio 3 recto#urn:cite2:hmt:codex:msA
msA#6#urn:cite2:hmt:msA.v1:3v#verso#Marcianus Graecus Z. 454 (= 822) (Venetus A) folio 3 verso#urn:cite2:hmt:codex:msA


#!citedata
urn#caption
urn:cite2:hmt:vaImg.v1:imgA#Image of a page: overview
urn:cite2:hmt:vaImg.v1:imgB#Detailed image of a page
urn:cite2:hmt:vaImg.v1:imgC#Detailed image of a second page

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

  it should "match a single-object URN with extension" in {
    val extendedUrn = Cite2Urn("urn:cite2:hmt:msA.v1:1v@folioExtension")
    val selection = repo ~~ extendedUrn
    assert(selection.size == 1)
    assert(selection(0).urn == Cite2Urn("urn:cite2:hmt:msA.v1:1v"))
  }

  it should "match a range URN with extensions" in {
    val extendedUrn = Cite2Urn("urn:cite2:hmt:msA.v1:1v@folioExtension-2v@folioExtension")
    val selection = repo ~~ extendedUrn
    assert(selection.size == 3)
  }

  it should "match a version-level URN on a range" in {
    val rangeUrn = Cite2Urn("urn:cite2:hmt:msA.v1:1v-3r")
    val selection = repo ~~ rangeUrn
    assert(selection.size == 4)


  }
  it should "match a property-level URN on a range" in {
    val rangeUrn = Cite2Urn("urn:cite2:hmt:msA.v1.siglum:1v-3r")
    val selection = repo ~~ rangeUrn
    assert(selection.size == 4)
  }

  it should "match a range URN with extension" in {
    val rangeUrn = Cite2Urn("urn:cite2:hmt:msA.v1.siglum:1v@folioExtension-3r")
    val selection = repo ~~ rangeUrn
    assert(selection.size == 4)
  }

  it should "throw an exception if matching a range URN on an unordered collection" in {
    val badRange = Cite2Urn("urn:cite2:hmt:vaImg.v1:imgA-imgC")
    try {
      val selection = repo ~~ badRange
      fail("Should not have been able to twiddle on bad range " + badRange)

    } catch {
      case coe: CiteObjectException => assert(coe.message == "Range expression not valid unless collection is ordered: urn:cite2:hmt:vaImg.v1:imgA-imgC")
      case t: Throwable => throw t
    }

  }




}
