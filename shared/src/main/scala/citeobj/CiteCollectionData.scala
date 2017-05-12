package edu.holycross.shot.citeobj

import edu.holycross.shot.cite._
import edu.holycross.shot.cex._

import scala.collection.mutable.ArrayBuffer

/** All data values for a repository of citable
* objects and properties.
*
* @param data vector of property values. Because [[CitePropertyValue]]s have URNs, they can be grouped
* by object using URN twiddling.
*/
case class CiteCollectionData (data: Vector[CitePropertyValue]) {

  /** Filter collection data by identifying URN.
  */
  def ~~(filterUrn: Cite2Urn): CiteCollectionData = {
    CiteCollectionData(data.filter(_.urn ~~ filterUrn))
  }

  def ++(collection2 : CiteCollectionData): CiteCollectionData = {
    CiteCollectionData(this.data ++ collection2.data)
  }

  def --(collection2 : CiteCollectionData): CiteCollectionData = {
    CiteCollectionData(this.data diff collection2.data)
  }

  /** Number of property vaues in the collection.
  */
  def size: Int = {
    data.size
  }

  /** True if there are no property values.
  */
  def isEmpty: Boolean = {
    data.isEmpty
  }


  /** Find URNs for each collection in the data set.
  */
  def collections = {
    data.map(_.urn.dropProperty).map(_.dropSelector).toSet
  }

  /** Find URNs for each object in the collection.
  */
  def objects = {
    data.map(_.urn.dropProperty).distinct.toSet
  }

  /** Find URNs for each property value in the collection.
  */
  def properties = {
    data.map(_.urn.dropSelector).distinct.toSet
  }

  /** Value for a single property value.
  * It is a CiteObjectException if propUrn does not
  * identify exactly 1 property value.
  *
  * @param propUrn URN identifying a [[CitePropertyValue]]
  */
  def propertyValue(propUrn: Cite2Urn) = {
    val selectProperty  = this  ~~ propUrn
    selectProperty.size match {
      case 1 =>
      case 0 => throw CiteObjectException("No property value found matching " + propUrn)
      case _ => throw CiteObjectException("Too general URN: found " + selectProperty.size + " matches.")
    }
  }
}

object CiteCollectionData {


  /** Creates CITE Collection data from a CEX source.
  *
  * @param src Text in CEX format.  Note that there must be  one `citedata` block per
  * collection and at least one `citecatalog` block that may contain catalog data for
  * any number of collections.
  * @param delimiter String defining structural units of delimited text content.
  * @param delimiter2 Secondary delimiter used within controlled vocabulary lists,
  * if any.
  */
  def apply(cexSource: String, delimiter: String = "#", delimiter2: String = ",") : CiteCollectionData = {
    val catalog = CiteCatalog(cexSource)

    val cex = CexParser(cexSource)
    val dataSets = cex.block("citedata")

    val propBuffer = ArrayBuffer[CitePropertyValue]()
    for (ds <- dataSets){
      val collUrn = CiteCollectionData.collectionForDataBlock(ds,delimiter)
      val collectionDef = catalog.collection(collUrn).get
      val mapped = mapsForDelimited(ds,delimiter).map(_.toMap)
      for (row <- mapped) {
        for (propVal <- propertiesForMappedText(row, collectionDef)) {
          propBuffer +=  propVal
        }
      }
    }

    val valuesByProperty = propBuffer.toVector
    CiteCollectionData(valuesByProperty)
  }



  /**  Find collection URN for data documented in a citedata block of
  * CEX data documenting a single collection.
  *
  * @param dataBlock String data in CEX format documenting data for a single
  * CITE Collection.
  * @param delimiter String value delimiting columns in the CEX source.
  */
  def collectionForDataBlock(dataBlock: String, delimiter : String = "#") = {//: Cite2Urn = {
    val dataLines = dataBlock.split("\n").toVector
    val lcHeader = dataLines(0).split(delimiter).toVector.map(_.trim).map(_.toLowerCase)
    val columnIdx = lcHeader.indexOf("urn")
    Cite2Urn(dataLines(1).split(delimiter)(columnIdx)).dropSelector
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
}
