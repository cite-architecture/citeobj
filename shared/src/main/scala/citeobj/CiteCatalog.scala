package edu.holycross.shot.citeobj

import edu.holycross.shot.cite._



case class CiteCatalog(collections: Vector[CiteCollectionDef]) {

  def size : Int = collections.size
  def isEmpty: Boolean = collections.isEmpty

  def urns = collections.map(_.urn).toSet
  
  def properties = {
    val propertyUrns = collections.flatMap(_.propertyDefs.map(_.urn))
    val labelUrns = collections.map(_.labelProperty)
    val orderingUrns = collections.map(_.orderingProperty).flatten

    val propertySet = (propertyUrns ++ labelUrns ++ orderingUrns).toSet
    propertySet
  }


  def ~~(filterUrn: Cite2Urn) : CiteCatalog = {
    CiteCatalog(collections.filter(_.urn ~~ filterUrn))
  }
}
