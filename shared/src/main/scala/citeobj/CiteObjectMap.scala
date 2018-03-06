package edu.holycross.shot.citeobj

import edu.holycross.shot.cite._
import edu.holycross.shot.cex._

import scala.collection.mutable.ArrayBuffer


import scala.scalajs.js
import scala.scalajs.js.annotation._


/** Map of URN -> CiteObject for a repository of citable objects and properties.
* The data are organized as a Map[Cite2Urn,CiteObject].
*
* @param objectMap map of urns to cite2 objects.
*/
@JSExportAll  case class CiteObjectMap (objectMap: Map[Cite2Urn,CiteObject]) {

  /** Filter map by identifying URN.
  */
  def ~~(filterUrn: Cite2Urn): CiteObjectMap = {
    val newMap:Map[Cite2Urn, CiteObject] = objectMap.filterKeys( _ ~~ filterUrn )
    CiteObjectMap(newMap)
  }

  /** Union of this collection with a second collection.
  * The resulting collection continues the set of unique members
  * belonging to either collection.
  *
  * @param collection2 Collection to unite with this collection.
  def ++(collection2 : CiteCollectionData): CiteCollectionData = {
    CiteCollectionData(this.data ++ collection2.data)
  }
  */


  /** Difference of this collection with a second collection.
  * The resulting collection contains member elements in one collection
  * but not the other.
  *
  * @param collection2 Collection to unite with this collection.
  def --(collection2 : CiteCollectionData): CiteCollectionData = {
    CiteCollectionData(this.data diff collection2.data)
  }
  */

  /** Number of objects in the collection.
  */
  def size: Int = {
    objectMap.size
  }

  /** True if there are no objects.
  */
  def isEmpty: Boolean = {
    objectMap.isEmpty
  }


  /** Find URNs for each collection in the data set.
  def collections = {
    data.map(_.urn.dropProperty).map(_.dropSelector).toSet
  }
  */

  /** Find URNs for each object in the collection.
  def objects = {
    data.map(_.urn.dropProperty).distinct.toSet
  }
  */

  /** Find URNs for each property value in the collection.
  def properties = {
    data.map(_.urn.dropSelector).distinct.toSet
  }
  */

  /* * Value for a single property value.
  * It is a CiteObjectException if propUrn does not
  * identify exactly 1 property value.
  *
  * @param propUrn URN identifying a [[CitePropertyValue]]
  def propertyValue(propUrn: Cite2Urn) = {
    val selectProperty  = this  ~~ propUrn
    selectProperty.size match {
      case 1 => selectProperty.data(0).propertyValue
      case 0 => throw CiteObjectException("No property value found matching " + propUrn)
      case _ => throw CiteObjectException("Too general URN: found " + selectProperty.size + " matches.")
    }
  }
  */
}


/** Factory for creating [[CiteCollectionData]] from source data in CEX format.
*/
object CiteObjectMap {

  /** Creates a Map of CITE Collection Obvjects from a CEX source.
  *
  * @param cexSource Text in CEX format.  Note that there must be  one `citedata` block per
  * collection and at least one `citecatalog` block that may contain catalog data for
  * any number of collections.
  * @param delimiter String defining structural units of delimited text content.
  * @param delimiter2 Secondary delimiter used within controlled vocabulary lists,
  * if any.
  */
  def apply(cexSource: String, delimiter: String = "#", delimiter2: String = ",") : CiteObjectMap = {
    val cex = CexParser(cexSource)
    val catalog = CiteCatalog(cexSource, delimiter, delimiter2)
    val dataSets = cex.blockVector("citedata")

    var mapBuffer:Map[Cite2Urn,CiteObject] = Map()

    for (ds <- dataSets){

      val collUrn = CiteCollectionData.collectionForDataBlock(ds,delimiter)
      val collectionDef = catalog.collection(collUrn)

      collectionDef match {
        case None =>
        case cd: Some[CiteCollectionDef] => {
          val mapped = mapsForDelimited(ds,delimiter).map(_.toMap)
          val labellingProperty:Option[Cite2Urn] = cd.get.labellingProperty
          for ((row,i) <- mapped.zipWithIndex) {
            val thisObjectUrn:Cite2Urn = {
              propertiesForMappedText(row, cd.get)(0).urn.dropProperty
            }
            val thisObjectProps:Vector[CitePropertyImplementation] = {
              propertiesForMappedText(row, cd.get).map( r => {
                val u:Cite2Urn = r.urn
                val d:CitePropertyDef = cd.get.propertyDefs.filter(_.urn == u.dropSelector)(0)
                val v:Any = r.propertyValue
                CitePropertyImplementation(u,d,v)
              })
            }

            // We don't want the URN property or the labelling property
            val thisFilteredProps:Vector[CitePropertyImplementation] = {
                thisObjectProps.filter(
                  (p) => (
                    (p.urn.dropSelector != labellingProperty.get) &&
                    (p.urn.property != "urn")
                  )
                )
            }

            val thisObjectLabel:String = labellingProperty match {
              case Some(lp) => {
                val thisLabel = row(s"${lp.propertyOption.get}")
                thisLabel
              }
              case None => {
                throw CiteObjectException("No label for object: " + thisObjectUrn)
              }
            }
            println(s"- ${i}")
            mapBuffer += (thisObjectUrn -> CiteObject(thisObjectUrn,thisObjectLabel, thisFilteredProps))
          }
        }
      }
    }

    println("----- done constructing mapBuffer, copying to unmutable map…")
    val objectMap:Map[Cite2Urn,CiteObject] = mapBuffer
    println("…done copying. Memory pressure should drop.")

    CiteObjectMap(objectMap)
  }



  /**  Find collection URN for data documented in a citedata block of
  * CEX data documenting a single collection.
  *
  * @param dataBlock String data in CEX format documenting data for a single
  * CITE Collection.
  * @param delimiter String value delimiting columns in the CEX source.
  */
  def collectionForDataBlock(dataBlock: String, delimiter : String) = {//: Cite2Urn = {
    val dataLines = dataBlock.split("\n").toVector
    if (dataLines.size < 2) {
      throw CiteObjectException("Data block has too few lines: " + dataLines.size)

    } else {
      val lcHeader = dataLines(0).split(delimiter).toVector.map(_.trim).map(_.toLowerCase)
      if (lcHeader.size < 1) {
        throw CiteObjectException("No header found for required property 'urn'")
      } else {
        val columnIdx = lcHeader.indexOf("urn")
        Cite2Urn(dataLines(1).split(delimiter)(columnIdx)).dropSelector
      }
    }
  }


  /** Create [[CiteCollectionData]] from maps of property names to serialized data strings.
  *
  * @param dataMap Vector of maps with each vector representing one CITE object, and
  * each mapping representing a mapping of property name to string value for that property.
  * @param collectionDef [[CiteCollectionDef]] for this collection.
  */
  def propertiesForMappedText(dataMap: Map[String,String], collectionDef: CiteCollectionDef) : Vector[CitePropertyValue] = {
    var propertyBuffer = ArrayBuffer[CitePropertyValue]()

    //println(s"Map ${dataMap.size} properties using def containing ${collectionDef.propertyDefs.size} props")
    val lcMap = dataMap.map{ case (k,v) => (k.toLowerCase,v)}
    val collectionUrn = collectionDef.urn
    val urn = Cite2Urn(lcMap("urn"))
    val lcLabelProperty = lcLabel(collectionDef)
    val label = lcMap(lcLabelProperty)


    val objectSelectorString = {
      for (k <- dataMap.keySet) yield {
        if (k.toLowerCase == "urn") {
          dataMap(k)
        } else { ""}
      }
    }.filter(_.nonEmpty).toSeq(0)

    val objectSelectorUrn = Cite2Urn(objectSelectorString)
    //println("OBJ URN " + objectSelectorUrn )
    for (k <- dataMap.keySet) {
       val propUrn = objectSelectorUrn.addProperty(k)
       //println("With prop" + propUrn)
       //println("CHeck out " + collectionDef.propertyDefs.map(_.urn))
       //println("Look for " +propUrn.dropSelector + "in " + collectionDef.propertyDefs.map(_.urn))
       val propDef = collectionDef.propertyDefs.filter(_.urn == propUrn.dropSelector)
       // check that you have one and only  one propDef ...
       //println("PROP DEF " + propDef.size)

       if (propDef.size == 1) {
         val typedValue = CitePropertyValue.valueForString(dataMap(k), propDef(0))
         val citePropertyVal = CitePropertyValue(propUrn, typedValue)
         propertyBuffer += citePropertyVal
       }else{
         println("No propdef matching " + propUrn)
       }

      //}
    }
    //println(s"has ${propertyBuffer.toVector.size} items in buffer " + propertyBuffer.toVector + "\n\n")
    propertyBuffer.toVector
  }

  /** Find lowercase version of the name of the labelling property
  * in a CITE Collection's definition.
  *
  * @param collectionDef Definition of the collection's structure.
  */
  def lcLabel(collectionDef: CiteCollectionDef) = {
    collectionDef.labelProperty.property.toLowerCase
  }
}
