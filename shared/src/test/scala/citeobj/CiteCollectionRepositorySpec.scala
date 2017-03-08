package edu.holycross.shot.citeobj
import org.scalatest.FlatSpec
import edu.holycross.shot.cite._


/**
*/
class CiteCollectionRepositorySpec extends FlatSpec {


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
    val repo = CiteCollectionRepository(dataCollection, catalogInfo)
    repo.catalog match {
      case cat: CiteCatalog => assert(true)
      case _ => fail("Could not find collection catalog.")
    }
  }

  it should "throw an AssertionError if the catalog is empty" in {
    try {
      val repo = CiteCollectionRepository(dataCollection, CiteCatalog(Vector.empty))
    } catch {
      case emptyErr: java.lang.AssertionError => assert(true)
      case er: Throwable => fail("Expected assertion error")
    }
  }

  it should "have a data set" in {
    val repo = CiteCollectionRepository(dataCollection, catalogInfo)
    repo.data match {
      case dataSet: CiteCollectionData => assert(true)
      case _ => fail("Could not find collection data.")
    }
  }


  it should "throw an AssertionError if the data set is empty" in {
    try {
      val repo = CiteCollectionRepository(CiteCollectionData(Vector.empty), catalogInfo)
    } catch {
      case emptyErr: java.lang.AssertionError => assert(true)
      case er: Throwable => fail("Expected assertion error")
    }
  }

  it should "enforce 1<->1 relation of properties in a data collection and property definitions in a catalog" in {
      val repo = CiteCollectionRepository(dataCollection, catalogInfo)

  }

  it should "throw an assertion errror if there is not a 1<->1 relation" in {
    val shortData : Vector[CitePropertyValue] = Vector(
      CitePropertyValue(Cite2Urn("urn:cite2:hmt:speeches.v1.speaker:speech1"),Cite2Urn("urn:cite2:hmt:pers:pers22")),
      CitePropertyValue(Cite2Urn("urn:cite2:hmt:speeches.v1.speaker:speech4"),Cite2Urn("urn:cite2:hmt:pers:pers1")))

    try {
      val repo = CiteCollectionRepository(CiteCollectionData(shortData), catalogInfo)
    } catch {
      case emptyErr: java.lang.AssertionError => assert(true)
      case er: Throwable => fail("Expected assertion error")
    }
  }

  it should "identify all collections in the repository" in {
    val repo = CiteCollectionRepository(dataCollection, catalogInfo)
    val expected = Set(Cite2Urn("urn:cite2:hmt:speeches.v1:"))
    assert (repo.collections ==  expected, s"${repo.collections} did not match ${expected}")
  }

  it should "identify all propoerties in the repository" in {
    val repo = CiteCollectionRepository(dataCollection, catalogInfo)
    val expected = Set(
      Cite2Urn("urn:cite2:hmt:speeches.v1.speaker:"), Cite2Urn("urn:cite2:hmt:speeches.v1.passage:"),
      Cite2Urn("urn:cite2:hmt:speeches.v1.sequence:"),
      Cite2Urn("urn:cite2:hmt:speeches.v1.label:")
    )
    assert (repo.properties == expected, s"${repo.properties} did not match ${expected}")
  }

  it should "throw an exception if any defined properties are missing for an object in a collection"  in {
    val shortData : Vector[CitePropertyValue] = Vector(
      CitePropertyValue(Cite2Urn("urn:cite2:hmt:speeches.v1.speaker:speech1"),Cite2Urn("urn:cite2:hmt:pers:pers22")),
      CitePropertyValue(Cite2Urn("urn:cite2:hmt:speeches.v1.label:speech1"),"Speech 1"),
      CitePropertyValue(Cite2Urn("urn:cite2:hmt:speeches.v1.sequence:speech1"),1)
    )


    val badData = CiteCollectionData(shortData)
    try {
      val repo = CiteCollectionRepository(badData, catalogInfo)
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
    } catch {
      case e: java.lang.AssertionError => assert(true)
      case _ : Throwable => fail("Expected an assertion error")
    }
  }

  it should "validate controlled vocab lists" in {

    val props: Vector[CitePropertyDef]   = Vector(
      CitePropertyDef(Cite2Urn("urn:cite2:hmt:speeches.v1.speaker:"), "speaker", Cite2UrnType),
      CitePropertyDef(Cite2Urn("urn:cite2:hmt:speeches.v1.passage:"),"text passage",CtsUrnType),
      CitePropertyDef(Cite2Urn("urn:cite2:hmt:speeches.v1.rating:"),"idiosyncratic rating",ControlledVocabType,Vector("good","bad","ugly"))
    )

    val collectionDefV : Vector[CiteCollectionDef] = Vector (
      CiteCollectionDef(Cite2Urn("urn:cite2:hmt:speeches.v1:"), "speeches in the Iliad", propertyDefs = props, orderingProperty = Some(Cite2Urn("urn:cite2:hmt:speeches.v1.sequence:")))
    )

    val dataV : Vector[CitePropertyValue] = Vector(
      CitePropertyValue(Cite2Urn("urn:cite2:hmt:speeches.v1.speaker:speech1"),Cite2Urn("urn:cite2:hmt:pers:pers22")),
      CitePropertyValue(Cite2Urn("urn:cite2:hmt:speeches.v1.label:speech1"),"Speech 1"),
      CitePropertyValue(Cite2Urn("urn:cite2:hmt:speeches.v1.passage:speech1"),CtsUrn("urn:cts:greekLit:tlg0012.tlg001:1.26-1.32")),
      CitePropertyValue(Cite2Urn("urn:cite2:hmt:speeches.v1.sequence:speech1"),1),
      CitePropertyValue(Cite2Urn("urn:cite2:hmt:speeches.v1.rating:speech1"),"good"),


      CitePropertyValue(Cite2Urn("urn:cite2:hmt:speeches.v1.speaker:speech4"),Cite2Urn("urn:cite2:hmt:pers:pers1")),
        CitePropertyValue(Cite2Urn("urn:cite2:hmt:speeches.v1.label:speech4"),"Speech 4"),
        CitePropertyValue(Cite2Urn("urn:cite2:hmt:speeches.v1.passage:speech4"),CtsUrn("urn:cts:greekLit:tlg0012.tlg001:1.85-1.91")),
        CitePropertyValue(Cite2Urn("urn:cite2:hmt:speeches.v1.sequence:speech4"),4),
        CitePropertyValue(Cite2Urn("urn:cite2:hmt:speeches.v1.rating:speech4"),"ugly")
      )

      val dataCollection = CiteCollectionData(dataV)
      val catalogInfo = CiteCatalog(collectionDefV)
      val repo = CiteCollectionRepository(dataCollection, catalogInfo)

      assert(repo.data.collections.size == repo.catalog.size, s"${repo.data.data.size} collections in data, but ${repo.catalog.size}in catalog")
  }
}
