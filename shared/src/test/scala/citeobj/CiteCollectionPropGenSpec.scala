package edu.holycross.shot.citeobj
import org.scalatest.FlatSpec
import edu.holycross.shot.cite._


/**
*/
class CiteCollectionPropGenSpec extends FlatSpec {

  val cex = """#!citecollections
URN#Description#Labelling property#Ordering property#License
urn:cite2:hmt:msA.v1:#Pages of the Venetus A manuscriptscript#urn:cite2:hmt:msA.v1.label:#urn:cite2:hmt:msA.v1.sequence:#CC-attribution-share-alike

#!citeproperties
Property#Label#Type#Authority list
urn:cite2:hmt:msA.v1.urn:#URN#Cite2Urn#
urn:cite2:hmt:msA.v1.label:#Label#String#
urn:cite2:hmt:msA.v1.siglum:#Manuscript siglum#String#
urn:cite2:hmt:msA.v1.sequence:#Page sequence#Number#
//urn:cite2:hmt:msA.v1.rv:#Recto or Verso#String#recto,verso
urn:cite2:hmt:msA.v1.rv:#Recto or Verso#String#
urn:cite2:hmt:msA.v1.codex:#Codex URN#Cite2Urn#

#!citedata
siglum#sequence#urn#rv#label#codex
msA#1#urn:cite2:hmt:msA.v1:1r#recto#Marcianus Graecus Z. 454 (= 822) (Venetus A) folio 1r#urn:cite2:hmt:codex:msA
msA#2#urn:cite2:hmt:msA.v1:1v#verso#Marcianus Graecus Z. 454 (= 822) (Venetus A) folio 1v#urn:cite2:hmt:codex:msA
msA#3#urn:cite2:hmt:msA.v1:2r#recto#Marcianus Graecus Z. 454 (= 822) (Venetus A) folio 2r#urn:cite2:hmt:codex:msA
"""


  val dataV : Vector[CitePropertyValue] = Vector(

    CitePropertyValue(Cite2Urn("urn:cite2:hmt:speeches.v1.speaker:speech1"),Cite2Urn("urn:cite2:hmt:pers:pers22")),
    CitePropertyValue(Cite2Urn("urn:cite2:hmt:speeches.v1.label:speech1"),"Speech 1"),
    CitePropertyValue(Cite2Urn("urn:cite2:hmt:speeches.v1.passage:speech1"),CtsUrn("urn:cts:greekLit:tlg0012.tlg001:1.26-1.32")),
    CitePropertyValue(Cite2Urn("urn:cite2:hmt:speeches.v1.sequence:speech1"),1),


    CitePropertyValue(Cite2Urn("urn:cite2:hmt:speeches.v1.speaker:speech4"),Cite2Urn("urn:cite2:hmt:pers:pers1")),
    CitePropertyValue(Cite2Urn("urn:cite2:hmt:speeches.v1.label:speech4"),"Speech 4"),
    CitePropertyValue(Cite2Urn("urn:cite2:hmt:speeches.v1.passage:speech4"),CtsUrn("urn:cts:greekLit:tlg0012.tlg001:1.85-1.91")),
    CitePropertyValue(Cite2Urn("urn:cite2:hmt:speeches.v1.sequence:speech4"),4)
  )


  val props: Vector[CitePropertyDef]   = Vector(
    CitePropertyDef(Cite2Urn("urn:cite2:hmt:speeches.v1.speaker:"), "speaker", Cite2UrnType),
    CitePropertyDef(Cite2Urn("urn:cite2:hmt:speeches.v1.passage:"),"text passage",CtsUrnType)
  )
  val collectionDefV : Vector[CiteCollectionDef] = Vector(
    CiteCollectionDef(Cite2Urn("urn:cite2:hmt:speeches.v1:"), "speeches in the Iliad", propertyDefs = props, orderingProperty = Some(Cite2Urn("urn:cite2:hmt:speeches.v1.sequence:")))
  )


  val dataCollection = CiteCollectionData(dataV)
  val catalogInfo = CiteCatalog(collectionDefV)


  "A Cite Collection repository" should "have a catalog" in {
    val repo = CiteCollectionRepository(cex,"#",",")
    repo.catalog match {
      case cat: CiteCatalog => assert(true)
      case _ => fail("Could not find collection catalog.")
    }
  }
}

/*
  it should "throw an AssertionError if the catalog is empty" in {
    try {
      val repo = CiteCollectionRepository(dataCollection, CiteCatalog(Vector.empty))
      fail("Should not have created repository")
    } catch {
      case emptyErr: java.lang.AssertionError => assert(true)
      case er: Throwable => fail("Expected assertion error")
    }
  }

  it should "have a data set" in {
    val repo = CiteCollectionRepository(cex,"#",",")
    repo.data match {
      case dataSet: CiteCollectionData => assert(true)
      case _ => fail("Could not find collection data.")
    }
  }


  it should "allow an empty data set" in {
    val repo = CiteCollectionRepository(CiteCollectionData(Vector.empty), catalogInfo)
    repo.data match {
      case dataSet: CiteCollectionData => assert(true)
      case _ => fail("Could not find collection data.")
    }
  }

  it should "throw an assertion errror if there is not a 1<->1 relation of properties from  instantiated collections to catalog " in pending
{
    val shortData : Vector[CitePropertyValue] = Vector(
      CitePropertyValue(Cite2Urn("urn:cite2:hmt:speeches.v1.speaker:speech1"),Cite2Urn("urn:cite2:hmt:pers:pers22")),
      CitePropertyValue(Cite2Urn("urn:cite2:hmt:speeches.v1.speaker:speech4"),Cite2Urn("urn:cite2:hmt:pers:pers1")))

    try {
      val repo = CiteCollectionRepository(CiteCollectionData(shortData), catalogInfo)
      fail("Should not have created repository")
    } catch {
      case emptyErr: java.lang.AssertionError => assert(true)
      case er: Throwable => fail("Expected assertion error")
    }
  }

  it should "identify all collections in the repository" in {
    val repo = CiteCollectionRepository(cex,"#",",")
    val expected = Set(Cite2Urn("urn:cite2:hmt:msA.v1:"))
    assert (repo.collections ==  expected, s"${repo.collections} did not match ${expected}")
  }


  it should "identify all propoerties in the repository" in {
    val repo = CiteCollectionRepository(cex,"#",",")
    val expected = Set(
      Cite2Urn("urn:cite2:hmt:msA.v1.urn:"),
      Cite2Urn("urn:cite2:hmt:msA.v1.siglum:"),
      Cite2Urn("urn:cite2:hmt:msA.v1.label:"),
      Cite2Urn("urn:cite2:hmt:msA.v1.rv:"),
      Cite2Urn("urn:cite2:hmt:msA.v1.sequence:"),
      Cite2Urn("urn:cite2:hmt:msA.v1.codex:")
    )
    assert (repo.properties == expected, s"${repo.properties} did not match ${expected}")
  }

  it should "throw an exception if any defined properties are missing for an
  object in a collection"  in pending

  {
    val shortData : Vector[CitePropertyValue] = Vector(
      CitePropertyValue(Cite2Urn("urn:cite2:hmt:speeches.v1.speaker:speech1"),Cite2Urn("urn:cite2:hmt:pers:pers22")),
      CitePropertyValue(Cite2Urn("urn:cite2:hmt:speeches.v1.label:speech1"),"Speech 1"),
      CitePropertyValue(Cite2Urn("urn:cite2:hmt:speeches.v1.sequence:speech1"),1)
    )


    val badData = CiteCollectionData(shortData)
    try {
      val repo = CiteCollectionRepository(badData, catalogInfo)
      fail("Should not have created repository")
    } catch {
      case e: java.lang.AssertionError => assert(true)
      case _ : Throwable => fail("Expected an assertion error")
    }

  }

  it should "throw an Exception if any values are not of the proper type"  in {

    val badDataType : Vector[CitePropertyValue] = Vector(
        CitePropertyValue(Cite2Urn("urn:cite2:hmt:speeches.v1.speaker:speech1"),Cite2Urn("urn:cite2:hmt:pers:pers22")),
        CitePropertyValue(Cite2Urn("urn:cite2:hmt:speeches.v1.label:speech1"),1),
        CitePropertyValue(Cite2Urn("urn:cite2:hmt:speeches.v1.passage:speech1"),CtsUrn("urn:cts:greekLit:tlg0012.tlg001:1.26-1.32")),
        CitePropertyValue(Cite2Urn("urn:cite2:hmt:speeches.v1.sequence:speech1"),1)
      )


    val badData = CiteCollectionData(badDataType)
    try {
      val repo = CiteCollectionRepository(badData, catalogInfo)
      fail("Should not have created repository")
    } catch {
      case iae: IllegalArgumentException => assert(true)
      case t : Throwable => fail("Expected an assertion error but got" + t)
    }
  }

  it should "throw an exception if constraints of controlled vocab lists are violated" in {

    val badVocab =  """#!citecollections
urn:cite2:hmt:msA.v1:#Pages of the Venetus A manuscriptscript#urn:cite2:hmt:msA.v1.label:#urn:cite2:hmt:msA.v1.sequence:#CC-attribution-share-alike

#!citeproperties
urn:cite2:hmt:msA.v1.urn:#URN#Cite2Urn#
urn:cite2:hmt:msA.v1.label:#Label#String#
urn:cite2:hmt:msA.v1.siglum:#Manuscript siglum#String#
urn:cite2:hmt:msA.v1.sequence:#Page sequence#Number#
urn:cite2:hmt:msA.v1.rv:#Recto or Verso#String#recto,verso
urn:cite2:hmt:msA.v1.codex:#Codex URN#Cite2Urn#

#!citedata
siglum#sequence#urn#rv#label#codex
msA#1#urn:cite2:hmt:msA.v1:1r#rectorated#Marcianus Graecus Z. 454 (= 822) (Venetus A) folio 1r#urn:cite2:hmt:codex:msA
"""
    try {
      val repo = CiteCollectionRepository(badVocab,"#",",")
      fail("Should not have created repository")
    } catch {
      case coe: CiteObjectException => assert(coe.message == "Value rectorated is not in controlled vocabulary recto, verso")
      case t: Throwable => fail(s"Should have thrown CiteObjectException, but threw ${t}")
    }
  }






      it should "validate controlled vocab lists" in {
        val goodVocab =  """#!citecollections
urn:cite2:hmt:msA.v1:#Pages of the Venetus A manuscriptscript#urn:cite2:hmt:msA.v1.label:#urn:cite2:hmt:msA.v1.sequence:#CC-attribution-share-alike

#!citeproperties
urn:cite2:hmt:msA.v1.urn:#URN#Cite2Urn#
urn:cite2:hmt:msA.v1.label:#Label#String#
urn:cite2:hmt:msA.v1.siglum:#Manuscript siglum#String#
urn:cite2:hmt:msA.v1.sequence:#Page sequence#Number#
urn:cite2:hmt:msA.v1.rv:#Recto or Verso#String#recto,verso
urn:cite2:hmt:msA.v1.codex:#Codex URN#Cite2Urn#

#!citedata
siglum#sequence#urn#rv#label#codex
msA#1#urn:cite2:hmt:msA.v1:1r#recto#Marcianus Graecus Z. 454 (= 822) (Venetus A) folio 1r#urn:cite2:hmt:codex:msA
"""

    val repo = CiteCollectionRepository(goodVocab,"#",",")
    val obj = repo.citableObject(Cite2Urn("urn:cite2:hmt:msA.v1:1r"))
    assert(obj.propertyList.filter(_.urn == Cite2Urn("urn:cite2:hmt:msA.v1.rv:1r"))(0).propertyValue == "recto")
  }

  it should "be able to construct a repository from CEX source" in {
    val repo = CiteCollectionRepository(cex,"#",",")
    repo match {
      case ccr: CiteCollectionRepository => assert(true)
      case _ => fail("Should have created CiteCollectionRepository")
    }
  }

  it should "find the value of a property identified by URN" in {
    val repo = CiteCollectionRepository(cex,"#",",")
    val propertyUrn = Cite2Urn("urn:cite2:hmt:msA.v1.rv:1r")

    assert(repo.propertyValue(propertyUrn) == "recto")
  }


*/
