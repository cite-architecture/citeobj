package edu.holycross.shot.citeobj

import edu.holycross.shot.cite._


case class CitePropertyValue(urn: Cite2Urn, propertyValue: Any)

object CitePropertyValue {
  def valueForString(stringValue: String, propertyDefinition: CitePropertyDef) = {

    propertyDefinition.propertyType match {

      case CtsUrnType => CtsUrn(stringValue)
      case Cite2UrnType => Cite2Urn(stringValue)
      case NumericType => stringValue.toDouble
      case BooleanType => stringValue.toBoolean
      case StringType => stringValue
      case ControlledVocabType => {
        if (propertyDefinition.vocabularyList.contains(stringValue)) {
          stringValue
        } else {
          throw CiteObjectException(s"Value ${stringValue} is not in controlled vocabulary " + propertyDefinition.vocabularyList.mkString(", "))
        }
      }

      case x => throw CiteObjectException("Unrecognized or unimplemented property type " + x)
    }

  }
}
