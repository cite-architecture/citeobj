package edu.holycross.shot.citeobj

import edu.holycross.shot.cite._


case class CitePropertyValue(urn: Cite2Urn, propertyValue: Any)


/*

object CiteProperty {
  def apply(urn: Cite2Urn, propType: CitePropertyType, propValue: Any, vocabularyList: Vector[String] = Vector.empty): CiteProperty = {
    propType match {
      case CtsUrnType => {
        propValue match {
          case u: CtsUrn => CiteProperty(urn, propType, u)
          case _ : CitePropertyType=> throw CiteObjectException("value " + propValue + " is not a CtsUrn")
        }
      }

      case _ => throw CiteObjectException("Unimplemented type")
    }
  }
}

      case Cite2UrnType => {
        propValue match {
          case u: Cite2Urn => CiteProperty(urn, propType, u)
          case _ => throw CiteObjectException("value " + propValue + " is not a Cite2Urn")
        }
      }

      case NumericType => {
        propValue match {
          case n: java.lang.Number => CiteProperty(urn, propType, n)
          case _ => throw CiteObjectException("value " + propValue + " is not a Numeric")
        }
      }


      case BooleanType => {
        propValue match {
          case b: Boolean => CiteProperty(urn, propType, b)
          case _ => throw CiteObjectException("value " + propValue + " is not a Boolean")
        }
      }


      case StringType => {
        propValue match {
          case s: String => CiteProperty(urn, propType, s)
          case _ => throw CiteObjectException("value " + propValue + " is not a String")
        }
      }

      case ControlledVocabType => {
        propValue match {
          case s: String => if (vocabularyList.contains(s)) {
              CiteProperty(urn,  propType, s)
            } else {
              throw CiteObjectException("value " + propValue + " is not in the controlled vocabulary list")
            }

          case _ => throw CiteObjectException("value " + propValue + " is not a String")
        }
      }
    }
  }
}*/
