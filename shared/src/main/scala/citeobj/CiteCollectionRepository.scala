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
  assert(data.properties == catalog.properties, s"failed when comparing ${data.properties.size} data properties to ${catalog.properties.size} catalog properties")


  /** Construct a citable object for a an identifying URN.
  * Returns None if a valid [[CiteObject]] cannot be
  * constructed.
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

  def objMatchesCatalog(citeObj: CiteObject,collectionDef: CiteCollectionDef): Boolean = {

    //ADD ORDERING PROPERTY IF PRESENT TO LIST OF
    // PROPERTIES TO CHECK
    val catalogSet = {
      collectionDef.orderingProperty match {
        case oprop : Some[Cite2Urn] => {
          collectionDef.propertyDefs :+ CitePropertyDef(oprop.get,"sequence",NumericType,Vector.empty)
        }
        case None => collectionDef.propertyDefs
      }
    }




    assert(catalogSet.size ==
       citeObj.propertyList.size, s"for ${citeObj.urn}, catalog defines ${collectionDef.propertyDefs.size} properties but found ${citeObj.propertyList.size}")

    val catalogPropUrns =  catalogSet.map(_.urn)
    for (p <- citeObj.propertyList) {

      println(p)
      assert(catalogPropUrns.contains(p.urn.dropSelector))

      // get type from catalog def
      val expectedType = {
        val propDef = catalogSet.filter(_.urn == p.urn.dropSelector)
        assert(propDef.size == 1)
        propDef(0).propertyType
      }

      assert(CiteCollectionRepository.typesMatch(p.propertyValue,expectedType ),s"For ${p.urn}, ${p.propertyValue} did not match ${expectedType}")
    }

    true
  }
  /** True if we can construct a valid object for a URN,
  * confirm 1<->1 relation of cataloged properties and
  * actual properties, and verify type of each property value.
  *
  * @param obj URN uniquely identifyiing an object.  It is an assertion exception if the URN does not match 1 and only 1 entry in the [[catalog]].
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
    assert(objectValidates(c))
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



object CiteCollectionRepository {

  def typesMatch(propertyVal: Any, expected: CitePropertyType) : Boolean = {
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
      case _ => false // unimplemented type??

    }
  }
}

  /*()




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
