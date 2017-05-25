package edu.holycross.shot.citeobj
import org.scalatest.FlatSpec
import edu.holycross.shot.cite._



/**
*/
class CiteRepositorySourceSpec extends FlatSpec {


  "A CiteRepositorySource" should "created a CiteCollectionRepository from a file in CEx format" in pending /*{
    val  f = "jvm/src/test/resources/p12r.cex"
    val repo = CiteRepositorySource.fromFile(f)
    repo match {
      case ccr: CiteCollectionRepository => assert(true)
      case _ => fail("Should have made CiteCollectionRepository")
    }*/


/* 

  } {
    val cat = CiteCatalogSource.fromFile("jvm/src/test/resources/collections.xml")
    val collDef = cat.collection(Cite2Urn("urn:cite2:hmt:msA.v1:")).get
    val fName = "jvm/src/test/resources/venetusA-v1.tsv"
    val data = CiteDataSource.fromFile(fName, collDef)
    println("VALIDATE DATA FROM CSV HERE ")

  }*/
  it should "accept explicit naming of pound signs as delimiters" in pending
  it should "optionally accept tabs as delimiters" in pending
  it should "optionally accept commas as delimiters" in pending

  /*{
    val fName = "jvm/src/test/resources/venetusAsample.csv"
    val data = CiteDataSource.fromFile(fName)
    println(data)
  }*/



}
