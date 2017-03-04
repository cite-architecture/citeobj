package edu.holycross.shot
package citeobj {

  case class CiteObjectException(message: String = "", cause: Option[Throwable] = None) extends Exception(message) {
    cause.foreach(initCause)
  }

}
