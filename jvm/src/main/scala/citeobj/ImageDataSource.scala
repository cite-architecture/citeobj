package edu.holycross.shot.citeobj

import edu.holycross.shot.cite._
import java.net.URL
import java.io.File

/** Implement [[BinaryImageSource]] trait for files in a local file system.
*
* @param baseUrl Base URL for a CITE Image service.
*/
case class LocalJpeg(baseRef: String)  extends BinaryImageSource[File] {
  def protocol = "Local file" // or urn?

  def binaryImageSource(u: Cite2Urn): File = {
    imageFile(u, "jpg")
  }

  def imageFile(u: Cite2Urn, fileExtension: String = "jpg"): java.io.File = {
    new File(baseRef + u.collection + "/" + u.objectComponent + "." + fileExtension)
  }
}

/** Implement [[BinaryImageSource]] trait for source using the CITE Image Service.
*
* @param baseUrl Base URL for a CITE Image service.
*/
case class CiteRESTImage(baseUrl: URL)  extends BinaryImageSource[URL] {
  def protocol = "CITE Image service" // or urn??

  def binaryImageSource(u: Cite2Urn) = {
  	 new URL(baseUrl.toString + s"request=GetBinaryImage&urn=${u}")
  }
}
