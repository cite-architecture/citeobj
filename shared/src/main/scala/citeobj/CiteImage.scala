package edu.holycross.shot.citeobj

import edu.holycross.shot.cite._

import edu.holycross.shot.cex._

import scala.scalajs.js
import js.annotation.JSExport

/** Trait for a source of binary image data.
* Implementing classes must identify the protocol for access
* and be able to create an object of appropriate type in that protocol
* for a given object identified by URN.
*
* See for example the implementation in the [[CiteImageAjax]] class.
*/
abstract class BinaryImageSource[+T] {
  def protocol: String   // or should it be a Cite2Urn?
  def binaryImageSource(u: Cite2Urn): T
}

/** Implement [[BinaryImageSource]] trait for a CITE Image Service
* accessed from AJAX calls requiring a simple String rather than
* a JVM URL object. See a similar example of type URL in the
* JVM subproject's `CiteRESTImage` class.
*
* @param baseUrl Base URL for a CITE Image service.
*/
@JSExport case class CiteImageAjax(baseUrl: String)  extends BinaryImageSource[String] {
  def protocol = "AJAX request for CITE Image service"

  def binaryImageSource(u: Cite2Urn): String = {
    baseUrl +  s"request=GetBinaryImage&urn=${u}"
  }
}

/** Implement [[BinaryImageSource]] trait for jpeg files in a local file system.
*
* @param baseRef Root directory for local jpeg files.
*/
@JSExport case class LocalJpegString(baseRef: String)  extends BinaryImageSource[String] {
  def protocol = "Local jpeg string"

  def binaryImageSource(u: Cite2Urn): String = {
    pathString(u, "jpg")
  }

  def pathString(u: Cite2Urn, fileExtension: String = "jpg"): String = {
    baseRef + u.collection + "/" + u.objectComponent + "." + fileExtension
  }
}
