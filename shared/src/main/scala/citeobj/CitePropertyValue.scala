package edu.holycross.shot.citeobj

import edu.holycross.shot.cite._

import scala.scalajs.js
import js.annotation.JSExport

/** An instance of a property value.
*
* @param urn URN identifying this value. Note that the URN must
* include an object selector, and its collection component must
* be at the property level.
* @param propertyValue A value. Its type must
* be valid for the property type cataloged for the property of which
* this is an instance.
*/
@JSExport  case class CitePropertyValue(urn: Cite2Urn, propertyValue: Any) {
  require(urn.property != "", s"${urn} must include property component")
  require(urn.objectComponent != "", s"${urn} must include property component")
}


/** Factory for creating values from string serializations.
*/
object CitePropertyValue {

  /** Create a value of the correct type for a given string.
  *
  * @param stringValue String serialization to convert to a value of
  * appropriate type.
  * @param propertyDefinition Definition of the type of value to be created.
  */
  def valueForString(stringValue: String, propertyDefinition: CitePropertyDef): Any = {

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
