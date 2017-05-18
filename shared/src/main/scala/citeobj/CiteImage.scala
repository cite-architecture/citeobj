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
abstract class BinaryImageSource[T] {
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
case class CiteImageAjax(baseUrl: String)  extends BinaryImageSource[String] {
  def protocol = "AJAX request for CITE Image service"

  def binaryImageSource(u: Cite2Urn): String = {
    baseUrl +  s"request=GetBinaryImage&urn=${u}"
  }
}

/** Class mapping CITE Collections
*/
case class ImageExtensions(protocolMap: Map[Cite2Urn,BinaryImageSource[Any]])


/** Factory for making ImageExtensions from CEX source.
*/
object ImageExtensions {

  /* Create ImageExtensions map from CEX source.
  *
  * @param cexSrc A String in CEX format including one or more
  * `imagedata` blocks.
  */
  def apply(cexSrc: String) = {
    val cex = CexParser(cexSrc)
    val imageblocks = cex.block("imagedata")

  }
}
