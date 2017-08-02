package edu.holycross.shot.citeobj
import org.scalatest.FlatSpec
import edu.holycross.shot.cite._


/**
*/
class CiteCollectionRepositoryCexSpec extends FlatSpec {

  val cex = """#!citecollections
URN#Description#Labelling property#Ordering property#License
urn:cite2:hmt:msA.v1:#Pages of the Venetus A manuscriptscript#urn:cite2:hmt:msA.v1.label:#urn:cite2:hmt:msA.v1.sequence:#CC-attribution-share-alike

#!citeproperties
Property#Label#Type#Authority list
urn:cite2:hmt:msA.v1.urn:#URN#Cite2Urn#
urn:cite2:hmt:msA.v1.label:#Label#String#
urn:cite2:hmt:msA.v1.siglum:#Manuscript siglum#String#
urn:cite2:hmt:msA.v1.sequence:#Page sequence#Number#
urn:cite2:hmt:msA.v1.rv:#Recto or Verso#String#recto,verso
urn:cite2:hmt:msA.v1.codex:#Codex URN#Cite2Urn#
urn:cite2:hmt:msA.v1.validated:#Passed Validation#Boolean#

#!citedata
siglum#sequence#urn#rv#label#codex#validated
msA#1#urn:cite2:hmt:msA.v1:1r#recto#Marcianus Graecus Z. 454 (= 822) (Venetus A) folio 1r#urn:cite2:hmt:codex:msA#true
msA#2#urn:cite2:hmt:msA.v1:1v#verso#Marcianus Graecus Z. 454 (= 822) (Venetus A) folio 1v#urn:cite2:hmt:codex:msA#true
msA#3#urn:cite2:hmt:msA.v1:2r#recto#Marcianus Graecus Z. 454 (= 822) (Venetus A) folio 2r#urn:cite2:hmt:codex:msA#false
"""

  "A Cite Collection repository" should "be able to construct a repository from CEX source" in {
    val repo = CiteCollectionRepository(cex,"#",",")
    repo match {
      case ccr: CiteCollectionRepository => assert(true)
      case _ => fail("Should have made CiteCollectionRepository")
    }

  }

  it should "ensure that all data are from cataloged collections" in pending


  it should "accept CEX defining a catalog but no data" in {
    val noData = """#!citelibrary
license#public domain
name#Demo library
urn#urn:cite2:cex:democex.2017a:test

// Library has two collections:
#!citecollections
URN#Description#Labelling property#Ordering property#License
// 1. Text-bearing surfaces:
urn:cite2:hmt:msA.v1:#Pages of the Venetus A manuscripts#urn:cite2:hmt:msA.v1.label:#urn:cite2:hmt:msA.v1.sequence:#CC-attribution-share-alike
// 2. Documentary images:
urn:cite2:hmt:vaimg.2017a:#Images of the Venetus A#urn:cite2:hmt:msA.v1.label:##CC-attribution-share-alike

#!citeproperties
Property#Label#Type#Authority list
// pages
urn:cite2:hmt:msA.v1.urn:#URN#Cite2Urn#
urn:cite2:hmt:msA.v1.label:#Label#String#
urn:cite2:hmt:msA.v1.siglum:#Manuscript siglum#String#
urn:cite2:hmt:msA.v1.sequence:#Page sequence#Number#
urn:cite2:hmt:msA.v1.rv:#Recto or Verso#String#recto,verso
urn:cite2:hmt:msA.v1.codex:#Codex URN#Cite2Urn#

// images
urn:cite2:hmt:vaimg.2017a.urn:#URN#Cite2Urn#
urn:cite2:hmt:vaimg.2017a.caption:#Caption#String#
urn:cite2:hmt:vaimg.2017a.rights:#Rights#String#
"""
    val repo = CiteCollectionRepository(noData,"#",",")
    assert(repo.catalog.size == 2)
  }
  it should "catch catalog entries with missing data " in {
    val defective = """
#!citecollections
URN#Description#Labelling property#Ordering property#License
// Missing a column...
urn:cite2:hmt:vaimg.2017a:#Images of the Venetus A#urn:cite2:hmt:msA.v1.label:#CC-attribution-share-alike
#!citeproperties
Property#Label#Type#Authority list
urn:cite2:hmt:vaimg.2017a.urn:#URN#Cite2Urn#
urn:cite2:hmt:vaimg.2017a.caption:#Caption#String#
urn:cite2:hmt:vaimg.2017a.rights:#Rights#String#
"""
  try {
    val repo = CiteCollectionRepository(defective,"#",",")

  } catch {
    case iae: IllegalArgumentException => assert(iae.getMessage() == "requirement failed: wrong number of components in  'CC-attribution-share-alike' (1)")
    case thr : Throwable => fail("Should have thrown IllegalArgumentException")
  }
  }

}
