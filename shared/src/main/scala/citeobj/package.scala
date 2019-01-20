package edu.holycross.shot

import scala.collection.mutable.ArrayBuffer
import scala.collection.mutable.Map
import edu.holycross.shot.cite._

package object citeobj {

  /** Creates a Vector of maps from a delimited text file with header line.
  * Values of the header line serve as keys to the maps. Each entry in the vector maps a
  * single row's values from column header to corresponding value.
  *
  * @param src Delimited text data, including a header line.
  * @param delimiter String defining columns.
  */
  def mapsForDelimited(src: String, delimiter: String = "#"): Vector[Map[String, String]] = {
    val rows = src.split("\n").toVector
    val header = rows(0).split(delimiter).toVector
    var rowMaps = ArrayBuffer[Map[String, String]]()

    for (r <- rows.drop(1)) {
      val headerToCell = scala.collection.mutable.Map[String, String]()
      val cols = r.split(delimiter,-1).toVector
      if (cols.size == header.size) {
        for (i <- 0 until cols.size) {
          headerToCell += (header(i) -> cols(i))
        }
        rowMaps += headerToCell
      } else {
        val errorStr = s"""\nCiteObjectException\n\nCould not map header with ${header.size} items onto data row with ${cols.size} items.\n\nHeader:\n\n${header.mkString(" ")}\n\nInvalid row:\n\n${r}\n"""
        throw new CiteObjectException(errorStr)
      }
    }
    rowMaps.toVector
  }

  /** True if propertyValue is valid for propertyType.
  *
  * @param propertyValue Value for a CITE Collection property.
  * @param propertyDefe Definition for the property.
  */
  def validValue(propertyValue: Any, propertyDef: CitePropertyDef): Boolean = {
    propertyDef.propertyType match {
      case CtsUrnType =>  propertyValue match {
        case u: CtsUrn => true
        case _ => false
      }
      case Cite2UrnType =>    propertyValue match {
        case u: Cite2Urn => true
        case _ => false
      }
      case NumericType =>  propertyValue match {
        case i: Int => true
        case d: Double => true
        case _ => false
      }
      case BooleanType =>   propertyValue match {
        case b: Boolean => true
        case _ => false
      }
      case ControlledVocabType  =>   propertyValue match {
        case s: String => if (propertyDef.vocabularyList.contains(s)) { true } else { false }
        case _ => false
      }
      case StringType  =>   propertyValue match {
        case s: String =>  true
        case _ => false
      }
    }
  }

}
