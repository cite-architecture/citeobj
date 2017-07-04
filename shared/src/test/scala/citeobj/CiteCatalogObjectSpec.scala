package edu.holycross.shot.citeobj
import org.scalatest.FlatSpec
import edu.holycross.shot.cite._

import edu.holycross.shot.cite._



class CiteCatalogObjectSpec extends FlatSpec {
  val p12catalog = """
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
// Text-bearing surfaces:
urn:cite2:hmt:msA.v1.urn:#URN#Cite2Urn#
urn:cite2:hmt:msA.v1.label:#Label#String#
urn:cite2:hmt:msA.v1.siglum:#Manuscript siglum#String#
urn:cite2:hmt:msA.v1.sequence:#Page sequence#Number#
urn:cite2:hmt:msA.v1.rv:#Recto or Verso#String#recto,verso
urn:cite2:hmt:msA.v1.codex:#Codex URN#Cite2Urn#

// Documentary images:
urn:cite2:hmt:vaimg.2017a.urn:#URN#Cite2Urn#
urn:cite2:hmt:vaimg.2017a.caption:#Caption#String#
urn:cite2:hmt:vaimg.2017a.rights:#Rights#String#


#!citedata
// Data block for the collection of text-bearing surfaces.
siglum#sequence#urn#rv#label#codex
msA#1#urn:cite2:hmt:msA.v1:12r#recto#Marcianus Graecus Z. 454 (= 822) (Venetus A) folio 12 recto#urn:cite2:hmt:codex:msA

#!citedata
// Data block for the collection of documentary images.
urn#caption#rights
urn:cite2:hmt:vaimg.2017a:VA012RN_0013.2017#Natural light photograph of Venetus A: Marcianus Graecus Z. 454 (= 822), folio 12, recto#This image was derived from an original ©2007, Biblioteca Nazionale Marciana, Venezie, Italia. The derivative image is ©2010, Center for Hellenic Studies. Original and derivative are licensed under the Creative Commons Attribution-Noncommercial-Share Alike 3.0 License. The CHS/Marciana Imaging Project was directed by David Jacobs of the British Library.

"""

    val cexSrc = """#!citecollections
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


  val collectionLine = "urn:cite2:hmt:msA.v1:,Folios of the Venetus A manuscript,urn:cite2:hmt:msA.v1.label:,urn:cite2:hmt:msA.v1.sequence:,Public domain"

  val property1Line = "urn:cite2:hmt:msA.v1.rv:,Recto or verso,String,recto#verso"

  val property2Line = "urn:cite2:hmt:msA.v1.sequence:,Page sequence,Number,"

  "The CiteCatalogObject" should "convert a column of strings to a collection tuple" in {
    val cols = collectionLine.split(",").toVector
    val collectionTuple = CiteCatalog.collectionTuple(cols)

    collectionTuple._1 match {
      case u: Cite2Urn => assert(true)
      case _ => fail(s"${collectionTuple._1} should have been a URN")
    }

    collectionTuple._2 match {
      case s: String => assert(true)
      case _ => fail(s"${collectionTuple._2} should have been a String")
    }

    collectionTuple._3 match {
      case l: Option[Cite2Urn] => assert(true)
      case _ => fail(s"${collectionTuple._3} should have been an Option[Cite2Urn]")
    }

    collectionTuple._4 match {
      case l: Option[Cite2Urn] => assert(true)
      case _ => fail(s"${collectionTuple._4} should have been an Option[Cite2Urn]")
    }

    collectionTuple._5 match {
      case s: String => assert(true)
      case _ => fail(s"${collectionTuple._5} should have been a String")
    }
  }



  it should "convert a column of strings to a PropertyDefinition" in {
    val cols = property2Line.split(",").toVector
    val propertyDefinition = CiteCatalog.propertyDefinition(cols,",")
    assert (propertyDefinition.urn == Cite2Urn("urn:cite2:hmt:msA.v1.sequence:"))
    assert(propertyDefinition.label == "Page sequence")
    assert(propertyDefinition.propertyType == NumericType)
    assert(propertyDefinition.vocabularyList.isEmpty)
  }


  it should "correctly handle controlled vocabulary lists when converting a column of strings to a PropertyDefinition" in {
    val cols = property1Line.split(",").toVector
    val propertyDefinition = CiteCatalog.propertyDefinition(cols,listDelimiter = "#")
    assert (propertyDefinition.urn == Cite2Urn("urn:cite2:hmt:msA.v1.rv:"))
    assert(propertyDefinition.label == "Recto or verso")
    assert(propertyDefinition.propertyType == ControlledVocabType)
    assert(propertyDefinition.vocabularyList.size == 2)
  }


  it should "create a CiteCatalog from a CEX citecollections and citeproperties blocks" in {
    val cex = "#!citecollections\n" + collectionLine + "\n#!citeproperties\n" + property1Line + "\n"+ property2Line + "\n"



    val cat = CiteCatalog(cex, columnDelimiter = ",", listDelimiter = "#")
    assert (cat.size == 1)
    val coll = cat.collections(0)
    assert (coll.propertyDefs.size == 2)

  }


  it should "create property defintions from strings" in {
    val vector1 = Vector("urn:cite2:hmt:msA.v1.urn:#URN#Cite2Urn#",
      "urn:cite2:hmt:msA.v1.label:#Label#String#",
      "urn:cite2:hmt:msA.v1.siglum:#Manuscript siglum#String#",
      "urn:cite2:hmt:msA.v1.sequence:#Page sequence#Number#",
      "urn:cite2:hmt:msA.v1.rv:#Recto or Verso#String#recto,verso",
      "urn:cite2:hmt:msA.v1.codex:#Codex URN#Cite2Urn#")
    val vectorOfVectors = vector1.map(_.split("#").toVector)
    val propDefsVector = CiteCatalog.propDefsFromVofV(vectorOfVectors,",")
    assert(propDefsVector.size == 6)
    val rv = propDefsVector(4)
    val expected = Vector("recto", "verso")
    assert(rv.vocabularyList == expected)

  }


  it should "create a CiteCatalog from any valid CEX source" in {
    val cat = CiteCatalog(cexSrc, "#", ",")
    assert(cat.size == 1)
    val coll1 = cat.collections(0)
    assert(coll1.urn == Cite2Urn("urn:cite2:hmt:msA.v1:"))
  }


  it should "recognize CtsUrn types in cex source" in {

    val cexWithCts =  """#!citecollections
urn:cite2:hmt:persName.v1:#People in the Iliad#urn:cite2:hmt:persName.v1.label:##CC-attribution-share-alike


#!citeproperties
urn:cite2:hmt:persName.v1.urn:#URN#Cite2Urn#
urn:cite2:hmt:persName.v1.label:#Label#String#
urn:cite2:hmt:persName.v1.psg:#Illustrative passage#CtsUrn#
"""
    val cat = CiteCatalog(cexWithCts, "#", ",")
    assert (cat.size == 1)
    val props = cat.collections(0).propertyDefs
    val psg = props.filter(_.urn == Cite2Urn("urn:cite2:hmt:persName.v1.psg:"))
    assert(psg(0).propertyType == CtsUrnType)

  }


  it should "work with p12 demo CEX" in {
    val cat = CiteCatalog(p12catalog, "#", ",")
    cat match {
      case cc: CiteCatalog => assert(true)
      case _ => fail("Should have created a CiteCatalog")
    }
  }

  it should "correctly handle ordered collections" in {
    val cat = CiteCatalog(p12catalog, "#", ",")
    val orderedColl = Cite2Urn("urn:cite2:hmt:msA.v1:")
    val unorderedColl = Cite2Urn("urn:cite2:hmt:vaimg.2017a:")
    assert (cat.isOrdered(orderedColl))
    assert(cat.isOrdered(unorderedColl) == false)
  }
  it should "correctly parse delimited text when options are represented by null strings" in {
    val cat = CiteCatalog(p12catalog, "#", ",")
    val imgColl = Cite2Urn("urn:cite2:hmt:vaimg.2017a:")
    def collDef = cat.collection(imgColl)
    assert(collDef.get.license == "CC-attribution-share-alike")
  }

}
