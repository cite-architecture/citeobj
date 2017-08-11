package edu.holycross.shot.citeobj
import org.scalatest.FlatSpec
import edu.holycross.shot.cite._

import java.io.File
/**
*/
class IipImageJpegStringSpec extends FlatSpec {



  val cexSrc = """#!imagedata
urn:cite2:hmt:vaimg.2017a:#iipImageJpegString#http://www.homermultitext.org/iipsrv?OBJ=IIP,1.0&FIF=/project/homer/pyramidal/deepzoom/#urn:cite2:hmt:vaimg.2017a.rights:
"""

  "The ImageExtensions object" should "support multiple extensions for a single collection, and be able to generate sources from each" in {
    // Create image extensions from CEX:
    val imgExtensions = ImageExtensions(cexSrc).get

    // Verify that a specified collection is extended:
    val imgCollection = Cite2Urn("urn:cite2:hmt:vaimg.2017a:")
    assert(imgExtensions.isExtended(imgCollection))
    // Get all extensions for a collection:
    val extensions = imgExtensions.extensions(imgCollection)
    assert(extensions.size == 1)

    // Since only one extension for this collection, use it.
    // (NB: Also possible to select by protocol)
    val extension = extensions(0)
    assert(extension.protocol == "iipImageString")

    val imgWRoi = Cite2Urn("urn:cite2:hmt:vaimg.2017a:VA012RN_0013@0.3253,0.5305,0.1651,0.01706")
    val expected = "http://www.homermultitext.org/iipsrv?OBJ=IIP,1.0&FIF=/project/homer/pyramidal/deepzoom/hmt/vaimg/2017a/VA012RN_0013.tif&RGN=0.3253,0.5305,0.1651,0.01706&CVT=JPEG"
    assert (extension.binaryImageSource(imgWRoi) == expected)

    val fullImg = Cite2Urn("urn:cite2:hmt:vaimg.2017a:VA012RN_0013")
    val expectedFullImg = "http://www.homermultitext.org/iipsrv?OBJ=IIP,1.0&FIF=/project/homer/pyramidal/deepzoom/hmt/vaimg/2017a/VA012RN_0013.tif&CVT=JPEG"
    assert (extension.binaryImageSource(fullImg) == expectedFullImg)

  }

}
