package edu.holycross.shot.citeobj
import org.scalatest.FlatSpec
import edu.holycross.shot.cite._


/**
*/
class CiteObjectSpec extends FlatSpec {


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

  "A CITE object" should "be constructed by a repository" in {
    val objectUrn = Cite2Urn("urn:cite2:hmt:msA.v1:1v")
    val labelPropertyUrn = Cite2Urn("urn:cite2:hmt:msA.v1.label:")
    val citableObj = repo.citableObject(objectUrn,labelPropertyUrn)

    citableObj match {
      case co : CiteObject => assert(true)
      case _ => fail("Should have created a CiteObject")
    }


  }

  it should "require an object selector on its URN" in {
    val collUrn = Cite2Urn("urn:cite2:hmt:msA.v1:")
    try {
      val citableObj = repo.citableObject(collUrn)
      fail("Should not have been able to make CiteObject from collection URN.")
    } catch {
      case iae: IllegalArgumentException => assert(iae.getMessage() == "requirement failed: For urn:cite2:hmt:msA.v1.urn:, found 3 matches")
      case t: Throwable => fail("Should have thrown IllegalArgumentException: " + t)
    }

  }
  it should "require a version-level identifier in its collection component" in {
      val noVersion = Cite2Urn("urn:cite2:hmt:msA:1r")
      try {
        val citableObj = repo.citableObject(noVersion)
        fail("Should not have been able to make CiteObject from URN without version.")
      } catch {
        case coe : CiteException => assert(coe.message == "No version defined in urn:cite2:hmt:msA:1r")

        case t: Throwable => fail("Should have thrown CiteObjectException: in fact threw " + t)
      }
  }
  it should "require a non-empty labelling string" in {
    val objectUrn = Cite2Urn("urn:cite2:hmt:msA.v1:1v")
    val citableObj = repo.citableObject(objectUrn)
    assert(citableObj.label.size > 0)
    assert(citableObj.label == "Marcianus Graecus Z. 454 (= 822) (Venetus A) folio 1v")

  }


  it should "find the value of a given property" in {

    val objectUrn = Cite2Urn("urn:cite2:hmt:msA.v1:1v")
    val propertyUrn = Cite2Urn("urn:cite2:hmt:msA.v1.rv:1v")
    val citableObj = repo.citableObject(objectUrn)

    assert(citableObj.propertyValue(propertyUrn) == "verso")
  }

}
