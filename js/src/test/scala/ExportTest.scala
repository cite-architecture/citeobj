package edu.holycross.shot.citeobj

import edu.holycross.shot.cite._
import org.scalatest._

class ExportTest extends FlatSpec {

  "The citeobj library"  should "expose methods of CiteObject" in {
    val cex = """#!citecollections
urn:cite2:hmt:msA.v1:#Pages of the Venetus A manuscriptscript#urn:cite2:hmt:msA.v1.label:#urn:cite2:hmt:msA.v1.sequence:#CC-attribution-share-alike

#!citeproperties
urn:cite2:hmt:msA.v1.urn:#URN#Cite2Urn#
urn:cite2:hmt:msA.v1.label:#Label#String#
urn:cite2:hmt:msA.v1.siglum:#Manuscript siglum#String#
urn:cite2:hmt:msA.v1.sequence:#Page sequence#Number#
urn:cite2:hmt:msA.v1.rv:#Recto or Verso#String#recto,verso
urn:cite2:hmt:msA.v1.codex:#Codex URN#Cite2Urn#

#!citedata
siglum#sequence#urn#rv#label#codex
msA#1#urn:cite2:hmt:msA.v1:1r#recto#Marcianus Graecus Z. 454 (= 822) (Venetus A) folio 1r#urn:cite2:hmt:codex:msA
msA#2#urn:cite2:hmt:msA.v1:1v#verso#Marcianus Graecus Z. 454 (= 822) (Venetus A) folio 1v#urn:cite2:hmt:codex:msA
msA#3#urn:cite2:hmt:msA.v1:2r#recto#Marcianus Graecus Z. 454 (= 822) (Venetus A) folio 2r#urn:cite2:hmt:codex:msA
"""

    val repo = CiteCollectionRepository(cex,"#",",")
    val objectUrn = Cite2Urn("urn:cite2:hmt:msA.v1:1v")
    val labelPropertyUrn = Cite2Urn("urn:cite2:hmt:msA.v1.label:")
    val citableObj = repo.citableObject(objectUrn,labelPropertyUrn)

    citableObj match {
      case co : CiteObject => assert(true)
      case _ => fail("Should have created a CiteObject")
    }
  }

}
