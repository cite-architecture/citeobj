package edu.holycross.shot.citeobj


import scala.io.Source





/** Factory for creating [[CiteCollectionRepository]].
*/
 object CiteRepositorySource {

  /** Create [[CiteCollectionRepository]] from a delimited text file.
  *
  * @param f Name of file with data in CEX format.
  * @param delimiter String used as primary column delimiter.
  * @param secondaryDelimiter String used as secondary object delimiter.
  */
  def fromFile(f: String, delimiter: String = "#", secondaryDelimiter: String = ",") : CiteCollectionRepository  = {
    val lns = Source.fromFile(f).getLines.toVector
    CiteCollectionRepository(lns.mkString("\n"),delimiter,secondaryDelimiter)
  }
}
