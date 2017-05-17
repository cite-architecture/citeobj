package edu.holycross.shot.citeobj

import edu.holycross.shot.cite._


/** Trait for a source of binary image data. */
trait BinaryImageSource[T] {
  def protocol: String   // or Cite2Urn
  def binaryImageSource(u: Cite2Urn): T
}


/*

    #!imagedata

    # Lines are structured as:
    # collection#protocol#base URL#rights property

    urn:cite2:hmt:vaimg.v1:#CITE image#http://www.homermultitext.org/hmtdigital/images?#urn:cite2:hmt:msA.v1.rights:
    urn:cite2:hmt:vaimg.v1:#IIIF#http://www.homermultitext.org/image2/context.json#urn:cite2:hmt:msA.v1.rights:
    urn:cite2:hmt:vaimg.v1:#local file#file://./images#urn:cite2:hmt:msA.v1.rights:
*/
