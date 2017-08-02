package edu.holycross.shot.citeobj

import edu.holycross.shot.cite._
import scala.collection.mutable.ArrayBuffer

import scala.scalajs.js
import scala.scalajs.js.annotation._

/** A cataloged collection of citable data.
* In initialization, the constructor validates every
* object in the data against the structure defined
* in the catalog.
*
* @param data Collection of data values.
* @param catalog Documentation of the structure of each collection.
*/
@JSExportAll  case class CiteCollectionRepository (data: CiteCollectionData, catalog: CiteCatalog) {

  assert (validateAll)

  /** Validate that data and catalog agree.
  */
  def validateAll: Boolean = {
    // Mutual validation of data and catalog:
    //assert(data.isEmpty == false, "Empty data collection: cannot create repository.")
    assert(catalog.isEmpty == false, "Empty catalog: cannot create repository.")
    // enforce 1<->1 relation of properties
    // (and therefore collections, too) between
    // catalog and data

    // This is a subtler question...
    //
    //assert(data.properties == catalog.properties, s"failed when comparing ${data.properties.size} data properties to ${catalog.properties.size} catalog properties.  Data properties: \n${data.properties}\n vs catalog properties: \n${catalog.properties}")

    // Validate contents of all objects in the repository against their
    // catalog description.
    for (c <- data.objects) {
      assert(objectValidates(c),"Failed to validate object " + c)
    }
    // if we make it this far without exceptions, we're valid:
    true
  }


  /** Find Vector of [[CiteObject]]s identified by URNs matching a given range URN.
  *
  * @param filterUrn URN to match.
  */
  def rangeFilter (filterUrn: Cite2Urn): Vector[CiteObject] = {
    if (filterUrn.isRange) {
      val baseUrn = filterUrn.dropSelector
      val simpleObjectUrn = filterUrn.dropExtensions
      if (catalog.isOrdered(baseUrn)) {

        val obj1 = citableObject(simpleObjectUrn.rangeBeginUrn.dropProperty)
        val obj2 = citableObject(simpleObjectUrn.rangeEndUrn.dropProperty)

        val idx1 = indexOf(obj1)
        val idx2 = indexOf(obj2) + 1 // "until" value
        objectsForCollection(baseUrn).slice(idx1 , idx2)

      } else {
        throw CiteObjectException(s"Range expression not valid unless collection is ordered: ${filterUrn}")
      }
    } else {
      throw  CiteObjectException(s"Function rangeFilter only applicable to range expressions: ${filterUrn}")
    }
  }

  /** Find Vector of [[CiteObject]]s identified by URNs matching a given URN.
  *
  * @param filterUrn URN to match.
  */
  def ~~ (filterUrn: Cite2Urn): Vector[CiteObject] = {
    filterUrn.objectComponentOption match {
      case None =>   citableObjects.filter(_.urn ~~ filterUrn)
      case _ =>  if (filterUrn.isObject) {
          citableObjects.filter(_.urn ~~ filterUrn)
        } else {
          rangeFilter(filterUrn)
        }
      }
  }

  /** Construct a citable object for an identifying URN.
  *
  * @param obj URN uniquely identifying a single object.
  * @param labelPropertyUrn URN of property to elevate to required label of
  * a [[CiteObject]].
  */
  def citableObject(objUrn: Cite2Urn, labelPropertyUrn : Cite2Urn): CiteObject = {
    require(objUrn.version != "", s"Cannot make citable object from urn without version identifier: ${objUrn}")


    val objectData = data ~~ objUrn

    val urnProperty = objUrn.addProperty("urn")
    val identifier = objectData ~~ urnProperty
    require(identifier.data.size == 1, s"For ${urnProperty}, found ${identifier.data.size} matches")
    val dropUrnProperty = objectData -- identifier

    val labeller = objectData ~~ labelPropertyUrn
    assert(labeller.data.size == 1)
    val labelProperty = labeller.data(0)
    val remainingProps = dropUrnProperty -- labeller

    val propertyImplementations = for (p <- remainingProps.data) yield {
      // p is the CitePropertyValue, so get the corresponding
      // CitePropertyDef, and construct an implementation:
      val pdef = catalog.propertyDefinition(p.urn)
      val propertyImpl = CitePropertyImplementation(p.urn,pdef.get, p.propertyValue)
      propertyImpl
    }
    CiteObject(
      objUrn,
      labelProperty.propertyValue.toString,
      propertyImplementations )
  }

  /** Construct a citable object for an identifying URN.
  *
  * @param obj URN uniquely identifying a single object.
  */
  def citableObject(objUrn: Cite2Urn) : CiteObject = {

    require(objUrn.version != "", s"Cannot make citable object from urn without version identifier: ${objUrn}")

    val collectionDef = collectionDefinition(objUrn.dropSelector)
    collectionDef match {
      case None => throw CiteObjectException(s"Could not find collection definition for ${objUrn}")
      case cd: Option[CiteCollectionDef] =>   citableObject(objUrn, cd.get.labelProperty)
    }
  }

  /** Convert all data to a Vector of [[CiteObject]]s.
  *
  * @param ccd [[CiteCollectionData]] to convert to a Vector of [[CiteObject]]s.
  */
  def citableObjects : Vector[CiteObject] = {
    val buffer = ArrayBuffer[CiteObject]()
    val collUrns = data.collections
    for (u <- collUrns) {
      val propUrn = u.addProperty("urn")
      val urndata = data ~~ propUrn
      for (uprop <- urndata.data) {
        uprop.propertyValue match {
          case objUrn : Cite2Urn => {
            buffer += citableObject(objUrn)
          }
          case _ => println("Could not figure out type of " + uprop.propertyValue)
        }
      }
    }
    buffer.toVector
  }


  /** Make a Vector of [[CiteObject]]s for a collection.
  * If the collection is ordered, the resulting Vector will
  * be sorted by the collection's  ordering property.
  *
  * @param coll Collection URN.
  */
  def objectsForCollection(coll: Cite2Urn) :  Vector[CiteObject]  = {
    val v = citableObjects.filter(_.urn ~~ coll)
    if (isOrdered(coll)) {
      v.sortWith(sortValue(_) < sortValue(_))
    } else {
      v
    }
  }


  /** Find catalog entry for a given collection.
  *
  * @param collUrn Collection's URN.
  */
  def collectionDefinition(collUrn: Cite2Urn): Option[CiteCollectionDef]= {
    catalog.collection(collUrn)
  }

  /** Find data set for a given collection.
  *
  * @param collUrn Collection's URN.
  */
  def collectionData(collUrn: Cite2Urn): CiteCollectionData = {
    data ~~ collUrn
  }

  /** True if the given [[CiteObject]] validates against the collection's definition.
  * In actuality, won't ever return false, but will throw an Exception if requirements
  * checked by `assert` statements are violated, otherwise returns true.
  *
  * @param citeObj Citable object to evaluate.
  * @param collectionDef Collection definition to use in evaulating object.
  */
  def objectMatchesCatalog(citeObj: CiteObject,collectionDef: CiteCollectionDef): Boolean = {
    val catalogSet = collectionDef.propertyDefs
    val catalogPropUrns =  catalogSet.map(_.urn)

    val objUrnSet = citeObj.propertyList.map(_.urn).toSet
    val catUrnSet = catalogPropUrns.toSet

    // Constructed object elevates URN and label
    // properties out of the catalog set:
    val expectedSize = catUrnSet.size - 2

    assert(expectedSize ==
       citeObj.propertyList.size, s"for ${citeObj.urn}, expected ${expectedSize} properties but found ${citeObj.propertyList.size} in \n${citeObj.propertyList.map(_.urn)}")

    for (p <- citeObj.propertyList) {
      assert(catalogPropUrns.contains(p.urn.dropSelector))
      val propDef = catalogSet.filter(_.urn == p.urn.dropSelector)
      assert(propDef.size == 1, s"Wrong number propDefs (${propDef.size})  for " + p.urn.dropSelector + " from \n\n" + propDef.mkString("\n\n"))

      // get type from catalog def
      val expectedType = propDef(0).propertyType
      // get controlled vocab vector:
      val vocabVector = propDef(0).vocabularyList

      assert(CiteCollectionRepository.typesMatch(p.propertyValue,expectedType,vocabVector ),s"For ${p.urn}, ${p.propertyValue} did not match ${expectedType}")
    }
    //passed all assertions!
    true
  }

  /** True if we can construct a valid object for a URN,
  * confirm 1<->1 relation of cataloged properties and
  * actual properties, and validate the type of each property value.
  *
  * @param objectUrn URN uniquely identifyiing an object.  It is an assertion exception if the URN does not match 1 and only 1 entry in the [[catalog]].
  */
  def objectValidates(objectUrn: Cite2Urn): Boolean = {
    val collectionCatalog = catalog ~~ objectUrn
    assert(collectionCatalog.size == 1, s"Validating ${objectUrn}, found ${collectionCatalog.size} matching objects")

    val collectionDefinition = collectionCatalog.collections(0)
    val asCitableObject = citableObject(objectUrn,collectionDefinition.labelProperty)

    objectMatchesCatalog(asCitableObject,collectionDefinition)
  }

  /** Set of all cataloged collections in the repository,
  * identified by URN.
  */
  def collections = {
    catalog.urns
  }

  /** True if collection is ordered.
  *
  * @param coll Collection to test.
  */
  def isOrdered(coll: Cite2Urn): Boolean = {
    catalog.isOrdered(coll)
  }

  /** Look up value of ordering property for a citable object.
  *
  * @param obj Citable object.
  * @param propertyKey URN identifying its ordering property.
  */
  def sortValue(obj: CiteObject, propertyKey: Cite2Urn) = {
    val propVect = obj.propertyList.filter(_.urn ~~ propertyKey)
    require (propVect.size == 1, s"Wrong number of ordering properties (${propVect.size}) for ${propertyKey} in ${obj.propertyList}")
    propVect(0).propertyValue match {
      case d: Double => d
      case _ => throw CiteObjectException(s"Did not find property value for ${propVect(0)}")
    }
  }

  /** Look up value of ordering property for a citable object.
  *
  * @param obj Citable object.
  */
  def sortValue(obj: CiteObject): Double = {
    // find its orderingKey:
    val collectionDef = collectionDefinition(obj.urn.dropSelector)
    collectionDef match {
      case None => throw CiteObjectException(s"Could not find collection definition for ${obj}")
      case cd: Option[CiteCollectionDef] =>   sortValue(obj, cd.get.orderingProperty.get)
    }
  }

  /** Find first citable object in an ordered collection.
  *
  * @param coll Collection URN.
  */
  def first(coll: Cite2Urn) : CiteObject = {
    if (! isOrdered(coll)) {
      throw CiteObjectException(s"${coll} is not an ordered collection.")
    } else {
      val v = objectsForCollection(coll)
      v(0)
    }
  }

  /** Find last citable object in an ordered collection.
  *
  * @param coll Collection URN.
  */
  def last(coll: Cite2Urn) : CiteObject = {
    if (! isOrdered(coll)) {
      throw CiteObjectException(s"${coll} is not an ordered collection.")
    } else {
      val v = objectsForCollection(coll)
      v.last
    }
  }


  /** Find following citable object in an ordered collection.
  *
  * @param obj Find object following this.
  */
  def next(obj: CiteObject) : Option[CiteObject] = {
    val objects = objectsForCollection(obj.urn.dropSelector)
    val limit = objects.size - 2
    val idx = indexOf(obj)
    idx match {
      case x if x == limit => None
      case n => {
        Some(objects(n + 1))
      }
    }
  }

  /** Find preceding citable object in an ordered collection.
  *
  * @param obj Find object preceding this.
  */
  def prev(obj: CiteObject) : Option[CiteObject] = {
    val objects = objectsForCollection(obj.urn.dropSelector)
    val idx = indexOf(obj)
    idx match {
      case 0 => None
      case n => {
        Some(objects(n - 1))
      }
    }
  }

  /** Index of a citable object in an ordered collection.
  *
  * @param obj A citable object in an ordered collection.
  */
  def indexOf(obj: CiteObject) : Int = {
    val v = objectsForCollection(obj.urn.dropSelector)
    v.indexOf(obj)
  }

  /** Set of all properties in the repository,
  * identified by URN.
  */
  def properties = {
    data.properties
  }

  /** Find value of a given property.
  *
  * @param propertyUrn Property to find value for.
  */
  def propertyValue(propertyUrn: Cite2Urn): Any = {
    data.propertyValue(propertyUrn)
  }

  /** Find all citable objects with a given property value.
  *
  * @param pValue Value to search for.
  */
  def valueEquals(pValue: Any): Vector[CiteObject] = {
    citableObjects.filter(_.valueEquals(pValue))
  }

  /* Find objects with a given value for a given property.
  *
  * @param propertyUrn Property to examine.
  * @param pValue Value to search for.
  */
  def valueEquals(propertyUrn: Cite2Urn, pValue: Any): Vector[CiteObject] = {
    citableObjects.filter(_.valueEquals(propertyUrn,pValue))
  }

  /** Find all citable objects with a numeric property value
  * less than a given value.
  *
  * @param pValue Value to compare.
  */
  def numericLessThan(pValue: Double): Vector[CiteObject] = {
    citableObjects.filter(_.numericLessThan(pValue))
  }

  /** Find citable objects with a given numeric property having a value
  * less than a given value.
  *
  * @param propertyUrn Property to examine.
  * @param pValue Value to compare.
  */
  def numericLessThan(propertyUrn: Cite2Urn, pValue: Double): Vector[CiteObject] = {
    citableObjects.filter(_.numericLessThan(propertyUrn,pValue))
  }


  /** Find all citable objects with a numeric property value
  * less than or equal to a given value.
  *
  * @param pValue Value to compare.
  */
  def numericLessThanOrEqual(pValue: Double): Vector[CiteObject] = {
    citableObjects.filter(_.numericLessThanOrEqual(pValue))
  }


  /** Find all citable objects with a numeric property value
  * less than or equal to a given value.
  *
  * @param propertyUrn Property to examine.
  * @param pValue Value to compare.
  */
  def numericLessThanOrEqual(propertyUrn: Cite2Urn, pValue: Double): Vector[CiteObject] = {
    citableObjects.filter(_.numericLessThanOrEqual(propertyUrn,pValue))
  }




    /** Find all citable objects with a numeric property value
    * greater than a given value.
    *
    * @param pValue Value to compare.
    */
    def numericGreaterThan(pValue: Double): Vector[CiteObject] = {
      citableObjects.filter(_.numericGreaterThan(pValue))
    }

    /** Find all citable objects with a given property value
    * greater than a given value.
    *
    * @param propertyUrn Property to examine.
    * @param pValue Value to compare.
    */
    def numericGreaterThan(propertyUrn: Cite2Urn, pValue: Double): Vector[CiteObject] = {
      citableObjects.filter(_.numericGreaterThan(propertyUrn,pValue))
    }



    /** Find all citable objects with a numeric property value
    * greater or equal to than a given value.
    *
    * @param pValue Value to compare.
    */
    def numericGreaterThanOrEqual(pValue: Double): Vector[CiteObject] = {
      citableObjects.filter(_.numericGreaterThanOrEqual(pValue))
    }

    /** Find all citable objects with a given property value
    * greater than or equal to a given value.
    *
    * @param propertyUrn Property to examine.
    * @param pValue Value to compare.
    */
    def numericGreaterThanOrEqual(propertyUrn: Cite2Urn, pValue: Double): Vector[CiteObject] = {
      citableObjects.filter(_.numericGreaterThanOrEqual(propertyUrn,pValue))
    }


    /** Find all citable objects with a numeric property value
    * within a given range.
    *
    * @param n1 Lower bound,inclusive.
    * @param n2 Upperbound, inclusive.
    */
    def numericWithin(n1: Double, n2: Double): Vector[CiteObject] = {
      citableObjects.filter(_.numericWithin(n1,n2))
    }

    /** Find all citable objects with a given property value falling
    * within a given range.
    *
    * @param propertyUrn Property to examine.
    * @param n1 Lower bound,inclusive.
    * @param n2 Upperbound, inclusive.
    */
    def numericWithin(propertyUrn: Cite2Urn, n1: Double, n2: Double): Vector[CiteObject] = {
      citableObjects.filter(_.numericWithin(propertyUrn,n1,n2))
    }


    /** Find all citable objects with a property containing a given substring,
    * optionally taking case into consideration.
    *
    * @param s Value to search for.
    * @param caseSensitive True if case should be considered in comparing strings.
    */
    def stringContains(s: String, caseSensitive: Boolean = true ): Vector[CiteObject] = {
      println(s"Looking for ${s} in citable objects")
      citableObjects.filter(_.stringContains(s, caseSensitive) )
    }



    /** Find citable objects with a given property containing a given substring,
    * optionally taking case into consideration.
    *
    * @param propertyUrn Property to examine.
    * @param s Value to search for.
    * @param caseSensitive True if case should be considered in comparing strings.
    */
    def stringContains(propertyUrn: Cite2Urn, s: String, caseSensitive: Boolean): Vector[CiteObject] = {
      citableObjects.filter(_.stringContains(propertyUrn, s, caseSensitive))
    }

    /** Find all citable objects with a property matching a given regular expression.
    *
    * @param re Regular expression to match.
    */
    def regexMatch(re: String): Vector[CiteObject] = {
      citableObjects.filter(_.regexMatch(re))
    }

    /** Find citable objects with a given property matching a given regular expression.
    *
    * @param propertyUrn Property to examine.
    * @param re Regular expression to match.
    */
    def regexMatch(propertyUrn: Cite2Urn, re: String): Vector[CiteObject] = {
      citableObjects.filter(_.regexMatch(propertyUrn, re))
    }

    /** Find all citable objects with a property matching a given URN.
    *
    * @param u URN to match.
    */
    def urnMatch(u: Urn): Vector[CiteObject] = {
      citableObjects.filter(_.urnMatch(u))
    }


    /** Find citable objects with a given property matching a given regular expression.
    *
    * @param propertyUrn Property to examine.
    * @param u URN to match.
    */
    def urnMatch(propertyUrn: Cite2Urn, u: Urn): Vector[CiteObject] = {
      citableObjects.filter(_.urnMatch(propertyUrn, u))
    }

}



/** Factory for creating [[CiteCollectionRepository]] from a CEX source.
*/
object CiteCollectionRepository {

  /** Creates CITE Collection data from a CEX source.
  *
  * @param cexSource Text in CEX format.  Note that there must be  one `citedata` block per
  * collection and at least one `citecollections` and `citeproperties` block that may contain catalog data for
  * any number of collections.
  * @param delimiter String defining structural units of delimited text content.
  * @param delimiter2 Secondary delimiter used within controlled vocabulary lists,
  * if any.
  */
  def apply(cexSource: String, delimiter: String = "#", delimiter2: String = ",") : CiteCollectionRepository = {
    val data = CiteCollectionData(cexSource,delimiter,delimiter2)
    val catalog = CiteCatalog(cexSource,delimiter,delimiter2)
    CiteCollectionRepository(data,catalog)
  }


  /** True if a given value is an appropriate type and value for the specified
  * [[CitePropertyType]].
  *
  * @param propertyVal Value to test.
  * @param expected [[CitePropertyType]] the value must validate against.
  * @param vocabList Vector of controlled vocabulary items if relevant, or
  * an empty vector otherwise.
  */
  def typesMatch(propertyVal: Any, expected: CitePropertyType, vocabList: Vector[String]) : Boolean = {
    expected match {
      case CtsUrnType => {
        propertyVal match {
          case u: CtsUrn => true
          case _ : CitePropertyType=> false
        }
      }
      case Cite2UrnType => {
        propertyVal match {
          case u: Cite2Urn => true
          case _ => false
        }
      }

      case NumericType => {
        propertyVal match {
          case n: java.lang.Number => true
          case _ => false
        }
      }

      case BooleanType => {
        propertyVal match {
          case b: Boolean => true
          case _ => false
        }
      }
      case StringType => {
        propertyVal match {
          case s: String => true
          case _ => false
        }
      }
      case ControlledVocabType => {
        propertyVal match {
          case s: String => if (vocabList.contains(s)) {true} else { false }
          case _ => false
        }
      }
    }
  }
}
