package edu.holycross.shot.citeobj
import org.scalatest.FlatSpec
import edu.holycross.shot.cite._


/**
*/
class CiteCatalogSourceSpec extends FlatSpec {


  "A catalog reader" should "read a Cite catalog from a text file" in {
    val root = CiteCatalogSource.fromFile("jvm/src/test/resources/collections.xml")
    //val colls = root \\ "citeCollection"
    //colls
  }

}
