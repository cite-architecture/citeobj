package edu.holycross.shot.citeobj
import org.scalatest.FlatSpec
import edu.holycross.shot.cite._
import edu.holycross.shot.cex._

/**
*/
class CiteCollectionDataObjectSpec extends FlatSpec {


val srcData = """#!citecollections
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


  it should "work with the p12 demo CEX set" in {
    val p12cex = """
#!citelibrary
name#Demo of DSE structure: Venetus A manuscript, folio 12 recto
urn#urn:cite2:dse:demo.2017a:va12r
license#public domain

#!citecollections

// Text-bearing surfaces:
urn:cite2:hmt:msA.v1:#Pages of the Venetus A manuscriptscript#urn:cite2:hmt:msA.v1.label:#urn:cite2:hmt:msA.v1.sequence:#CC-attribution-share-alike
// Documentary images:
urn:cite2:hmt:vaimg.2017a:#Images of the Venetus A manuscriptscript#urn:cite2:hmt:vaimg.2017a.caption:##CC-attribution-share-alike

#!citeproperties
urn:cite2:hmt:msA.v1.urn:#URN#Cite2Urn#
urn:cite2:hmt:msA.v1.label:#Label#String#
urn:cite2:hmt:msA.v1.siglum:#Manuscript siglum#String#
urn:cite2:hmt:msA.v1.sequence:#Page sequence#Number#
urn:cite2:hmt:msA.v1.rv:#Recto or Verso#String#recto,verso
urn:cite2:hmt:msA.v1.codex:#Codex URN#Cite2Urn#


urn:cite2:hmt:vaimg.2017a.urn:#URN#Cite2Urn#
urn:cite2:hmt:vaimg.2017a.caption:#Caption#String#
urn:cite2:hmt:vaimg.2017a.rights:#Rights#String#


#!citedata
//Data block for the collection of text-bearing surfaces.

siglum#sequence#urn#rv#label#codex
msA#1#urn:cite2:hmt:msA.v1:12r#recto#Marcianus Graecus Z. 454 (= 822) (Venetus A) folio 12 recto#urn:cite2:hmt:codex:msA

#!citedata
// Data block for the collection of documentary images.

urn#caption#rights
urn:cite2:hmt:vaimg.2017a:VA012RN_0013.2017#Natural light photograph of Venetus A: Marcianus Graecus Z. 454 (= 822), folio 12, recto#This image was derived from an original ©2007, Biblioteca Nazionale Marciana, Venezie, Italia. The derivative image is ©2010, Center for Hellenic Studies. Original and derivative are licensed under the Creative Commons Attribution-Noncommercial-Share Alike 3.0 License. The CHS/Marciana Imaging Project was directed by David Jacobs of the British Library.

"""
    val collData = CiteCollectionData(p12cex, delimiter = "#")
    collData match {
      case ccd: CiteCollectionData => assert(true)
      case _ => fail ("Should have created CiteCollectionData")
    }

  }


    it should "recognize case-insensitive match on required URN property " in {

val dataBlocks = """#!citelibrary
name#Demo of DSE structure: Venetus A manuscript, folio 12 recto
urn#urn:cite2:dse:demo.2017a:va12r
license#public domain

#!citecollections
URN#Description#Labelling property#Ordering property#License
// Text-bearing surfaces:
urn:cite2:hmt:msA.v1:#Pages of the Venetus A manuscriptscript#urn:cite2:hmt:msA.v1.label:#urn:cite2:hmt:msA.v1.sequence:#CC-attribution-share-alike
//Documentary images:
urn:cite2:hmt:vaimg.2017a:#Images of the Venetus A manuscriptscript#urn:cite2:hmt:vaimg.2017a.caption:##CC-attribution-share-alike


#!citeproperties
Property#Label#Type#Authority list
urn:cite2:hmt:msA.v1.urn:#URN#Cite2Urn#
urn:cite2:hmt:msA.v1.label:#Label#String#
urn:cite2:hmt:msA.v1.siglum:#Manuscript siglum#String#
urn:cite2:hmt:msA.v1.sequence:#Page sequence#Number#
urn:cite2:hmt:msA.v1.rv:#Recto or Verso#String#recto,verso
urn:cite2:hmt:msA.v1.codex:#Codex URN#Cite2Urn#


urn:cite2:hmt:vaimg.2017a.urn:#URN#Cite2Urn#
urn:cite2:hmt:vaimg.2017a.caption:#Caption#String#
urn:cite2:hmt:vaimg.2017a.rights:#Rights#String#
#!citedata
//Data block for the collection of text-bearing surfaces.
siglum#sequence#Urn#rv#label#codex
msA#1#urn:cite2:hmt:msA.v1:12r#recto#Marcianus Graecus Z. 454 (= 822) (Venetus A) folio 12 recto#urn:cite2:hmt:codex:msA
#!citedata
// Data block for the collection of documentary images.
Urn#caption#rights
urn:cite2:hmt:vaimg.2017a:VA012RN_0013.2017#Natural light photograph of Venetus A: Marcianus Graecus Z. 454 (= 822), folio 12, recto#This image was derived from an original ©2007, Biblioteca Nazionale Marciana, Venezie, Italia. The derivative image is ©2010, Center for Hellenic Studies. Original and derivative are licensed under the Creative Commons Attribution-Noncommercial-Share Alike 3.0 License. The CHS/Marciana Imaging Project was directed by David Jacobs of the British Library.
"""
      val collData = CiteCollectionData(dataBlocks, delimiter = "#")
      val expected = Set(Cite2Urn("urn:cite2:hmt:msA.v1:"), Cite2Urn("urn:cite2:hmt:vaimg.2017a:"))
      assert (collData.collections == expected)
    }

  it should "with when there is an empty column" in {
    val hmtData:String = """#!citecollections
URN#Description#Labelling property#Ordering property#License
urn:cite2:hmt:pers.v1:#Names of persons#urn:cite2:hmt:pers.v1.label:##Public domain
urn:cite2:hmt:place.v1:#Geographic names#urn:cite2:hmt:place.v1.label:##Public domain

#!citeproperties
Property#Label#Type#Authority list
urn:cite2:hmt:pers.v1.urn:#Person#Cite2Urn#
urn:cite2:hmt:pers.v1.label:#Label#String#
urn:cite2:hmt:pers.v1.description:#Description#String#
urn:cite2:hmt:pers.v1.status:#Status#String#proposed,accepted,rejected
urn:cite2:hmt:pers.v1.redirect:#Redirected to#Cite2Urn#

#!citedata
urn#Label#Description#Status#Redirect
urn:cite2:hmt:pers.v1:pers1#Achilles#hero of the Iliad, greatest warrior of the Greeks at Troy#proposed#
"""

    val collData = CiteCollectionData(hmtData, delimiter = "#")

  }






}
