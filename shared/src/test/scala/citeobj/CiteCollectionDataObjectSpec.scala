package edu.holycross.shot.citeobj
import org.scalatest.FlatSpec
import edu.holycross.shot.cite._
import edu.holycross.shot.cex._

/**
*/
class CiteCollectionDataObjectSpec extends FlatSpec {


val srcData = """#!citecatalog
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
msA#4#urn:cite2:hmt:msA.v1:2v#verso#Marcianus Graecus Z. 454 (= 822) (Venetus A) folio 2v#urn:cite2:hmt:codex:msA
msA#5#urn:cite2:hmt:msA.v1:3r#recto#Marcianus Graecus Z. 454 (= 822) (Venetus A) folio 3r#urn:cite2:hmt:codex:msA
msA#6#urn:cite2:hmt:msA.v1:3v#verso#Marcianus Graecus Z. 454 (= 822) (Venetus A) folio 3v#urn:cite2:hmt:codex:msA
msA#7#urn:cite2:hmt:msA.v1:4r#recto#Marcianus Graecus Z. 454 (= 822) (Venetus A) folio 4r#urn:cite2:hmt:codex:msA
msA#8#urn:cite2:hmt:msA.v1:4v#verso#Marcianus Graecus Z. 454 (= 822) (Venetus A) folio 4v#urn:cite2:hmt:codex:msA
msA#9#urn:cite2:hmt:msA.v1:5r#recto#Marcianus Graecus Z. 454 (= 822) (Venetus A) folio 5r#urn:cite2:hmt:codex:msA
"""


  "The CiteCollectionDataObject" should "construct CiteCollectionData from a delimited text string" in {
    val collData = CiteCollectionData(srcData, delimiter = "#")
    collData match {
      case cd: CiteCollectionData =>    {
        // 9 records, six properties
        assert(cd.size == 9*6)
        assert(cd.collections ==  Set(Cite2Urn("urn:cite2:hmt:msA.v1:")))
      }
      case _ => fail("Should have created CiteCollectionData")
    }
  }

  it should "be able to determine the collection URN for data in a CEX citedata block" in {
    val dataBlock = """Siglum,Sequence,urn,RV,label,CodexURN
msA,1,urn:cite2:hmt:msA.v1:1r,recto,Marcianus Graecus Z. 454 (= 822) (Venetus A) folio 1r,urn:cite2:hmt:codex:msA
msA,2,urn:cite2:hmt:msA.v1:1v,verso,Marcianus Graecus Z. 454 (= 822) (Venetus A) folio 1v,urn:cite2:hmt:codex:msA"
"""
    assert (CiteCollectionData.collectionForDataBlock(dataBlock,delimiter = ",") == Cite2Urn("urn:cite2:hmt:msA.v1:"))
  }

  it should "recognized case-insensitive match on required URN property " in pending
}
