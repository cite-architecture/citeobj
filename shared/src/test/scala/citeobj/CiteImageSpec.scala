package edu.holycross.shot.citeobj
import org.scalatest.FlatSpec
import edu.holycross.shot.cite._

import java.io.File
/**
*/
class CiteImageSpec extends FlatSpec {

  "The BinaryImageSource trait" should "be implemented for local files" in {
    var f = new File("dummyimagefile.jpg")
    val fileImage = LocalImageSource(f)
    fileImage match {
      case lis: LocalImageSource => assert(true)
      case _ => fail("Should have created a local image source")
    }
  }
}
