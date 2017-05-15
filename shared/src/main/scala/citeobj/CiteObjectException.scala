package edu.holycross.shot




package citeobj {

  import scala.scalajs.js
  import js.annotation.JSExport

  /** Extension for exceptions specific to the `citeobj` package.
  */
  @JSExport  case class CiteObjectException(message: String = "", cause: Option[Throwable] = None) extends Exception(message) {
    cause.foreach(initCause)
  }

}
