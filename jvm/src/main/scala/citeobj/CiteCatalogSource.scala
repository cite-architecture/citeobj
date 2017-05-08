package edu.holycross.shot.citeobj

import edu.holycross.shot.cite._
import scala.io.Source
import java.io._
import scala.xml.XML


/** Factory for creating CITE collection catalogs
* from various sources.
*/
object CiteCatalogSource {

  /** Create a CITE collection catalog from a file.
  *
  * @param f Name of file with XML catalog data
  * validating against the CITE collection schema.
  */
  def fromFile(f: String) : CiteCatalog = {
    val root = XML.loadFile(f)
    CiteCatalog.fromNodeSeq(root)
  }
}
