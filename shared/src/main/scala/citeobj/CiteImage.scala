package edu.holycross.shot.citeobj

import edu.holycross.shot.cite._
import java.net.URL


/** Placeholder for possible implementation of a CitableImage trait.
* This design may change radically before first release.
*/
trait CitableImage {
  def caption: String
  def rights: String
  def binaryUrl: URL
}


/** Placeholder for later implementation of a CiteImage class.
* This design may change radically before first release.
*/
case class CiteImage(urn: Cite2Urn, label: String, propertyList: Vector[CitePropertyValue] = Vector.empty, caption: String, rights: String, binaryUrl: URL) extends BaseObject with CitableImage {

}
