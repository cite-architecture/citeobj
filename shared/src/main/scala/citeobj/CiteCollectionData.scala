package edu.holycross.shot.citeobj

import edu.holycross.shot.cite._


case class CiteCollectionData (data: Vector[CiteProperty]) {

  def ~~(filterUrn: Cite2Urn): CiteCollectionData = {
    CiteCollectionData(data.filter(_.urn ~~ filterUrn))
  }
}
