package edu.holycross.shot.citeobj
import org.scalatest.FlatSpec
import edu.holycross.shot.cite._


/**
*/
class CiteCatalogSourceSpec extends FlatSpec {


  "A catalog reader" should "read a Cite catalog from a text file" in {
    val cat = CiteCatalogSource.fromFile("jvm/src/test/resources/collections.xml")
    cat match {
      case c: CiteCatalog => assert(true)
      case _ => fail("Failed to create CITE catalog")
    }
  }


}
