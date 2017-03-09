package edu.holycross.shot.citeobj

import scala.scalajs.js
import edu.holycross.shot.cite._

object Main extends js.JSApp {
  def main(): Unit = {
    val citeValue =
    CitePropertyValue(Cite2Urn("urn:cite2:hmt:speeches.v1.passage:speech4"),CtsUrn("urn:cts:greekLit:tlg0012.tlg001:1.85-1.91"))

    println("Citable property:\n\t" + citeValue.urn)
    println("has value:\n\t"+ citeValue.propertyValue)
  }
}
