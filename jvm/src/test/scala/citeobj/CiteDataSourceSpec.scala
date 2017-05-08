package edu.holycross.shot.citeobj
import org.scalatest.FlatSpec
import edu.holycross.shot.cite._



/**
*/
class CiteDataSourceSpec extends FlatSpec {


  "A data reader" should "read cataloged delimited data from a text file" in  {
    val cat = CiteCatalogSource.fromFile("jvm/src/test/resources/collections.xml")
    val collDef = cat.collection(Cite2Urn("urn:cite2:hmt:msA.v1:")).get
    val fName = "jvm/src/test/resources/venetusA-v1.tsv"
    val data = CiteDataSource.fromFile(fName, collDef)
    println("DATA FROM CSV: " + data)

  }
  it should "accept explicit naming of pound signs as delimiters" in pending
  it should "optionally accept tabs as delimiters" in pending
  it should "optionally accept commas as delimiters" in pending /*{
    val fName = "jvm/src/test/resources/venetusAsample.csv"
    val data = CiteDataSource.fromFile(fName)
    println(data)
  }*/



}
