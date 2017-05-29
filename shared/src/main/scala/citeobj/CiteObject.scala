package edu.holycross.shot.citeobj

import edu.holycross.shot.cite._

import scala.scalajs.js
import js.annotation.JSExport

/** Trait defining the minimum functionality of a discrete citable object. */
trait BaseCitable {

  /** URN uniquely identifying this object.  The URN must include
  * an object selector component, and its collection hierarachy must be at the version level.
  */
  def urn: Cite2Urn


  /** Human-readable string labelling this object. */
  def label: String

  /** Vector of any further [[CitePropertyValue]]s belonging to this object.
  * Note that the Vector may be empty.
  */
  def propertyList: Vector[CitePropertyImplementation]
}


/** Representation of a single citable object.  Note that this can easily be
* constructed from the properties in a set of [[CiteCollectionData]] by URN twiddling, but
* since the [[CiteObject]] representation elevates `urn` and `label` properties into
* object members, its `propertyList` will have two items fewer than the properties
* resulting from twiddling on a [[CiteCollectionData]] or from consulting the
* [[CiteCatalog]] corresponding to the [[CiteCollectionData]].
*
* @param urn Identifier required by [[BaseCitable]] trait.
* @param label Human-readable string required by [[BaseCitable]] trait.
* @param propertyList Possibly empty vector of any further [[CitePropertyValue]]s
* belonging to this object.
*/
@JSExport  case class CiteObject(
  urn: Cite2Urn,
  label: String,
  propertyList: Vector[CitePropertyImplementation]) extends BaseCitable {


    /** Find property definition for a given property.
    *
    * @param propertyUrn Property for which to find definition.
    */
    def definitionForProperty(propertyUrn: Cite2Urn) = {//: CitePropertyDef = {
      val matches = propertyList.filter(_.urn ~~ propertyUrn)
      require(matches.size == 1, s"Exception: found ${matches.size} match(es) for ${propertyUrn}")
      matches(0).propertyDef
    }


    /*
    p1.propertyList(0).propertyDef.propertyType
    */



    /** Find value of a given property.
    *
    * @param propertyUrn Property to find value for.
    */
    def propertyValue(propertyUrn: Cite2Urn): Any = {
      val matches = propertyList.filter(_.urn ~~ propertyUrn)
      require(matches.size == 1, s"Exception: found ${matches.size} match(es) for ${propertyUrn}")
      matches(0).propertyValue
    }

    /** True if any property of the object matches the given value.
    *
    * @param pValue Value to test for.
    */
    def valueEquals(pValue: Any): Boolean = {
      val matching = propertyList.filter(_.propertyValue == pValue)
      (matching.size > 0)
    }


    /** True if the value of a given property matches a given value.
    *
    * @param pValue Value to test for.
    * @param propertyUrn Property to test.
    */
    def valueEquals(propertyUrn : Cite2Urn, pValue: Any): Boolean = {
      val propDef = definitionForProperty(propertyUrn)
      if (CiteCollectionRepository.typesMatch(pValue, propDef.propertyType, propDef.vocabularyList)) {
        this.propertyValue(propertyUrn) == pValue
      } else {
        throw CiteObjectException(s"Type fails: ${propDef.propertyType} does not match value ${pValue}")
      }
    }

    /** True if any numeric property of the object is less than the given value.
    *
    * @param n Value to compare.
    */
    def numericLessThan(n: BigDecimal): Boolean = {
      val numericProperties = propertyList.filter(_.propertyDef.propertyType == NumericType)
      val numericValues = numericProperties.map{ p => CitePropertyValue.valueForString(p.propertyValue.toString,p.propertyDef)}
      val matching = numericValues.filter{ x  => BigDecimal(x.toString) < n }
      (matching.size > 0)
    }


    /** True if the value of a given property is less than a given value.
    *
    * @param propertyUrn Property to test.
    * @param n Value to compare.
    */
    def numericLessThan(propertyUrn : Cite2Urn, n: BigDecimal): Boolean = {
      val propDef = definitionForProperty(propertyUrn)
      if (CiteCollectionRepository.typesMatch(n, propDef.propertyType, propDef.vocabularyList)) {
        BigDecimal(this.propertyValue(propertyUrn).toString) < n
      } else {
        throw CiteObjectException(s"Type fails: ${propDef.propertyType} does not match value ${n}")
      }
    }

    /** True if any numeric property of the object is less than or equal to the given value.
    *
    * @param n Value to compare.
    */
    def numericLessThanOrEqual(n: BigDecimal): Boolean = {
      val numericProperties = propertyList.filter(_.propertyDef.propertyType == NumericType)
      val numericValues = numericProperties.map{ p => CitePropertyValue.valueForString(p.propertyValue.toString,p.propertyDef)}
      val matching = numericValues.filter{ x  => BigDecimal(x.toString) <= n }
      (matching.size > 0)
    }


    /** True if the value of a given property is less than or equal to a given value.
    *
    * @param propertyUrn Property to test.
    * @param n Value to compare.
    */
    def numericLessThanOrEqual(propertyUrn : Cite2Urn, n: BigDecimal): Boolean = {
      val propDef = definitionForProperty(propertyUrn)
      if (CiteCollectionRepository.typesMatch(n, propDef.propertyType, propDef.vocabularyList)) {
        BigDecimal(this.propertyValue(propertyUrn).toString) <= n
      } else {
        throw CiteObjectException(s"Type fails: ${propDef.propertyType} does not match value ${n}")
      }
    }

    /** True if any numeric property of the object is greater than the given value.
    *
    * @param n Value to compare.
    */
    def numericGreaterThan(n: BigDecimal): Boolean = {
      val numericProperties = propertyList.filter(_.propertyDef.propertyType == NumericType)
      val numericValues = numericProperties.map{ p => CitePropertyValue.valueForString(p.propertyValue.toString,p.propertyDef)}
      val matching = numericValues.filter{ x  => BigDecimal(x.toString) > n }
      (matching.size > 0)
    }


    /** True if the value of a given property is greater than a given value.
    *
    * @param propertyUrn Property to test.
    * @param n Value to compare.
    */
    def numericGreaterThan(propertyUrn : Cite2Urn, n: BigDecimal): Boolean = {
      val propDef = definitionForProperty(propertyUrn)
      if (CiteCollectionRepository.typesMatch(n, propDef.propertyType, propDef.vocabularyList)) {
        BigDecimal(this.propertyValue(propertyUrn).toString) > n
      } else {
        throw CiteObjectException(s"Type fails: ${propDef.propertyType} does not match value ${n}")
      }
    }

    /** True if any numeric property of the object is greater than or equal to the given value.
    *
    * @param n Value to compare.
    */
    def numericGreaterThanOrEqual(n: BigDecimal): Boolean = {
      val numericProperties = propertyList.filter(_.propertyDef.propertyType == NumericType)
      val numericValues = numericProperties.map{ p => CitePropertyValue.valueForString(p.propertyValue.toString,p.propertyDef)}
      val matching = numericValues.filter{ x  => BigDecimal(x.toString) >= n }
      (matching.size > 0)
    }

    /** True if the value of a given property is greater than or equal to a given value.
    *
    * @param propertyUrn Property to test.
    * @param n Value to compare.
    */
    def numericGreaterThanOrEqual(propertyUrn : Cite2Urn, n: BigDecimal): Boolean = {
      val propDef = definitionForProperty(propertyUrn)
      if (CiteCollectionRepository.typesMatch(n, propDef.propertyType, propDef.vocabularyList)) {
        BigDecimal(this.propertyValue(propertyUrn).toString) >= n
      } else {
        throw CiteObjectException(s"Type fails: ${propDef.propertyType} does not match value ${n}")
      }
    }

    /** True if the value of any numeric property of the object falls within a range
    * of two numeric values.
    *
    * @param n1 Lower bound,inclusive.
    * @param n2 Upperbound, inclusive.
    */
    def numericWithin(n1: BigDecimal, n2:  BigDecimal): Boolean = {
      val numericProperties = propertyList.filter(_.propertyDef.propertyType == NumericType)
      val numericValues = numericProperties.map{ p => CitePropertyValue.valueForString(p.propertyValue.toString,p.propertyDef)}
      val matching = numericValues.filter{ x  => (BigDecimal(x.toString) >= n1) && (BigDecimal(x.toString) <= n2) }
      (matching.size > 0)
    }

    /** True if the value of a given property falls within a range
    * of two numeric values.
    *
    * @param propertyUrn Property to test.
    * @param n1 Lower bound,inclusive.
    * @param n2 Upperbound, inclusive.
    */
    def numericWithin(propertyUrn : Cite2Urn, n1: BigDecimal, n2: BigDecimal): Boolean = {
      val propDef = definitionForProperty(propertyUrn)
      require(CiteCollectionRepository.typesMatch(n1, propDef.propertyType, propDef.vocabularyList), s"Lower bound ${n1} did not match type ${propDef.propertyType}")
      require(CiteCollectionRepository.typesMatch(n2, propDef.propertyType, propDef.vocabularyList), s"Upper bound ${n2} did not match type ${propDef.propertyType}")
      (BigDecimal(this.propertyValue(propertyUrn).toString) >= n1) &&     (BigDecimal(this.propertyValue(propertyUrn).toString) <= n2)
    }

    /** True if any string property of the object contains a given substring,
    * optionally taking case into consideration.
    *
    * @param s Substring to compare.
    * @param caseSensitive True if case should be considered in comparing strings.
    */
    def stringContains(s: String, caseSensitive: Boolean = true): Boolean = {
      val stringProperties = propertyList.filter(p => (p.propertyDef.propertyType == StringType) || (p.propertyDef.propertyType) == ControlledVocabType )
      val stringValues = stringProperties.map{ p => CitePropertyValue.valueForString(p.propertyValue.toString,p.propertyDef)}
      val matching = if (caseSensitive) {
         stringValues.filter(_.toString.contains(s))
       } else {
         stringValues.filter(_.toString.toLowerCase.contains(s.toLowerCase))
       }
      (matching.size > 0)
    }


    /** True if any the value of a given property contains a given substring,
    * optionally taking case into consideration.
    *
    * @param propertyUrn Property to test.
    * @param s Substring to compare.
    * @param caseSensitive True if case should be considered in comparing strings.
    */
    def stringContains(propertyUrn : Cite2Urn, s: String, caseSensitive: Boolean): Boolean = {
      val propDef = definitionForProperty(propertyUrn)
      if ((propDef.propertyType == StringType) || (propDef.propertyType == ControlledVocabType)) {
        caseSensitive match {
          case true => this.propertyValue(propertyUrn).toString.contains(s)
          case false => this.propertyValue(propertyUrn).toString.toLowerCase.contains(s.toLowerCase)
        }

      } else {
        throw CiteObjectException(s"Type ${propDef.propertyType} did not match value ${s}." )
      }
    }


    /** True if any string property of the object matches the given
    * regular expression.
    *
    * @param re String definition of a regular expression to test for.
    */
    def regexMatch(re: String): Boolean = {
      val rePattern = re.toString.r
      val stringProperties = propertyList.filter(p => (p.propertyDef.propertyType == StringType) || (p.propertyDef.propertyType) == ControlledVocabType )
      val stringValues = stringProperties.map{ p => CitePropertyValue.valueForString(p.propertyValue.toString,p.propertyDef)}
      val matching = stringValues.filter(s => rePattern.findFirstIn(s.toString) != None)
      (matching.size > 0)
    }


    /** True if the value of a given property matches the given
    * regular expression.
    *
    * @param propertyUrn Property to test.
    * @param re String definition of a regular expression to test for.
    */
    def regexMatch(propertyUrn : Cite2Urn, re: String): Boolean = {
      val propDef = definitionForProperty(propertyUrn)
      if ((propDef.propertyType == StringType) || (propDef.propertyType == ControlledVocabType)) {
        val rePattern = re.toString.r
        val s = this.propertyValue(propertyUrn).toString
        (rePattern.findFirstIn(s) != None)

      } else {
        throw CiteObjectException(s"Type ${propDef.propertyType} did not match value for ${re}." )
      }
    }



    /** True if any URN property of the object matches the given
    * URN.
    *
    * @param u URN test for.
    */
    def urnMatch(u: Urn): Boolean = {
      val urnProperties = propertyList.filter(p => (p.propertyDef.propertyType == CtsUrnType) || (p.propertyDef.propertyType) == Cite2UrnType )


      u match {
        case cts: CtsUrn => {
          val urnValues = urnProperties.map{ p => CtsUrn(p.propertyValue.toString) }
          val matching =  urnValues.filter(_ ~~ u)
          (matching.size > 0)
        }
        case cite2: Cite2Urn => {
          val urnValues = urnProperties.map{ p => Cite2Urn(p.propertyValue.toString) }
          val matching =  urnValues.filter(_ ~~ u)
          (matching.size > 0)
        }
      }
    }



    /** True if a given property matches the given URN.
    *
    * @param propertyUrn Property to test.
    * @param u URN test for.
    */
    def urnMatch(propertyUrn: Cite2Urn, u: Urn): Boolean = {
      val propDef = definitionForProperty(propertyUrn)

      propDef.propertyType match {
        case CtsUrnType => {
          u match {
            case cts: CtsUrn => {
              val urn = CtsUrn( this.propertyValue(propertyUrn).toString)
              cts ~~ urn
            }
            case _ =>   throw CiteObjectException(s"Type ${propDef.propertyType} did not match value for ${u}." )
          }
        }
        case Cite2UrnType => {
          u match {
            case cite2: Cite2Urn => {
              val urn = Cite2Urn( this.propertyValue(propertyUrn).toString)
              cite2 ~~ urn
            }
            case _ =>   throw CiteObjectException(s"Type ${propDef.propertyType} did not match value for ${u}." )
          }
        }
        case _ =>   throw CiteObjectException(s"Type ${propDef.propertyType} did not match value for ${u}." )
      }
/*
      if ((propDef.propertyType == CtsUrnType) || (propDef.propertyType == Cite2UrnType)) {
        u match {
          case cts: CtsUrn => {
            val urn = CtsUrn( this.propertyValue(propertyUrn).toString)
            cts ~~ urn
          }
          case cite2: Cite2Urn => {
            val urn = Cite2Urn( this.propertyValue(propertyUrn).toString)
            cite2 ~~ urn
          }
        }
      } else {
        throw CiteObjectException(s"Type ${propDef.propertyType} did not match value for ${u}." )
      }
      */
    }


}
