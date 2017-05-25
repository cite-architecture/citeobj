package edu.holycross.shot.citeobj

//import edu.holycross.shot.cite._
import scala.io.Source
//import java.io._

//import scala.collection.mutable.ArrayBuffer


/** Factory for creating [[CiteCollectionData]].
*/
object CiteDataSource {

  /** Create [[CiteCollectionData]] from a delimited text file.
  *
  * @param f Name of file with delimited text data.
  * @param collectionDefinition [[CiteCollectionDef]] for this collection.
  * @param delimiter Character used as column delimiter.
  */
  def fromFile(f: String, collectionDefinition: CiteCollectionDef, delimiter: Char = '#') : CiteCollectionData  = {
    val lns = Source.fromFile(f).getLines.toVector
    CiteCollectionData(lns.mkString("\n"))
  }
}
