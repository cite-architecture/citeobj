package edu.holycross.shot.citeobj
import org.scalatest.FlatSpec
import edu.holycross.shot.cite._


/**
*/
class CiteCollectionRepositoryCexSpec extends FlatSpec {

  val cex = """#!citecollections
urn:cite2:hmt:msA.v1:#Pages of the Venetus A manuscriptscript#urn:cite2:hmt:msA.v1.label:#urn:cite2:hmt:msA.v1.sequence:#CC-attribution-share-alike

#!citeproperties
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

}
