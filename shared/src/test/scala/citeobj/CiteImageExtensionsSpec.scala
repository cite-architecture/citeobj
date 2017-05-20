package edu.holycross.shot.citeobj
import org.scalatest.FlatSpec
import edu.holycross.shot.cite._

import java.io.File
/**
*/
class CiteImageExtensionsSpec extends FlatSpec {

  val img = Cite2Urn("urn:cite2:hmt:vaimg:VA012RN_0013")


  val cexSrc = """

#!imagedata

# Lines are structured as:
# collection#protocol#image source initializier#rights property

urn:cite2:hmt:vaimg.v1:#CITE image string#http://www.homermultitext.org/hmtdigital/images?#urn:cite2:hmt:msA.v1.rights:
urn:cite2:hmt:vaimg.v1:#CITE image URL#http://www.homermultitext.org/hmtdigital/images?#urn:cite2:hmt:msA.v1.rights:
urn:cite2:hmt:vaimg.v1:#local jpeg string#./images/#urn:cite2:hmt:msA.v1.rights:
urn:cite2:hmt:vaimg.v1:#local jpeg file#./images#urn:cite2:hmt:msA.v1.rights:
"""


  "The ImageExtensions object" should "construct ImageExtensions from a CEX source" in {
    val imgExtensions = ImageExtensions(cexSrc).get
    assert(imgExtensions.protocolMap.size == 1)
    val mappedExample = imgExtensions.protocolMap.toSeq(0)
    assert(mappedExample._1 == Cite2Urn("urn:cite2:hmt:vaimg.v1:"))
    val sourceVector = mappedExample._2

    // should accept 2 of the 4:
    assert(sourceVector.size == 2)

    val expected1 = "http://www.homermultitext.org/hmtdigital/images?request=GetBinaryImage&urn=urn:cite2:hmt:vaimg:VA012RN_0013"
    val expected2 = "./images/vaimg/VA012RN_0013.jpg"

    val expected = Set(expected1, expected2)
    val answersVector = sourceVector.map(_.binaryImageSource(img))
    assert(answersVector.toSet == expected)
  }
}
