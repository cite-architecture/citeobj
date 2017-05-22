package edu.holycross.shot.citeobj

import edu.holycross.shot.cite._
import scala.collection.mutable.ArrayBuffer

import scala.scalajs.js
import js.annotation.JSExport

/** A cataloged collection of citable data.
* In initialization, the constructor validates every
* object in the data against the structure defined
* in the catalog.
*
* @param data Collection of data values.
* @param catalog Documentation of the structure of each collection.
*/
@JSExport  case class CiteCollectionRepository (data: CiteCollectionData, catalog: CiteCatalog) {

  assert (validateAll)

  /** Validate that data and catalog agree.
  */
  def validateAll: Boolean = {
    // Mutual validation of data and catalog:
    assert(data.isEmpty == false)
    assert(catalog.isEmpty == false)
    // enforce 1<->1 relation of properties
    // (and therefore collections, too) between
    // catalog and data
    assert(data.properties == catalog.properties, s"failed when comparing ${data.properties.size} data properties to ${catalog.properties.size} catalog properties.  Data properties: \n${data.properties}\n vs catalog properties: \n${catalog.properties}")

    // Validate contents of all objects in the repository against their
    // catalog description.
    for (c <- data.objects) {
      assert(objectValidates(c),"Failed to validate object " + c)
    }
    // if we make it this far without exceptions, we're valid:
    true
  }

  def rangeFilter (filterUrn: Cite2Urn): Vector[CiteObject] = {
    if (catalog.isOrdered(filterUrn.dropSelector)) {
      Vector[CiteObject]()
    } else {
      throw CiteObjectException(s"Range expression not valid unless collection is ordered: ${filterUrn}")
    }

  }

  /**
  */
  def ~~ (filterUrn: Cite2Urn): Vector[CiteObject] = {
    if (filterUrn.isObject) {
      citableObjects.filter(_.urn ~~ filterUrn)
    } else {
      rangeFilter(filterUrn)
    }
  }

  /** Construct a citable object for an identifying URN.
  *
  * @param obj URN uniquely identifying a single object.
  * @param labelPropertyUrn URN of property to elevate to required label of
  * a [[CiteObject]].
  */
  def citableObject(objUrn: Cite2Urn, labelPropertyUrn : Cite2Urn): CiteObject = {

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
  def citableObjects(coll: Cite2Urn) :  Vector[CiteObject]  = {
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

  /** Set of all collections in the repository,
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
      throw CiteException(s"${coll} is not an ordered collection.")
    } else {
      val v = citableObjects(coll)
      v(0)
    }
  }

  /** Find last citable object in an ordered collection.
  *
  * @param coll Collection URN.
  */
  def last(coll: Cite2Urn) : CiteObject = {
    if (! isOrdered(coll)) {
      throw CiteException(s"${coll} is not an ordered collection.")
    } else {
      val v = citableObjects(coll)
      v.last
    }
  }


  /** Find following citable object in an ordered collection.
  *
  * @param obj Find object following this.
  */
  def next(obj: CiteObject) : Option[CiteObject] = {
    val objects = citableObjects(obj.urn.dropSelector)
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
    val objects = citableObjects(obj.urn.dropSelector)
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
    val v = citableObjects(obj.urn.dropSelector)
    v.indexOf(obj)
  }

  /** Set of all properties in the repository,
  * identified by URN.
  */
  def properties = {
    data.properties
  }
}



/** Factory for creating [[CiteCollectionRepository]] from a CEX source.
*/
object CiteCollectionRepository {

  /** Creates CITE Collection data from a CEX source.
  *
  * @param cexSource Text in CEX format.  Note that there must be  one `citedata` block per
  * collection and at least one `citecatalog` block that may contain catalog data for
  * any number of collections.
  * @param delimiter String defining structural units of delimited text content.
  * @param delimiter2 Secondary delimiter used within controlled vocabulary lists,
  * if any.
  */
  def apply(cexSource: String, delimiter: String = "#", delimiter2: String = ",") : CiteCollectionRepository = {
    //data: CiteCollectionData, catalog: CiteCatalog
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
