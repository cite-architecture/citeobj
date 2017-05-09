package edu.holycross.shot.citeobj
import org.scalatest.FlatSpec
import edu.holycross.shot.cite._

import edu.holycross.shot.cite._

/**
*/
class CiteCatalogObjectSpec extends FlatSpec {

  val collectionLine = "collection,urn:cite2:hmt:msA.v1:,Folios of the Venetus A manuscript,urn:cite2:hmt:msA.v1.label:,urn:cite2:hmt:msA.v1.sequence:,Public domain"

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

}
