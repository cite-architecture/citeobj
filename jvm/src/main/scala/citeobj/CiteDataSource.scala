package edu.holycross.shot.citeobj

import edu.holycross.shot.cite._
import scala.io.Source
import java.io._






object CiteDataSource {
  def fromFile(f: String, delimiter: String = "\t") /*: CiteCollectionData */ = {
    val stringPairs = Source.fromFile(f).getLines.toVector.map(_.split(delimiter))
    /*
    val citableNodes = stringPairs.map( arr => CitableNode(Cite2Urn(arr(0)), arr(1)))
    Corpus(citableNodes)
    */
  }


}
