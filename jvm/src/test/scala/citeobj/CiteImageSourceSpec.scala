package edu.holycross.shot.citeobj
import org.scalatest.FlatSpec
import edu.holycross.shot.cite._
import java.io.File
import java.net.URL


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

  "A CiteRESTImage" should "create a URL object" in {
    val serviceUrl = new URL("http://www.homermultitext.org/hmt-digital/images?")
    val expectedUrl = new URL("http://www.homermultitext.org/hmt-digital/images?request=GetBinaryImage&urn=urn:cite2:hmt:vaimg:VA012RN_0013")
    val citeImages = CiteRESTImage(serviceUrl)

    assert(citeImages.protocol == "CITE Image service")
    assert(citeImages.binaryImageSource(img) == expectedUrl)
  }

  
}
