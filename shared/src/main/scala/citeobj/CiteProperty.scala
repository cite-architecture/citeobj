package edu.holycross.shot.citeobj

import edu.holycross.shot.cite._


case class CiteProperty(propertyDef: CitePropertyDef, propertyValue: Any) {

}


object CiteProperty {
  def apply(propName: String, propType: CitePropertyType, propValue: Any): CiteProperty = {
    propType match {
      case CtsUrnType => {
        propValue match {
          case u: CtsUrn => CiteProperty(CitePropertyDef(propName, propType), u)
          case _ => throw CiteObjectException("value " + propValue + " is not a CtsUrn")
        }
      }

      case Cite2UrnType => {
        propValue match {
          case u: Cite2Urn => CiteProperty(CitePropertyDef(propName, propType), u)
          case _ => throw CiteObjectException("value " + propValue + " is not a Cite2Urn")
        }
      }

      case NumericType => {
        propValue match {
          case n: java.lang.Number => CiteProperty(CitePropertyDef(propName, propType), n)
          case _ => throw CiteObjectException("value " + propValue + " is not a Numeric")
        }
      }


      case BooleanType => {
        propValue match {
          case b: Boolean => CiteProperty(CitePropertyDef(propName, propType), b)
          case _ => throw CiteObjectException("value " + propValue + " is not a Boolean")
        }
      }


      case StringType => {
        propValue match {
          case s: String => CiteProperty(CitePropertyDef(propName, propType), s)
          case _ => throw CiteObjectException("value " + propValue + " is not a String")
        }
      }

      case _ => throw CiteObjectException("unimplemented cite type " + propType)
    }
  }
}
