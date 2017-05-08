package edu.holycross.shot.citeobj

import edu.holycross.shot.cite._
import scala.io.Source
import java.io._

import com.github.tototoshi.csv._




object CiteDataSource {

  implicit object PoundFormat extends DefaultCSVFormat {
    override val delimiter = '#'
  }

  implicit object CommaFormat extends DefaultCSVFormat {
    override val delimiter = ','
  }
  implicit object TabFormat extends DefaultCSVFormat {
    override val delimiter = '\t'
  }


  /** Create [[CiteCollectionData]] from maps of property names to serialized data strings.
  *
  * @param dataMaps Vector of maps with each vector representing one CITE object, and
  * each mapping representing a mapping of property name to string value for that property.
  * @param collectionDef [[CiteCollectionDef]] for this collection.
  */
  def collectionForMappedText(dataMaps: Vector[Map[String,String]], collectionDef: CiteCollectionDef) = {

  }

  /** Create [[CiteCollectionData]] from a delimited text file.
  *
  * @param f Name of file with delimited text data.
  * @param collectionDefinition [[CiteCollectionDef]] for this collection.
  * @param delimiter Character used as column delimiter.
  */
  def fromFile(f: String, collectionDefinition: CiteCollectionDef, delimiter: Char = '#') /*: CiteCollectionData */ = {

    delimiter match {
      case '#' => {
        val reader = CSVReader.open(f) (PoundFormat)
        collectionForMappedText(reader.allWithHeaders().toVector, collectionDefinition)
      }
      case ',' => {
        val reader = CSVReader.open(f) (CommaFormat)
        collectionForMappedText(reader.allWithHeaders().toVector, collectionDefinition)
      }
      case '\t' => {
        val reader = CSVReader.open(f) (TabFormat)
        collectionForMappedText(reader.allWithHeaders().toVector, collectionDefinition)
      }
    }


    /*
    val citableNodes = stringPairs.map( arr => CitableNode(Cite2Urn(arr(0)), arr(1)))
    Corpus(citableNodes)
    */
  }


}
