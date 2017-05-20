package edu.holycross.shot.citeobj
import org.scalatest.FlatSpec
import edu.holycross.shot.cite._

import java.io.File
/**
*/
class CiteImageSpec extends FlatSpec {



  val img = Cite2Urn("urn:cite2:hmt:vaimg:VA012RN_0013")

  val cexSrc = """

#!imagedata

# Lines are structured as:
# collection#protocol#image source initializier#rights property

urn:cite2:hmt:vaimg.v1:#CITE image URL#http://www.homermultitext.org/hmtdigital/images?#urn:cite2:hmt:msA.v1.rights:
urn:cite2:hmt:vaimg.v1:#CITE image string#http://www.homermultitext.org/hmtdigital/images?#urn:cite2:hmt:msA.v1.rights:
urn:cite2:hmt:vaimg.v1:#local jpeg#file://./images#urn:cite2:hmt:msA.v1.rights:
urn:cite2:hmt:vaimg.v1:#local file string#./#urn:cite2:hmt:msA.v1.rights:
"""


  "A CiteImageAjax object" should "create strings for CITE Image Service request usable either in Ajax calls or from the command line" in {

    val serviceUrlString = "http://www.homermultitext.org/hmt-digital/images?"
    val citeImages = CiteImageAjax(serviceUrlString)

    val expectedString = "http://www.homermultitext.org/hmt-digital/images?request=GetBinaryImage&urn=urn:cite2:hmt:vaimg:VA012RN_0013"

    assert(citeImages.protocol == "AJAX request for CITE Image service")
    assert(citeImages.binaryImageSource(img) == expectedString)
  }

  "A LocalJpegString object" should "create strings for paths to files in a local system usable in any environment" in {
    val directoryRoot = "./"
    val expectedPath = "./vaimg/VA012RN_0013.jpg"
    val jpegSource = LocalJpegString(directoryRoot)
    assert(jpegSource.protocol == "Local jpeg string")
    assert(jpegSource.binaryImageSource(img) == expectedPath)
  }


}
