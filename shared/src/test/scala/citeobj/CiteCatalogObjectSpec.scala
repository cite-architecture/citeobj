package edu.holycross.shot.citeobj
import org.scalatest.FlatSpec
import edu.holycross.shot.cite._

import edu.holycross.shot.cite._

/**
*/
class CiteCatalogObjectSpec extends FlatSpec {

  val collectionLine = "collection,urn:cite2:hmt:msA.v1:,Folios of the Venetus A manuscript,urn:cite2:hmt:msA.v1.label:,urn:cite2:hmt:msA.v1.sequence:,Public domain"

  val property1Line = "property,urn:cite2:hmt:msA.v1.rv:,Recto or verso,String,recto#verso"

  val property2Line = "property,urn:cite2:hmt:msA.v1.sequence:,Page sequence,Number,"

  "The CiteCatalogObject" should "convert a column of strings to a collection tuple" in {
    val cols = collectionLine.split(",").toVector.drop(1)
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

  it should "correctly parse delimited text when options are represented by null strings" in pending
  it should "throw an exception if URNs are not correctly  formatted" in pending

  it should "convert a column of strings to a PropertyDefinition" in {
    val cols = property2Line.split(",").toVector.drop(1)
    val propertyDefinition = CiteCatalog.propertyDefinition(cols)
    assert (propertyDefinition.urn == Cite2Urn("urn:cite2:hmt:msA.v1.sequence:"))
    assert(propertyDefinition.label == "Page sequence")
    assert(propertyDefinition.propertyType == NumericType)
    assert(propertyDefinition.vocabularyList.isEmpty)
  }

  it should "correctly handle controlled vocabulary lists when converting a column of strings to a PropertyDefinition" in {
    val cols = property1Line.split(",").toVector.drop(1)
    val propertyDefinition = CiteCatalog.propertyDefinition(cols,listDelimiter = "#")
    assert (propertyDefinition.urn == Cite2Urn("urn:cite2:hmt:msA.v1.rv:"))
    assert(propertyDefinition.label == "Recto or verso")
    assert(propertyDefinition.propertyType == ControlledVocabType)
    assert(propertyDefinition.vocabularyList.size == 2)
  }


  it should "create a CiteCatalog from a CEX citecatalog block" in {
    val cex = collectionLine + "\n" + property1Line + "\n"+ property2Line + "\n"
    //val cex = collectionLine  + "\n"+ property2Line + "\n"

    val cat = CiteCatalog(cex, columnDelimiter = ",", listDelimiter = "#")
    assert (cat.size == 1)
    val coll = cat.collections(0)
    assert (coll.propertyDefs.size == 2)

  }

}
