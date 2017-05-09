package edu.holycross.shot.citeobj

import edu.holycross.shot.cite._
import scala.io.Source
import java.io._

import com.github.tototoshi.csv._

import scala.collection.mutable.ArrayBuffer




object CiteDataSource {


  // write api docs for these objects...
  //
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
  * @param dataMap Vector of maps with each vector representing one CITE object, and
  * each mapping representing a mapping of property name to string value for that property.
  * @param collectionDef [[CiteCollectionDef]] for this collection.
  */
  def propertiesForMappedText(dataMap: Map[String,String], collectionDef: CiteCollectionDef) = { //: Vector[CitePropertyValue] = {
    var propertyBuffer = ArrayBuffer[CitePropertyValue]()
    /*println("\n\nWORK ON COLLE DEF " + collectionDef.urn)
    for (p <- collectionDef.propertyDefs) {
      println("\t" + p)
    }
*/
    val lcMap = dataMap.map{ case (k,v) => (k.toLowerCase,v)}
    //println("LC MAP keys "+ lcMap.keySet)
    val collectionUrn = collectionDef.urn
    val urn = Cite2Urn(lcMap("urn"))
    val lcLabelProperty = lcLabel(collectionDef)
    val label = lcMap(lcLabelProperty)


    //println("Figure out how to extract props for " + dataMap.keySet)
    for (k <- dataMap.keySet) {
      if ((k.toLowerCase == lcLabelProperty) || (k.toLowerCase == "urn")) {
          // omit
      } else {
       //println("Configure " + k + " -> " + dataMap(k))
       val propUrn = collectionUrn.addProperty(k)
       //println("Prop urn is " + propUrn)

       val propDef = collectionDef.propertyDefs.filter(_.urn == propUrn)
       // check that you have one and only  one propDef ...
       val typedValue = CitePropertyValue.valueForString(dataMap(k), propDef(0))

       val citePropertyVal = CitePropertyValue(propUrn, typedValue)
       propertyBuffer += citePropertyVal
       //println("\t-> " + dataMap(lcMap(k)))
      }
    }

    propertyBuffer
  }


  /** Find lowercase version of the name of the labelling property
  * in a CITE Collection's definition.
  *
  * @param collectionDef Definition of the collection's structure.
  */
  def lcLabel(collectionDef: CiteCollectionDef) = {
    collectionDef.labelProperty.property.toLowerCase
  }

  /** Create [[CiteCollectionData]] from a delimited text file.
  *
  * @param f Name of file with delimited text data.
  * @param collectionDefinition [[CiteCollectionDef]] for this collection.
  * @param delimiter Character used as column delimiter.
  */
  def fromFile(f: String, collectionDefinition: CiteCollectionDef, delimiter: Char = '#') : CiteCollectionData  = {

    delimiter match {
      case '#' => {
        val reader = CSVReader.open(f) (PoundFormat)
        val vectorList = for (propertyMap <- reader.allWithHeaders() ) yield {
          println("PROP MAP keys " + propertyMap.keySet)
          propertiesForMappedText(propertyMap, collectionDefinition)
        }
        CiteCollectionData(vectorList.flatMap( v => v).toVector)
        //
        //collectionForMappedText(reader.allWithHeaders().toVector, collectionDefinition)
      }
      case ',' => {
        val reader = CSVReader.open(f) (CommaFormat)
            val vectorList =  for (propertyMap <- reader.allWithHeaders() ) yield{
          propertiesForMappedText(propertyMap, collectionDefinition)
        }
        CiteCollectionData(vectorList.flatMap( v => v).toVector)
        //collectionForMappedText(reader.allWithHeaders().toVector, collectionDefinition)
      }
      case '\t' => {
        val reader = CSVReader.open(f) (TabFormat)
        val vectorList =  for (propertyMap <- reader.allWithHeaders() )yield {
          propertiesForMappedText(propertyMap, collectionDefinition)
        }
        CiteCollectionData(vectorList.flatMap( v => v).toVector)
        //collectionForMappedText(reader.allWithHeaders().toVector, collectionDefinition)
      }
    }


    /*
    val citableNodes = stringPairs.map( arr => CitableNode(Cite2Urn(arr(0)), arr(1)))
    Corpus(citableNodes)
    */
  }


}
