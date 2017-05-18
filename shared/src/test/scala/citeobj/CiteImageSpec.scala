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
# collection#protocol#base URL#rights property

urn:cite2:hmt:vaimg.v1:#CITE image#http://www.homermultitext.org/hmtdigital/images?#urn:cite2:hmt:msA.v1.rights:
urn:cite2:hmt:vaimg.v1:#IIIF#http://www.homermultitext.org/image2/context.json#urn:cite2:hmt:msA.v1.rights:
urn:cite2:hmt:vaimg.v1:#local file#file://./images#urn:cite2:hmt:msA.v1.rights:
"""


  "A CiteImageAjax object" should "create strings for CITE Image Service request usable either in Ajax calls or from the command line" in {

    val serviceUrlString = "http://www.homermultitext.org/hmt-digital/images?"
    val citeImages = CiteImageAjax(serviceUrlString)

    val expectedString = "http://www.homermultitext.org/hmt-digital/images?request=GetBinaryImage&urn=urn:cite2:hmt:vaimg:VA012RN_0013"

    assert(citeImages.protocol == "AJAX request for CITE Image service")
    assert(citeImages.binaryImageSource(img) == expectedString)
  }

  
}
