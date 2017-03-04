package edu.holycross.shot
package citeimage {

  case class CiteObjectException(message: String = "", cause: Option[Throwable] = None) extends Exception(message) {
    cause.foreach(initCause)
  }

}
