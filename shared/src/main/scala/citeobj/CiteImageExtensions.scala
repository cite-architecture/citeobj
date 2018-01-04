  package edu.holycross.shot.citeobj

import edu.holycross.shot.cite._

import edu.holycross.shot.cex._

import scala.scalajs.js
import scala.scalajs.js.annotation._


/** Class mapping CITE Collections
*/
@JSExportAll case class ImageExtensions(
  protocolMap: Map[Cite2Urn, Vector[BinaryImageSource[Any]]]
) {


  /** Find a Vector of configured image extensions for a given collection.
  * If none found, return an empty Vector.
  *
  * @param collection Collection-level URN identifying a collection.
  */
  def extensions(collection: Cite2Urn): Vector[BinaryImageSource[Any]] = {
    try {
      protocolMap(collection)
    } catch {
      case e: Throwable => Vector[BinaryImageSource[Any]]()
    }
  }


  /** Create a new [[ImageExtensions]] object limited to
  * mappings for a given protocol.
  *
  * @param protocol String identifying the protocol used.
  */
  def forProtocol(protocol: String): ImageExtensions = {
    //ImageExtensions(protocolMap.filter(_._2.protocol == protocol))
    var filteredMap = Map[Cite2Urn,Vector[BinaryImageSource[Any]]]()
    for ( (coll, vect) <- protocolMap) {
      //println(s"Compare ${vect.map(_.protocol)}")
      val filteredSources = vect.filter(_.protocol.toLowerCase == protocol.toLowerCase)
      if (filteredSources.size > 0) {
        filteredMap += (coll -> filteredSources)
      }  else {
        //println(s"No matches for " + )
      }
    }
    ImageExtensions(filteredMap)
  }

  /** True if the given collection has one or more image extensions.
  *
  * @param collection Collection-level URN identifying a collection.
  */
  def isExtended(collection: Cite2Urn):  Boolean = {
    (extensions(collection).size > 0)
  }
}


/** Factory for making ImageExtensions from CEX source.
*/
object ImageExtensions {

  /* Create ImageExtensions map from CEX source.
  *
  * @param cexSrc A String in CEX format including one or more
  * `imagedata` blocks.
  */
  def apply(cexSrc: String, separator: String = "#") : Option[ImageExtensions] = {

    var binarySourceMap = Map[Cite2Urn,Vector[BinaryImageSource[Any]]]()
    val cex = CexParser(cexSrc)
    val imageString = cex.blockString("imagedata")
    if (imageString.size == 0 ) {
      None
    } else {

      val rows = imageString.split("\n").toVector.filter(_.nonEmpty)
      val columnsByRow = rows.map(_.split(separator).toVector)
      for (columns <- columnsByRow) {
        val collectionUrn  = try {
          Cite2Urn(columns(0))
        } catch {
          case e: Throwable => throw CiteObjectException(s"Failed to make URN from ${columns(0)}; ${e.getMessage()}")
        }
        val protocol = columns(1)
        val initializer = columns(2)
        val rights = columns(3)

        val prevMap = try {
          binarySourceMap(collectionUrn)
        } catch {
          case _ : Throwable => Vector[BinaryImageSource[Any]]()
        }


        protocol match {


          case "CITE image string" => {
            val ajax = CiteImageAjax(initializer)
            val newMap = Vector(ajax) ++ prevMap
            binarySourceMap += (collectionUrn -> newMap)
          }
          case "iipImageDzString" => {
            val jpeg = IipImageDzString(initializer)
            val newMap = Vector(jpeg) ++ prevMap
            binarySourceMap += (collectionUrn -> newMap)
          }
          case "iipImageJpegString" => {
            val jpeg = IipImageJpegString(initializer)
            val newMap = Vector(jpeg) ++ prevMap
            binarySourceMap += (collectionUrn -> newMap)
          }
          case "localJpegString" => {
            val jpeg = LocalJpegString(initializer)
            val newMap = Vector(jpeg) ++ prevMap
            binarySourceMap += (collectionUrn -> newMap)
          }
          case "localDzString" => {
            val dzi = LocalDzString(initializer)
            val newMap = Vector(dzi) ++ prevMap
            binarySourceMap += (collectionUrn -> newMap)
          }
          case "CITE image URL" => {
            println("CITE image URL cannot be directly instatiated from CiteImage.\nPlease use one of the classes in this library's JVM subproject to load.")
          }
          case "local jpeg file" => {
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
