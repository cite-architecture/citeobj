package edu.holycross.shot




package citeobj {

  import scala.scalajs.js
  import scala.scalajs.js.annotation._

  /** Extension for exceptions specific to the `citeobj` package.
  */
  @JSExportAll  case class CiteObjectException(message: String = "", cause: Option[Throwable] = None) extends Exception(message) {
    cause.foreach(initCause)
  }

}
