package edu.holycross.shot.citeobj

import edu.holycross.shot.cite._
import scala.io.Source
import java.io._
import scala.xml.XML


object CiteCatalogSource {


  def fromFile(f: String, delimiter: String = "\t") /*: CiteCatalog*/ = {

    val root = XML.loadFile(f)
    CiteCatalog.fromNodeSeq(root)

  }


}
