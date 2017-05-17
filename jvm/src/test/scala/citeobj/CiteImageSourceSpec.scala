package edu.holycross.shot.citeobj
import org.scalatest.FlatSpec
import edu.holycross.shot.cite._
import java.io.File


/**
*/
class CiteImageSourceSpec extends FlatSpec {

  val img = Cite2Urn("urn:cite2:hmt:vaimg:VA012RN_0013")

  "A LocalJpeg" should "create a file object" in {
    val directoryRoot = "./"
    val expectedFile = new File("./vaimg/VA012RN_0013.jpg")
    val imageFiles = LocalJpeg(directoryRoot)
    assert(imageFiles.protocol == "Local file")
    assert(imageFiles.binaryImageSource(img) == expectedFile)
  }


}
