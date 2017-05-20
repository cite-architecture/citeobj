package edu.holycross.shot.citeobj
import org.scalatest.FlatSpec
import edu.holycross.shot.cite._

import java.io.File
/**
*/
class CiteImageExtensionsFilterSpec extends FlatSpec {

  val img = Cite2Urn("urn:cite2:hmt:vaimg:VA012RN_0013")
  val collection = Cite2Urn("urn:cite2:hmt:vaimg.v1:")


  "The ImageExtensions object" should  "filter the map by protocol" in {

    val shortCex = """
#!imagedata
urn:cite2:hmt:vaimg.v1:#CITE image string#http://www.homermultitext.org/hmtdigital/images?#urn:cite2:hmt:msA.v1.rights:
urn:cite2:hmt:vaimg.v1:#local jpeg string#./images/#urn:cite2:hmt:msA.v1.rights:
"""
    val imgExtensions = ImageExtensions(shortCex).get
    val jpegOnly = imgExtensions.forProtocol("local jpeg string")
    assert(jpegOnly.protocolMap.size == 1)
    val sourceVector = jpegOnly.extensions(collection)
    assert(sourceVector.size == 1)
    val binarySrc = sourceVector(0)
    assert (binarySrc.protocol.toLowerCase == "local jpeg string")
    assert (binarySrc.binaryImageSource(img) == "./images/vaimg/VA012RN_0013.jpg")
  }

}
