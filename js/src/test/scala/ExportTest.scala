package edu.holycross.shot.citeobj

import edu.holycross.shot.cite._
import org.scalatest._

class ExportTest extends FlatSpec {


  val cex = """#!citecollections
URN#Description#Labelling Ordering License
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

  val repo = CiteCollectionRepository(cex,"#",",")
  val citeObjects = repo.citableObjects
  val oneRecto = citeObjects(0)
  val oneVerso = citeObjects(1)

  val rvProperty = Cite2Urn("urn:cite2:hmt:msA.v1.rv:")
  val seqProperty = Cite2Urn("urn:cite2:hmt:msA.v1.sequence:")
  val codexProperty = Cite2Urn("urn:cite2:hmt:msA.v1.codex:")


  "The citeobj library"  should "expose methods of CiteObject" in {
    val cex = """#!citecollections
URN#Description#Labelling Ordering License
urn:cite2:hmt:msA.v1:#Pages of the Venetus A manuscriptscript#urn:cite2:hmt:msA.v1.label:#urn:cite2:hmt:msA.v1.sequence:#CC-attribution-share-alike

#!citeproperties
Property#Label#Type#Authority list
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

/*
  assert(oneRecto.valueEquals(rvProperty,"recto"))
  assert(oneRecto.valueEquals(rvProperty,"verso") == false)
  assert(oneRecto.valueEquals(seqProperty,1))
  */

  it should "be able to do boolean comparisons on numeric values" in {
    assert(oneRecto.numericGreaterThan(seqProperty,1) == false)
    assert(oneRecto.numericGreaterThan(seqProperty,1.5) == false)
    assert(oneRecto.valueEquals(seqProperty,1))
    assert(oneRecto.numericGreaterThan(seqProperty,0))

    assert(oneRecto.numericGreaterThan(seqProperty,0.9))
  }

}
