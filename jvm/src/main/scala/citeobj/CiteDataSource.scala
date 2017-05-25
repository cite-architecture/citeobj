package edu.holycross.shot.citeobj

import edu.holycross.shot.cite._
import scala.io.Source
import java.io._

//import com.github.tototoshi.csv._

import scala.collection.mutable.ArrayBuffer

object CiteDataSource {
  // write api docs for these objects...
  //

  /*
  implicit object PoundFormat extends DefaultCSVFormat {
    override val delimiter = '#'
  }

  implicit object CommaFormat extends DefaultCSVFormat {
    override val delimiter = ','
  }
  implicit object TabFormat extends DefaultCSVFormat {
    override val delimiter = '\t'
  }

*/






  /** Create [[CiteCollectionData]] from a delimited text file.
  *
  * @param f Name of file with delimited text data.
  * @param collectionDefinition [[CiteCollectionDef]] for this collection.
  * @param delimiter Character used as column delimiter.
  */
  def fromFile(f: String, collectionDefinition: CiteCollectionDef, delimiter: Char = '#') : CiteCollectionData  = {

    val lns = Source.fromFile(f).getLines.toVector
    CiteCollectionData(lns.mkString("\n"))
/*
    delimiter match {
      case '#' => {
        val reader = CSVReader.open(f) (PoundFormat)
        val vectorList = for (propertyMap <- reader.allWithHeaders() ) yield {

          CiteCollectionData.propertiesForMappedText(propertyMap, collectionDefinition)
        }
        CiteCollectionData(vectorList.flatMap( v => v).toVector)
        //
        //collectionForMappedText(reader.allWithHeaders().toVector, collectionDefinition)
      }
      case ',' => {
        val reader = CSVReader.open(f) (CommaFormat)
            val vectorList =  for (propertyMap <- reader.allWithHeaders() ) yield{
          CiteCollectionData.propertiesForMappedText(propertyMap, collectionDefinition)
        }
        CiteCollectionData(vectorList.flatMap( v => v).toVector)
        //collectionForMappedText(reader.allWithHeaders().toVector, collectionDefinition)
      }
      case '\t' => {
        val reader = CSVReader.open(f) (TabFormat)
        val vectorList =  for (propertyMap <- reader.allWithHeaders() )yield {
          CiteCollectionData.propertiesForMappedText(propertyMap, collectionDefinition)
        }
        CiteCollectionData(vectorList.flatMap( v => v).toVector)
        //collectionForMappedText(reader.allWithHeaders().toVector, collectionDefinition)
      }
    }
*/
  }
}
