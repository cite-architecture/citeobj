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
  def protocol = "Local file string"

  def binaryImageSource(u: Cite2Urn): String = {
    pathString(u, "jpg")
  }

  def pathString(u: Cite2Urn, fileExtension: String = "jpg"): String = {
    baseRef + u.collection + "/" + u.objectComponent + "." + fileExtension
  }
}


/** Class mapping CITE Collections
*/
@JSExport case class ImageExtensions(protocolMap: Map[Cite2Urn,BinaryImageSource[Any]])


/** Factory for making ImageExtensions from CEX source.
*/
object ImageExtensions {

  /* Create ImageExtensions map from CEX source.
  *
  * @param cexSrc A String in CEX format including one or more
  * `imagedata` blocks.
  */
  def apply(cexSrc: String, separator: String = "#") : Option[ImageExtensions] = {

    var binarySourceMap = Map[Cite2Urn,BinaryImageSource[Any]]()
    val cex = CexParser(cexSrc)
    val imageBlocks = cex.block("imagedata")
    if (imageBlocks.size == 0 ) {
      None
    } else {

      val rows = imageBlocks.mkString("\n").split("\n").filter(_.nonEmpty).toVector

      val columnsByRow = rows.map(_.split(separator).toVector)
      for (columns <- columnsByRow) {
        val collectionUrn  = Cite2Urn(columns(0))
        val protocol = columns(1)
        val initializer = columns(2)
        val rights = columns(3)
        protocol match {
          case "CITE image string" => {
            val ajax = CiteImageAjax(initializer)
            binarySourceMap += (collectionUrn -> ajax)
          }
          case "CITE image URL" => {
            println("CITE image URL cannot be directly instatiated from CiteImage.\nPlease use one of the classes in this library's JVM subproject to load.")
          }
          case "local jpeg" => {
              println("Local file protocols cannot be directly instatiated from CiteImage.\nPlease use one of the classes in this library's JVM subproject to load.")
          }
          case s: String => {println(s"Unrecognized protocol: ${s}.")}

        }
      }
    }
    if (binarySourceMap.size > 0) {
      Some(ImageExtensions(binarySourceMap))
    } else {
      None
    }
  }
}
