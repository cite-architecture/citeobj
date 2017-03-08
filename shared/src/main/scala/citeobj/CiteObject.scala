package edu.holycross.shot.citeobj

import edu.holycross.shot.cite._

trait BaseObject {
  def urn: Cite2Urn
  def label: String
  def propertyList: Vector[CitePropertyValue]
}

case class CiteObject(urn: Cite2Urn, label: String, propertyList: Vector[CitePropertyValue]) extends BaseObject
