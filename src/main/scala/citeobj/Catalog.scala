package edu.holycross.shot.citeobj

import edu.holycross.shot.cite._
import scala.io.Source
import scala.collection.mutable.ArrayBuffer
import scala.util.Try

/**
* @param urn A Collection-level URN.
*/
case class Catalog(entries: Vector[CatalogEntry])
/*

case class Catalog (collections: Vector[CatalogEntry]) {
  def entriesForUrn(filterUrn: CtsUrn): Vector[CatalogEntry] = {
    collections.filter(_.urn.urnMatch(filterUrn))
  }
}

object Catalog {
  def apply(f: String, sep: String = "#"): Catalog = {
    var entries = scala.collection.mutable.ArrayBuffer.empty[CatalogEntry]
    // read file, drop header line:
    val columnsByRow = Source.fromFile(f).getLines.toVector.map(_.split(sep)).drop(1)

    for (row <- columnsByRow) {
      val urn = CtsUrn(row(0))
      val citation = row(1)
      val group = row(2)
      val work = row(3)
      val vers = row(4)
      val online = Try(row(6).toBoolean).getOrElse(false)
      if (row(5).isEmpty) {
        entries += CatalogEntry(urn,citation,group,work,vers,None,online)
      } else {
        entries += CatalogEntry(urn,citation,group,work,vers,Some(row(5)),online)

      }
    }
    Catalog(entries.toVector)
  }


}*/
