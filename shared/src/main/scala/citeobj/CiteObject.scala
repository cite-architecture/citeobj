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

    /** True if the value of any numeric property of the object falls with a range
    * of two numeric values.
    *
    * @param n1 Lower bound,inclusive.
    * @param n2j Upperbound, inclusive.
    */
    def numericWithin(n1: BigDecimal,n2:  BigDecimal): Boolean = {
      val numericProperties = propertyList.filter(_.propertyDef.propertyType == NumericType)
      val numericValues = numericProperties.map{ p => CitePropertyValue.valueForString(p.propertyValue.toString,p.propertyDef)}
      val matching = numericValues.filter{ x  => (BigDecimal(x.toString) >= n1) && (BigDecimal(x.toString) <= n2) }
      (matching.size > 0)
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

}
