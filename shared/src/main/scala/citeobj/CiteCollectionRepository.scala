package edu.holycross.shot.citeobj

import edu.holycross.shot.cite._

/** A cataloged collection of citable data.
* In initialization, the constructor validates every
* object in the data against the structure defined
* in the catalog.
*
* @param data Collection of data values.
* @param catalog Documentation of the structure of each collection.
*/
case class CiteCollectionRepository (data: CiteCollectionData, catalog: CiteCatalog) {
  assert(data.isEmpty == false)
  assert(catalog.isEmpty == false)
  // enforce 1<->1 relation of properties
  // (and therefore collections, too) between
  // catalog and data
  assert(data.properties == catalog.properties, s"failed when comparing ${data.properties.size} data properties to ${catalog.properties.size} catalog properties.  Data properties: \n${data.properties}\n vs catalog properties: \n${catalog.properties}")


  /** Construct a citable object for an identifying URN.
  *
  * @param obj URN uniquely identifying a single object.
  */
  def citableObject(objUrn: Cite2Urn, labelPropertyUrn : Cite2Urn): CiteObject = {
    val objectData = data ~~ objUrn
    val labeller = objectData ~~ labelPropertyUrn
    assert(labeller.data.size == 1)
    val labelProperty = labeller.data(0)
    val remainingProps = objectData -- labeller

    CiteObject(objUrn, labelProperty.propertyValue.toString,remainingProps.data )

  }


  /** True if the given [[CiteObject]] validates against the collection's definition.
  *
  * @param citeObj Citable object to evaluate.
  * @param collectionDef Collection definition to use in evaulating object.
  */
  def objMatchesCatalog(citeObj: CiteObject,collectionDef: CiteCollectionDef): Boolean = {
    //println("DOES OBJ MATCH DEF?")
    //println(citeObj)
    //println(collectionDef)

    //
    //ADD ORDERING PROPERTY IF PRESENT TO LIST OF
    // PROPERTIES TO CHECK
    val catalogSet = {
      collectionDef.orderingProperty match {
        case oprop : Some[Cite2Urn] => {
          collectionDef.propertyDefs :+ CitePropertyDef(oprop.get,"sequence",NumericType,Vector.empty)
        }
        case None => collectionDef.propertyDefs// :+ CitePropertyDef(collectionDef.urn.addProperty("urn"),"URN",Cite2UrnType,Vector.empty)
      }
    }

    val catalogPropUrns =  catalogSet.map(_.urn)

    val objUrnSet = citeObj.propertyList.map(_.urn).toSet
    val catUrnSet = catalogPropUrns.toSet
    //assert(objUrnSet == catUrnSet)
    println("OBJURNSET ")
    for (o <- objUrnSet) {
      println("\t" + o)
    }
    println("CATURNSET " + catUrnSet)


    assert(catalogSet.size ==
       citeObj.propertyList.size, s"for ${citeObj.urn}, catalog defines ${collectionDef.propertyDefs.size} properties but found ${citeObj.propertyList.size} in \n${citeObj.propertyList.map(_.urn)}")



    for (p <- citeObj.propertyList) {

      assert(catalogPropUrns.contains(p.urn.dropSelector))
      val propDef = catalogSet.filter(_.urn == p.urn.dropSelector)
      assert(propDef.size == 1)

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
    assert(collectionCatalog.size == 1)

    val collectionDefinition = collectionCatalog.collections(0)

    val asCitableObject = citableObject(objectUrn,collectionDefinition.labelProperty)

    objMatchesCatalog(asCitableObject,collectionDefinition)
  }

  //Validate contents of all collections in the repository.
  for (c <- data.objects) {
    assert(objectValidates(c),"Failed to validate object " + c)
  }


  /** Set of all collections in the repository,
  * identified by URN.
  */
  def collections = {
    catalog.urns
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
