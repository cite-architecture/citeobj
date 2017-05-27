package edu.holycross.shot.citeobj


import scala.io.Source





/** Factory for creating [[CiteCollectionData]].
*/
  @deprecated ("CiteDataSource deprecated in favor of CiteRepositorySource", "3.0.0")  object CiteDataSource {

  /** Create [[CiteCollectionData]] from a delimited text file.
  *
  * @param f Name of file with delimited text data.
  * @param collectionDefinition [[CiteCollectionDef]] for this collection.
  * @param delimiter Character used as column delimiter.
  */
    @deprecated ("CiteDataSource deprecated in favor of CiteRepositorySource", "3.0.0")  def fromFile(f: String, collectionDefinition: CiteCollectionDef, delimiter: Char = '#') : CiteCollectionData  = {
    val lns = Source.fromFile(f).getLines.toVector
    CiteCollectionData(lns.mkString("\n"))
  }
}
