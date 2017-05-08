package edu.holycross.shot.citeobj

import edu.holycross.shot.cite._


case class CitePropertyValue(urn: Cite2Urn, propertyValue: Any)

object CitePropertyValue {
  def valueForString(stringVal: String, propertyType: CitePropertyType) = {
    propertyType match {
      case StringType => stringVal
      case _ => 
    }

  }
}
