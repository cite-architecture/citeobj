package edu.holycross.shot

import scala.collection.mutable.ArrayBuffer
import scala.collection.mutable.Map

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
      val cols = r.split(delimiter).toVector
      for (i <- 0 until cols.size) {
        headerToCell += (header(i) -> cols(i))
      }
      rowMaps += headerToCell
    }
    rowMaps.toVector
  }

}
