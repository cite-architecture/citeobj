package edu.holycross.shot.citeobj
import org.scalatest.FlatSpec
import edu.holycross.shot.cite._



/**
*/
class CiteRepositorySourceSpec extends FlatSpec {


  "A CiteRepositorySource" should "create a CiteCollectionRepository from a file in CEX format" in {
    val f = "jvm/src/test/resources/p12r.cex"
    val repo = CiteRepositorySource.fromFile(f)
    repo match {
      case ccr : CiteCollectionRepository => assert(true)
      case _ => fail("Should have created a CiteCollectionRepository")
    }

  }
/*
  it should "accept explicit naming of principal delimiter" in {
    val f = "jvm/src/test/resources/p12r.tsv"
    val repo = CiteRepositorySource.fromFile(f, "\t",",")
    repo match {
      case ccr : CiteCollectionRepository => assert(true)
      case _ => fail("Should have created a CiteCollectionRepository")
    }
  }*/
  





}
