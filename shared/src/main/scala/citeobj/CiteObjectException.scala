package edu.holycross.shot
package citeobj {


  /** Extension for exceptions specific to the `citeobj` package.
  */
  case class CiteObjectException(message: String = "", cause: Option[Throwable] = None) extends Exception(message) {
    cause.foreach(initCause)
  }

}
