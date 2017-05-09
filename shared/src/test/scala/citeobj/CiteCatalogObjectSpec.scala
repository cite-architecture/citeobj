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

  it should "convert a column of strings to a property tuple" in {
    val cols = property2Line.split(",").toVector.drop(1)
    val propertyTuple = CiteCatalog.propertyTuple(cols)
    propertyTuple._1 match {
      case u: Cite2Urn => assert(true)
      case _ => fail(s"${propertyTuple._1} should have been a URN")
    }

    propertyTuple._2 match {
      case s: String => assert(true)
      case _ => fail(s"${propertyTuple._2} should have been a String")
    }

    propertyTuple._3 match {
      case pt: CitePropertyType => assert(true)
      case _ => fail(s"${propertyTuple._3} should have been a CitePropertyType")
    }

    propertyTuple._4 match {
      case v: Vector[String] => assert(true)
      case _ => fail(s"${propertyTuple._4} should have been a Vector of Strings")
    }

  }

  it should "create a CiteCatalog from a CEX citecatalog block" in pending

}