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
"""


  "A CiteImageAjax object" should "create strings for CITE Image Service request usable either in Ajax calls or from the command line" in {

    val serviceUrlString = "http://www.homermultitext.org/hmt-digital/images?"
    val citeImages = CiteImageAjax(serviceUrlString)

    val expectedString = "http://www.homermultitext.org/hmt-digital/images?request=GetBinaryImage&urn=urn:cite2:hmt:vaimg:VA012RN_0013"

    assert(citeImages.protocol == "AJAX request for CITE Image service")
    assert(citeImages.binaryImageSource(img) == expectedString)
  }

  "The ImageExtensions object" should "construct ImageExtensions from a CEX source" in {
    val imgExtensions = ImageExtensions(cexSrc).get
    assert(imgExtensions.protocolMap.size == 1)
    val mappedExample = imgExtensions.protocolMap.toSeq(0)
    assert(mappedExample._1 == Cite2Urn("urn:cite2:hmt:vaimg.v1:"))
    val binarySource = mappedExample._2
    val expected = "http://www.homermultitext.org/hmtdigital/images?request=GetBinaryImage&urn=urn:cite2:hmt:vaimg:VA012RN_0013"
    assert(binarySource.binaryImageSource(img) == expected)

  }
}
