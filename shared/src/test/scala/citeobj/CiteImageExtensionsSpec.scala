package edu.holycross.shot.citeobj
import org.scalatest.FlatSpec
import edu.holycross.shot.cite._

import java.io.File
/**
*/
class CiteImageExtensionsSpec extends FlatSpec {

  val img = Cite2Urn("urn:cite2:hmt:vaimg.v1:VA012RN_0013")
  val imgOther = Cite2Urn("urn:cite2:hmt:otherimg.v1:someImage")
  val collection = Cite2Urn("urn:cite2:hmt:vaimg.v1:")

  val cexSrc = """

#!imagedata

// Lines are structured as:
// collection#protocol#image source initializier#rights property

urn:cite2:hmt:vaimg.v1:#localJpegString#./image_archive/#urn:cite2:hmt:msA.v1.rights:
urn:cite2:hmt:vaimg.v1:#localDzString#./image_archive/#urn:cite2:hmt:msA.v1.rights:
urn:cite2:hmt:vaimg.v1:#iipImageDzString#http://www.homermultitext.org/iipsrv?DeepZoom=/project/homer/pyramidal/deepzoom/#urn:cite2:hmt:msA.v1.rights:
urn:cite2:hmt:vaimg.v1:#iipImageJpegString#http://www.homermultitext.org/iipsrv?OBJ=IIP,1.0&FIF=/project/homer/pyramidal/deepzoom/#urn:cite2:hmt:msA.v1.rights:
urn:cite2:hmt:otherimg.v1:#localJpegString#./other_archive#urn:cite2:hmt:msA.v1.rights:
"""
// [Log] http://www.homermultitext.org/iipsrv?DeepZoom=/project/homer/pyramidal/deepzoom/hmt/vaimg/2017a/VA012RN_0013.tif.dzi (ict2.js, line 132)

  "The ImageExtensions object" should "construct ImageExtensions from a CEX source" in {
    val imgExtensions = ImageExtensions(cexSrc).get
    imgExtensions match {
      case ie: ImageExtensions => assert(true)
      case _ => fail("Should have constructed image extensions mapping")
    }
  }


  it should "support multiple extensions for a single collection, and be able to generate sources from each" in {
    val imgExtensions = ImageExtensions(cexSrc).get
    assert(imgExtensions.protocolMap.size == 2)
    val mappedExample = imgExtensions.protocolMap.toSeq(0)
    assert(mappedExample._1 == Cite2Urn("urn:cite2:hmt:vaimg.v1:"))
    val sourceVector = mappedExample._2

    // should accept 2 of the 4:
    assert(sourceVector.size == 4)

    val expected1 = "http://www.homermultitext.org/iipsrv?DeepZoom=/project/homer/pyramidal/deepzoom/hmt/vaimg/v1/VA012RN_0013.tif.dzi"
    val expected2 = "./image_archive/hmt/vaimg/v1/VA012RN_0013.jpg"
    val expected3 = "./image_archive/hmt/vaimg/v1/VA012RN_0013.dzi"
		val expected4 = "http://www.homermultitext.org/iipsrv?OBJ=IIP,1.0&FIF=/project/homer/pyramidal/deepzoom/hmt/vaimg/v1/VA012RN_0013.tif&CVT=JPEG"

    val expected = Set(expected1, expected2, expected3, expected4)
    val answersVector = sourceVector.map(_.binaryImageSource(img))
    assert(answersVector.toSet == expected)
  }


  it should "find configured extensions by collection" in {
    val imgExtensions = ImageExtensions(cexSrc).get
    val extensions = imgExtensions.extensions(collection)
    assert(extensions.size == 4)
  }

  it should "return an empty Vector if no extensions are found for a collection" in {
    val imgExtensions = ImageExtensions(cexSrc).get
    assert(imgExtensions.protocolMap.size == 2)
    val missing = Cite2Urn("urn:cite2:hmt:noimages.v1:")
    assert(imgExtensions.extensions(missing) == Vector.empty)
  }
}
