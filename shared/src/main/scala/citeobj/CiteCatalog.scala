package edu.holycross.shot.citeobj

import edu.holycross.shot.cite._




/** Catalog defining structure of all collections in a repository.
*
* @param collections Defintions of structure of individual collections.
*/
case class CiteCatalog(collections: Vector[CiteCollectionDef]) {

  /** Number of collections in the repository.
  */
  def size : Int = collections.size

  /** True if no collections in the repository.
  */
  def isEmpty: Boolean = collections.isEmpty

  /** Set of URNs identifying all collections in the repository.
  */
  def urns = collections.map(_.urn).toSet

  /** Select a specific [[CiteCollectionDef]] by URN value.
  */
  def collection(urn: Cite2Urn) :  Option[CiteCollectionDef] = {
    val filtered = collections.filter(_.urn == urn)
    filtered.size match  {
      case 0 => None
      case 1 => Some(filtered(0))
      case _ => throw CiteObjectException("Duplicate URNs found in catalog: " + urn)
    }

  }

  /** Set of URNs identifying all properties in the repository.
  */
  def properties = {
    val propertyUrns = collections.flatMap(_.propertyDefs.map(_.urn))
    val labelUrns = collections.map(_.labelProperty)
    val orderingUrns = collections.map(_.orderingProperty).flatten

    val propertySet = (propertyUrns ++ labelUrns ++ orderingUrns).toSet
    propertySet
  }


  /** Create a new catalog composed of entries
  * matching a given URN.
  *
  * @param filterUrn URN to match against.
  * @return A new CiteCatalog containing only entries
  * matching `filterUrn`.
  */
  def ~~(filterUrn: Cite2Urn) : CiteCatalog = {
    CiteCatalog(collections.filter(_.urn ~~ filterUrn))
  }
}

/** Object providing factory methods for
* creating CiteCatalog instances from various
* kinds of sources.
*/
object CiteCatalog {


  /** Determing Cite property type based on string name and presence of controlled vocabulary list.
  *
  * @param s Value of  `@type` attribute in a `citeProperty` XML element.
  * @param vocablist True if property has a controlled vocabulary list.
  */
  def typeForString(s: String, vocabList: Boolean): CitePropertyType = {
    if (vocabList) {
      s match {
        case "string" => ControlledVocabType
        case _ => throw CiteException("Controlled vocabulary lists only allowed with string type data.")
      }

    } else {
      s match {
        case "cite2urn" => Cite2UrnType
        case "number" => NumericType
        case "string" => StringType
        case _ => throw CiteException("Unrecognized attribute value for string: " + s)
      }
    }
  }
}
