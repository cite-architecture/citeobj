package edu.holycross.shot.citeobj

import edu.holycross.shot.cite._
import scala.collection.mutable.ArrayBuffer
import edu.holycross.shot.cex._

import java.lang.System.currentTimeMillis

import scala.scalajs.js
import scala.scalajs.js.annotation._

import scala.collection.immutable.ListMap

/** A cataloged collection of citable data.
* In initialization, the constructor validates every
* object in the data against the structure defined
* in the catalog.
*
* @param data Collection of data values.
* @param catalog Documentation of the structure of each collection.
*/
@JSExportAll  case class CiteCollectionRepository (data: CiteCollectionData, objects:CiteObjectMap, catalog: CiteCatalog) {

  assert (validateAll)

  /** Hold a vector of [[CiteObject]]s
  *
  */
  val citableObjects: Vector[CiteObject] = {
    this.objects.objectMap.map(obj => obj._2).toVector
  }

  /** A Map of (collection-level) URNs -> Vectors of URNs for that collection
  * For ordered collections, the Vector[Cite2Urn] will be in the correct order
  * for unordered collections, the Vector[Cite2Urn] will be in any old order
  */
  val collectionsMap:Map[Cite2Urn,Vector[Cite2Urn]] = {
      val timeStart =  java.lang.System.currentTimeMillis()
      val colls:Vector[Cite2Urn] = this.catalog.collections.map(_.urn)
      val cm = colls.map(c => (c,urnsForCollectionFromProperties(c))).toMap
      val timeEnd =  java.lang.System.currentTimeMillis()
      cm
  }

 


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
    /*
    for (c <- data.objects) {
      assert(objectValidates(c),"Failed to validate object " + c)
    }
    */
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
        val rangeBegin:Cite2Urn = simpleObjectUrn.rangeBeginUrn.dropProperty
        val rangeEnd:Cite2Urn = simpleObjectUrn.rangeEndUrn.dropProperty
        if (this.objects.objectMap.contains(rangeBegin) && this.objects.objectMap.contains(rangeEnd) ){
          val obj1 = citableObject(rangeBegin)
          val obj2 = citableObject(rangeEnd)
          val v = objectsForCollection(baseUrn)
          val idx1 = v.indexOf(obj1)
          val idx2 = v.indexOf(obj2) + 1 // "until" value
          val ofc = v.slice(idx1 , idx2)
          ofc
        } else {
          Vector()
        }
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
    //println(s"twiddling on: ${filterUrn}")
    val collUrn:Cite2Urn = {
      val u1:Cite2Urn = filterUrn.dropSelector
      u1.propertyOption match {
        case None => u1
        case _ => u1.dropProperty
      }
    }
    val collectionMatches:Vector[CiteCollectionDef] = this.catalog.collections.filter(_.urn ~~ collUrn)
    // Is the collection in the data? Don't waste time looking if it isn't.
    collectionMatches.size match {
      case 0 => {
        println(s"NO collection for twiddle with: ${filterUrn}")
          Vector()
      } 
      // Collection is in data
      case _ => {
        //println(s"matched collection for twiddle with: ${filterUrn}")
        // Object or Whole Collection?
        filterUrn.objectComponentOption match {
          // Whole Collection:
          case None => {  
            // WORK HERE!!!!!!!
            //println(s"No objectComponent for: ${filterUrn}")
            //citableObjects.filter(_.urn ~~ filterUrn)
            this.collectionsMap(collUrn).map( cu => this.objects.objectMap(cu))
          }
          // Object-component, present
          case _ => {
            // Object, not range
            //println(s"Yes objectComponent for: ${filterUrn}")
            if (filterUrn.isObject) {
              //println(s"isObject true for: ${filterUrn}")
              // Versioned URN?
              filterUrn.versionOption match {
                // Yes… has version
                case Some(v) => {
                  println(s"versionOption = Some(v) for: ${filterUrn}")
                  val tempVec:Vector[CiteObject] = {
                    objects.objectMap.contains(filterUrn.dropProperty.dropExtensions) match {
                     case true => {
                        Vector(objects.objectMap(filterUrn.dropProperty.dropExtensions))
                      }
                      case _ => Vector()
                    }
                  }
                  tempVec 
                }
                //No… notional object
                case _ => {
                  println(s"No versionOption for: ${filterUrn}")
                  val tempMap = objects.objectMap.filterKeys( _ ~~ filterUrn ) // faster!
                  tempMap.map( k => k._2 ).toVector
                }
              } 
            // range
            } else {
              rangeFilter(filterUrn)
            }
          }
        }
      }
    }
  }

  /** Construct a citable object for an identifying URN out of the vectdor of properties.
  *
  * @param obj URN uniquely identifying a single object.
  * @param labelPropertyUrn URN of property to elevate to required label of
  * a [[CiteObject]].
  */
  def citableObjectFromProperties(objUrn: Cite2Urn, labelPropertyUrn : Cite2Urn): CiteObject = {
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

  /** Retreive a citable object for an identifying URN from the objectMap
  * @param objUrn Cite2Urn
  */
  def citableObject(objUrn:Cite2Urn): CiteObject = {
    val urn = objUrn.dropExtensions.dropProperty
    if (urn.objectComponentOption == None) throw CiteObjectException(s"No object identifier on ${objUrn}")
    if (this.objects.objectMap.contains(urn) == false) throw CiteObjectException(s"${urn} not present in collection.")
    val thisObject = objects.objectMap(urn)
    thisObject
  }

  /** Construct a citable object for an identifying URN from the vector of properties
  *
  * @param obj URN uniquely identifying a single object.
  */
  def citableObjectFromProperties(objUrn: Cite2Urn) : CiteObject = {

    require(objUrn.version != "", s"Cannot make citable object from urn without version identifier: ${objUrn}")

    val collectionDef = collectionDefinition(objUrn.dropSelector)
    collectionDef match {
      case None => throw CiteObjectException(s"Could not find collection definition for ${objUrn}")
      case cd: Option[CiteCollectionDef] =>   citableObjectFromProperties(objUrn, cd.get.labelProperty)
    }
  }

  /** Convert all data to a Vector of [[CiteObject]]s.
  *
  */
  def citableObjectsFromProperties : Vector[CiteObject] = {
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

  /** Make a Vector of [[CiteObject]]s for a collection, from the vector of properties.
  * If the collection is ordered, the resulting Vector will
  * be sorted by the collection's  ordering property.
  *
  * @param coll Collection URN.
  */
  def urnsForCollectionFromProperties(urn: Cite2Urn) :  Vector[Cite2Urn]  = {
    val coll:Cite2Urn = urn.dropSelector.dropProperty
    val filteredMap:Map[Cite2Urn,CiteObject] = objects.objectMap.filterKeys(_ ~~ coll)
    if (isOrdered(coll)) {
      val sortedSeq:Seq[(Cite2Urn,CiteObject)] = filteredMap.toSeq.sortWith(
          (x,y) => ( sortValue(x._2) < sortValue(y._2) )
      )
      val sortedVec:Vector[Cite2Urn] = sortedSeq.map( x => x._1 ).toVector
      sortedVec
    } else {
      val sortedVec:Vector[Cite2Urn] = filteredMap.map(x => x._1).toVector
      sortedVec
    }
  }


  /** Make a Vector of [[CiteObject]]s for a collection, from the vector of properties.
  * If the collection is ordered, the resulting Vector will
  * be sorted by the collection's  ordering property.
  *
  * @param coll Collection URN.
  */
  def objectsForCollectionFromProperties(coll: Cite2Urn) :  Vector[CiteObject]  = {
    val v = citableObjects.filter(_.urn ~~ coll)
    if (isOrdered(coll)) {
      v.sortWith(sortValue(_) < sortValue(_))
    } else {
      v
    }
  }

  /** Make a Vector of [[CiteObject]]s for a collection.
  * If the collection is ordered, the resulting Vector will
  * be sorted by the collection's  ordering property.
  *
  * @param coll Collection URN.
  */
  def objectsForCollection(urn: Cite2Urn) :  Vector[CiteObject]  = {
    val coll:Cite2Urn = urn.dropSelector.dropProperty
    this.collectionsMap(coll).map(u => this.objects.objectMap(u)).toVector
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
    val asCitableObject = citableObjectFromProperties(objectUrn,collectionDefinition.labelProperty)

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
  def sortValue(obj: CiteObject, propertyKey: Cite2Urn):Double = {
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

    val cex = CexParser(cexSource)
    val catalog = CiteCatalog(cexSource, delimiter, delimiter2)

    val dataSets = cex.blockVector("citedata")


    // Temp buffers for holding objects
    val propBuffer = ArrayBuffer[CitePropertyValue]()
    val singleObjectPropBuffer = ArrayBuffer[CitePropertyValue]()
    val mapBuffer:collection.mutable.Map[Cite2Urn,CiteObject] = collection.mutable.Map()

    for (ds <- dataSets){
      val collUrn = CiteCollectionData.collectionForDataBlock(ds,delimiter)
      val collectionDef = catalog.collection(collUrn)
      collectionDef match {
        case None => println("missing collection definition")
        case cd: Some[CiteCollectionDef] => {
          val mapped = mapsForDelimited(ds,delimiter).map(_.toMap)
          val labellingProperty:Option[Cite2Urn] = cd.get.labellingProperty

          // let's get an index with this, for testing
          for ((row,i) <- mapped.zipWithIndex) {
            val mappedProps = propertiesForMappedText(row, cd.get)
            val thisObjectUrn:Cite2Urn = {
              mappedProps(0).urn.dropProperty
            }

            // Build up property vector
            for (propVal <- propertiesForMappedText(row, cd.get)) {
              propBuffer +=  propVal
              singleObjectPropBuffer += propVal
            }

            // Build up object map
            val thisObjectProps:Vector[CitePropertyImplementation] = {
              propertiesForMappedText(row, cd.get).map( r => {
                val u:Cite2Urn = r.urn
                val d:CitePropertyDef = cd.get.propertyDefs.filter(_.urn == u.dropSelector)(0)
                val v:Any = r.propertyValue
                CitePropertyImplementation(u,d,v)
              })
            }

            // We don't want the URN property or the labelling property 
            val thisFilteredProps:Vector[CitePropertyImplementation] = {
                thisObjectProps.filter( 
                  (p) => (
                    (p.urn.dropSelector != labellingProperty.get) && 
                    (p.urn.property != "urn")
                  )
                )
            }

            val thisObjectLabel:String = labellingProperty match {
              case Some(lp) => {
                val thisLabel = row(s"${lp.propertyOption.get}")
                thisLabel
              }
              case None => {
                throw CiteObjectException("No label for object: " + thisObjectUrn)
              }
            }

            val tempObject = CiteObject(thisObjectUrn,thisObjectLabel,thisFilteredProps)

            /* DO SOME VALIDATION!! */
            //throw CiteObjectException("No property value found matching " + propUrn)
           CiteCollectionRepository.objectMatchesCatalog(tempObject,cd.get) match {
            case true => 
            case _ => throw CiteObjectException(s"constructed object fails to match catalog: ${row}.")
           } 

           // Confirm that the props in the objects match the props in the data
           assert(singleObjectPropBuffer.size == (tempObject.propertyList.size + 2))

           // clear singleObjectPropBuffer
           singleObjectPropBuffer.clear

            // log each row, if you want to keep up
            if ( ( i % 1000) == 0){
              print(s"\b\b\b\b\b\b\b${i}")
            }
            mapBuffer += (thisObjectUrn -> tempObject)
          }
        }
      }
    }

    val valuesByProperty = propBuffer.toVector
    val objMap = mapBuffer.toMap
    val data = CiteCollectionData(valuesByProperty)
    val objects = CiteObjectMap(objMap) 
    CiteCollectionRepository(data,objects,catalog)
  }

/** Create [[CiteCollectionData]] from maps of property names to serialized data strings.
  *
  * @param dataMap Vector of maps with each vector representing one CITE object, and
  * each mapping representing a mapping of property name to string value for that property.
  * @param collectionDef [[CiteCollectionDef]] for this collection.
  */
  def propertiesForMappedText(dataMap: Map[String,String], collectionDef: CiteCollectionDef) : Vector[CitePropertyValue] = {
    var propertyBuffer = ArrayBuffer[CitePropertyValue]()

    val lcMap = dataMap.map{ case (k,v) => (k.toLowerCase,v)}
    val collectionUrn = collectionDef.urn
    val urn = Cite2Urn(lcMap("urn"))
    val lcLabelProperty = lcLabel(collectionDef)
    val label = lcMap(lcLabelProperty)

    val objectSelectorString = {
      for (k <- dataMap.keySet) yield {
        if (k.toLowerCase == "urn") {
          dataMap(k)
        } else { ""}
      }
    }.filter(_.nonEmpty).toSeq(0)

    val objectSelectorUrn = Cite2Urn(objectSelectorString)
    for (k <- dataMap.keySet) {
       val propUrn = objectSelectorUrn.addProperty(k)
       val propDef = collectionDef.propertyDefs.filter(_.urn == propUrn.dropSelector)

       if (propDef.size == 1) {
         val typedValue = CitePropertyValue.valueForString(dataMap(k), propDef(0))
         val citePropertyVal = CitePropertyValue(propUrn, typedValue)
         propertyBuffer += citePropertyVal
       }else{
         println("No propdef matching " + propUrn)
       }
    }
    propertyBuffer.toVector
  }

    /** Find lowercase version of the name of the labelling property
  * in a CITE Collection's definition.
  *
  * @param collectionDef Definition of the collection's structure.
  */
  def lcLabel(collectionDef: CiteCollectionDef) = {
    collectionDef.labelProperty.property.toLowerCase
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
