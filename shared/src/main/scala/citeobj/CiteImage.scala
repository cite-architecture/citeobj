package edu.holycross.shot.citeobj

import edu.holycross.shot.cite._
import java.net.URL
import java.io.File


/** Trait for a source of binary image data. */
trait BinaryImageSource {
  type BinaryImageAccess
  def binaryImageSource: BinaryImageAccess
}


// these concrete implementations must be JVM-specific
//

/** Binary image data from a local file.
*
* @param localFile File in local file system with binary image data.

case class LocalImageSource(val localFile: File) extends BinaryImageSource {
  type BinaryImageAccess = File
  def binaryImageSource = {localFile}
}
*/
/** Binary image data from a networked source.
*
* @param url URL to binary image data.

case class NetworkedImageSource(val url: URL) extends BinaryImageSource {
  type BinaryImageAccess = URL
  def binaryImageSource = {url}
}
*/


/** Citable image object extended by binary data access.
*
* @param urn Identifier required by [[BaseObject]] trait.
* @param label Human-readable string required by [[BaseObject]] trait.
* @param propertyList Possibly empty vector of any further [[CitePropertyValue]]s
* belonging to this object.
* @param rights Licensing image for binary image data
* @param binarySource Source for binary image data.
*/
case class CitableImage(urn: Cite2Urn,
  label: String,
  propertyList: Vector[CitePropertyValue],
  rights: String,
  binarySource: BinaryImageSource
) extends BaseCitable {
}


/** A collection of citeable images.
*
* @param urn URN for the collection.  Must include a
* version level component for collection, and must not
* include an object selector component.
* @param protocol Identifier for protocol to use for binary image access.
* @param baseAccessPoint A generic [[BinaryImageSource]] from which protocol-specific
* [[BinaryImageSource]]s can be derived for individual images.
* @param rightsProperty URN for property including licensing data for binary
* image data accessed via the defined protocol.
* @param dataCollection Contents of the generic CITE Collection for this collection.
*/
class CiteableImageCollection (
  val urn: Cite2Urn,
  val protocol: String,
  val baseAccessPoint: BinaryImageSource,
  val rightsProperty: Cite2Urn,
  val dataCollection: CiteCollectionData
  )  {
}

/*

    #!imagedata

    # Lines are structured as:
    # collection#protocol#base URL#rights property

    urn:cite2:hmt:vaimg.v1:#CITE image#http://www.homermultitext.org/hmtdigital/images?#urn:cite2:hmt:msA.v1.rights:
    urn:cite2:hmt:vaimg.v1:#IIIF#http://www.homermultitext.org/image2/context.json#urn:cite2:hmt:msA.v1.rights:
    urn:cite2:hmt:vaimg.v1:#local file#file://./images#urn:cite2:hmt:msA.v1.rights:
*/
