package edu.holycross.shot.citeobj
import org.scalatest.FlatSpec
import edu.holycross.shot.cite._


/**
*/
class CiteCatalogSpec extends FlatSpec {

  val imageColl = """
#!citecollections
URN#Description#Labelling property#Ordering property#License
urn:cite2:hmt:vaimg.2017a:#Images of the Venetus A manuscriptscript#urn:cite2:hmt:vaimg.2017a.caption:##CC-attribution-share-alike

#!citeproperties
Property#Label#Type#Authority list
urn:cite2:hmt:vaimg.2017a.urn:#URN#Cite2Urn#
urn:cite2:hmt:vaimg.2017a.caption:#Caption#String#
urn:cite2:hmt:vaimg.2017a.rights:#Rights#String#
"""

  val imageCollUrn = Cite2Urn("urn:cite2:hmt:vaimg.2017a:")

  val propsVector: Vector[CitePropertyDef] = Vector(
    CitePropertyDef(Cite2Urn("urn:cite2:hmt:speeches.v1.passage:"),"Text passage",CtsUrnType),
    CitePropertyDef(Cite2Urn("urn:cite2:hmt:speeches.v1.speaker:"),"Speaker",Cite2UrnType),
    CitePropertyDef(Cite2Urn("urn:cite2:hmt:speeches.v1.sequence:"),"Sequence",NumericType)
  )


  val ccDef = CiteCollectionDef(
    Cite2Urn("urn:cite2:hmt:speeches.v1:"),
    "Speeches in the Iliad",
    propsVector,
    labellingProperty = None,
    orderingProperty = Some(Cite2Urn("urn:cite2:hmt:speeches.v1.sequence:")),
    license = "CC attribution-share-alike"
  )


  "The catalog of a CITE data collection" should "list all cataloged properties" in {
    val catalog = CiteCatalog(Vector(ccDef))
    val expectedProperties = Set(Cite2Urn("urn:cite2:hmt:speeches.v1.passage:"), Cite2Urn("urn:cite2:hmt:speeches.v1.speaker:"), Cite2Urn("urn:cite2:hmt:speeches.v1.sequence:"),
    Cite2Urn("urn:cite2:hmt:speeches.v1.label:")
  )

    assert (catalog.properties == expectedProperties)
  }

  it should "list all cataloged collections" in {
    val catalog = CiteCatalog(Vector(ccDef))
    val expectedCollections = Set(Cite2Urn("urn:cite2:hmt:speeches.v1:"))
    assert (catalog.urns == expectedCollections)
  }
  it should "support urn twiddling" in {
    val catalog = CiteCatalog(Vector(ccDef))

    val speakerProperty = Cite2Urn("urn:cite2:hmt:speeches.v1.speaker:")
    val speakerCatalog = catalog ~~ speakerProperty
    assert (speakerCatalog.size == 1)

    val speechCollection =  Cite2Urn("urn:cite2:hmt:speeches.v1:")
    val speechCatalog = catalog ~~ speechCollection
  }

  it should "support selecting a collection definition by collection URN" in {
    val catalog = CiteCatalog(Vector(ccDef))
    val collectionDef = catalog.collection(Cite2Urn("urn:cite2:hmt:speeches.v1:"))
    collectionDef match {
      case cdef : Option[CiteCollectionDef] => assert(true)
      case _ => fail("Should have found a CiteCollectionDef")
    }
  }

  it should "identify if a collection is ordered" in {
    val catalog = CiteCatalog(Vector(ccDef))
    assert(catalog.isOrdered(Cite2Urn("urn:cite2:hmt:speeches.v1:")))
  }


  it should "report false for unordered collection" in {

    val cat = CiteCatalog(imageColl)
    assert(cat.isOrdered(imageCollUrn) == false)
  }

  it should "do throw a CiteObjectException if asked for ordering on a non-existent urn" in {
    val unCataloged = Cite2Urn("urn:cite2:hmt:NOCOLLECTION.2017a:")
    val cat = CiteCatalog(imageColl)
    try {
      val ordering = cat.isOrdered(unCataloged)
      fail("Should not have tolerated request")
    } catch {
      case coe: CiteObjectException => assert(coe.message == "No collection urn:cite2:hmt:NOCOLLECTION.2017a: cataloged.")
      case _ : Throwable => fail("Should have thrown CiteObjectException")

    }
  }



  it should "find the definition for a given property" in {
    val catalog = CiteCatalog(Vector(ccDef))
    val singleValue = Cite2Urn("urn:cite2:hmt:speeches.v1.speaker:pers1")
    val pdef = catalog.propertyDefinition(singleValue)
    assert(pdef.get.propertyType == Cite2UrnType)
    assert(pdef.get.label == "Speaker")
  }

  it should "create a collection from a larger CEX source" in {
    val noData = """#!citelibrary
license#public domain
name#Demo library
urn#urn:cite2:cex:democex.2017a:test

// Library has two collections:
#!citecollections
URN#Description#Labelling property#Ordering property#License
// 1. Text-bearing surfaces:
urn:cite2:hmt:msA.v1:#Pages of the Venetus A manuscripts#urn:cite2:hmt:msA.v1.label:#urn:cite2:hmt:msA.v1.sequence:#CC-attribution-share-alike
// 2. Documentary images:
urn:cite2:hmt:vaimg.2017a:#Images of the Venetus A#urn:cite2:hmt:msA.v1.label:##CC-attribution-share-alike

#!citeproperties
Property#Label#Type#Authority list

// pages
urn:cite2:hmt:msA.v1.urn:#URN#Cite2Urn#
urn:cite2:hmt:msA.v1.label:#Label#String#
urn:cite2:hmt:msA.v1.siglum:#Manuscript siglum#String#
urn:cite2:hmt:msA.v1.sequence:#Page sequence#Number#
urn:cite2:hmt:msA.v1.rv:#Recto or Verso#String#recto,verso
urn:cite2:hmt:msA.v1.codex:#Codex URN#Cite2Urn#

// images
urn:cite2:hmt:vaimg.2017a.urn:#URN#Cite2Urn#
urn:cite2:hmt:vaimg.2017a.caption:#Caption#String#
urn:cite2:hmt:vaimg.2017a.rights:#Rights#String#
"""
  val cat = CiteCatalog(noData)
  assert(cat.size == 2)
  }



  it should "serialize to CEX" in {
    val bigLib = """#!citelibrary
license#public domain
name#Demo library
urn#urn:cite2:cex:democex.2017a:test

#!citecollections
URN#Description#Labelling property#Ordering property#License
urn:cite2:hmt:msA.v1:#Pages of the Venetus A manuscripts#urn:cite2:hmt:msA.v1.label:#urn:cite2:hmt:msA.v1.sequence:#CC-attribution-share-alike
urn:cite2:hmt:vaimg.2017a:#Images of the Venetus A#urn:cite2:hmt:msA.v1.label:##CC-attribution-share-alike

#!citeproperties
Property#Label#Type#Authority list
urn:cite2:hmt:msA.v1.urn:#URN#Cite2Urn#
urn:cite2:hmt:msA.v1.label:#Label#String#
urn:cite2:hmt:msA.v1.siglum:#Manuscript siglum#String#
urn:cite2:hmt:msA.v1.sequence:#Page sequence#Number#
urn:cite2:hmt:msA.v1.rv:#Recto or Verso#String#recto,verso
urn:cite2:hmt:msA.v1.codex:#Codex URN#Cite2Urn#

#!citeproperties
Property#Label#Type#Authority list
urn:cite2:hmt:vaimg.2017a.urn:#URN#Cite2Urn#
urn:cite2:hmt:vaimg.2017a.caption:#Caption#String#
urn:cite2:hmt:vaimg.2017a.rights:#Rights#String#
"""

    val justCollections = """#!citecollections
URN#Description#Labelling property#Ordering property#License
urn:cite2:hmt:msA.v1:#Pages of the Venetus A manuscripts#urn:cite2:hmt:msA.v1.label:#urn:cite2:hmt:msA.v1.sequence:#CC-attribution-share-alike
urn:cite2:hmt:vaimg.2017a:#Images of the Venetus A#urn:cite2:hmt:msA.v1.label:##CC-attribution-share-alike

#!citeproperties
Property#Label#Type#Authority list
urn:cite2:hmt:msA.v1.urn:#URN#Cite2Urn#
urn:cite2:hmt:msA.v1.label:#Label#String#
urn:cite2:hmt:msA.v1.siglum:#Manuscript siglum#String#
urn:cite2:hmt:msA.v1.sequence:#Page sequence#Number#
urn:cite2:hmt:msA.v1.rv:#Recto or Verso#String#recto,verso
urn:cite2:hmt:msA.v1.codex:#Codex URN#Cite2Urn#

#!citeproperties
Property#Label#Type#Authority list
urn:cite2:hmt:vaimg.2017a.urn:#URN#Cite2Urn#
urn:cite2:hmt:vaimg.2017a.caption:#Caption#String#
urn:cite2:hmt:vaimg.2017a.rights:#Rights#String#"""

    val catalog = CiteCatalog(bigLib)
    val cexized:String = catalog.cex("#")
    assert( cexized.replaceAll("[\n ]+"," ") == justCollections.replaceAll("[\n ]+"," ") )

    val otherDelim:String = catalog.cex("\t")
    assert( otherDelim.replaceAll("[\n ]+"," ") == justCollections.replaceAll("#","\t").replaceAll("[\n ]+"," ").replaceAll("\t!","#!") )
  }

  it should "return a citedata header line for a collection" in {
    val bigLib = """#!citelibrary
license#public domain
name#Demo library
urn#urn:cite2:cex:democex.2017a:test

#!citecollections
URN#Description#Labelling property#Ordering property#License
urn:cite2:hmt:msA.v1:#Pages of the Venetus A manuscripts#urn:cite2:hmt:msA.v1.label:#urn:cite2:hmt:msA.v1.sequence:#CC-attribution-share-alike
urn:cite2:hmt:vaimg.2017a:#Images of the Venetus A#urn:cite2:hmt:msA.v1.label:##CC-attribution-share-alike

#!citeproperties
Property#Label#Type#Authority list
urn:cite2:hmt:msA.v1.urn:#URN#Cite2Urn#
urn:cite2:hmt:msA.v1.label:#Label#String#
urn:cite2:hmt:msA.v1.siglum:#Manuscript siglum#String#
urn:cite2:hmt:msA.v1.sequence:#Page sequence#Number#
urn:cite2:hmt:msA.v1.rv:#Recto or Verso#String#recto,verso
urn:cite2:hmt:msA.v1.codex:#Codex URN#Cite2Urn#

#!citeproperties
Property#Label#Type#Authority list
urn:cite2:hmt:vaimg.2017a.urn:#URN#Cite2Urn#
urn:cite2:hmt:vaimg.2017a.caption:#Caption#String#
urn:cite2:hmt:vaimg.2017a.rights:#Rights#String#
"""
    val expectedHeader1:String = "urn#label#siglum#sequence#rv#codex"
    val collectionUrn:Cite2Urn = Cite2Urn("urn:cite2:hmt:msA.v1:")
    val catalog = CiteCatalog(bigLib)

    assert ( catalog.cexDataHeader(collectionUrn, "#") == expectedHeader1 )
    assert ( catalog.cexDataHeader(collectionUrn) == expectedHeader1 )
    assert ( catalog.cexDataHeader(collectionUrn, "\t") == expectedHeader1.replaceAll("#","\t") )


  }

}
