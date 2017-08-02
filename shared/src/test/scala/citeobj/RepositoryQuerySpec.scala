package edu.holycross.shot.citeobj
import org.scalatest.FlatSpec
import edu.holycross.shot.cite._



class RepositoryQuerySpec extends FlatSpec {


  val cex = """#!citecollections
URN#Description#Labelling property#Ordering property#License
urn:cite2:hmt:msA.v1:#Pages of the Venetus A manuscriptscript#urn:cite2:hmt:msA.v1.label:#urn:cite2:hmt:msA.v1.sequence:#CC-attribution-share-alike

#!citeproperties
Property#Label#Type#Authority list
urn:cite2:hmt:msA.v1.urn:#URN#Cite2Urn#
urn:cite2:hmt:msA.v1.label:#Label#String#
urn:cite2:hmt:msA.v1.siglum:#Manuscript siglum#String#
urn:cite2:hmt:msA.v1.sequence:#Page sequence#Number#
urn:cite2:hmt:msA.v1.rv:#Recto or Verso#String#recto,verso
urn:cite2:hmt:msA.v1.codex:#Codex URN#Cite2Urn#

#!citedata
siglum#sequence#urn#rv#label#codex
msA#1#urn:cite2:hmt:msA.v1:1r#recto#Marcianus Graecus Z. 454 (= 822) (Venetus A) folio 1r#urn:cite2:hmt:codex:msA
msA#2#urn:cite2:hmt:msA.v1:1v#verso#Marcianus Graecus Z. 454 (= 822) (Venetus A) folio 1v#urn:cite2:hmt:codex:msA
msA#3#urn:cite2:hmt:msA.v1:2r#recto#Marcianus Graecus Z. 454 (= 822) (Venetus A) folio 2r#urn:cite2:hmt:codex:msA
"""

  val repo = CiteCollectionRepository(cex,"#",",")

  val rvProperty = Cite2Urn("urn:cite2:hmt:msA.v1.rv:")
  val seqProperty = Cite2Urn("urn:cite2:hmt:msA.v1.sequence:")
  val codexProperty = Cite2Urn("urn:cite2:hmt:msA.v1.codex:")

  "A CITE Collection repository" should "find citable objects matching any value"  in {
    val rectos = repo.valueEquals("recto")
    assert(rectos.size == 2)

    val seq2 = repo.valueEquals(2)
    assert(seq2.size == 1)

    val codex =Cite2Urn("urn:cite2:hmt:codex:msA")
    val codexA = repo.valueEquals(codex)
    assert(codexA.size == 3)
  }
  it should "find citable objects satisfying matching a specified property"  in {
    val rectos = repo.valueEquals(rvProperty,"recto")
    assert(rectos.size == 2)


    val seq3 = repo.valueEquals(seqProperty,3)
    assert(seq3.size == 1)

    val codexA = Cite2Urn("urn:cite2:hmt:codex:msA")
    val codexApages = repo.valueEquals(codexProperty, codexA)
    assert(codexApages.size == 3)
  }
  it should "throw an exception if types do not match when matching a specified property" in {
    try {
      val badPairing = repo.valueEquals(rvProperty,4)
      fail("Should not have completed search.")
    } catch {
      case coe: CiteObjectException => assert(coe.message == "Type fails: ControlledVocabType does not match value 4")
      case t: Throwable =>  fail("Should have thrown CiteObjectException but threw " + t)
    }
  }


  it should "find citable objects satisfying numeric less than on any value" in {
    val first3 = repo.numericLessThan(4)
    assert(first3.size== 3)
  }
  it should "find citable objects satisfying numeric less than on a specified property" in {
    val first3 = repo.numericLessThan(seqProperty,4)
    assert(first3.size== 3)
  }
  it should "throw an exception if types do not match when matching numeric less" in {
    try {
      val badPairing = repo.numericLessThan(rvProperty,4)
      fail("Should not have completed search.")
    } catch {
      case coe: CiteObjectException => assert(coe.message contains "Type fails: ControlledVocabType does not match value 4")
      case t: Throwable =>  fail("Should have thrown CiteObjectException but threw " + t)
    }
  }


  it should "find citable objects satisfying numeric less than or equal to on any value" in {
    val first3 = repo.numericLessThanOrEqual(3)
    assert(first3.size== 3)

    val firstdec = repo.numericLessThanOrEqual(3.1)
    assert(firstdec.size== 3)
  }
  it should "find citable objects satisfying numeric less than or equal to on a specified property" in {
    val first3 = repo.numericLessThanOrEqual(seqProperty,3)
    assert(first3.size== 3)

    val firstdec = repo.numericLessThanOrEqual(seqProperty,3.1)
    assert(firstdec.size== 3)
  }
  it should "throw an exception if types do not match when matching numeric less than or equal to" in {
    try {
      val badPairing = repo.numericLessThanOrEqual(rvProperty,4)
      fail("Should not have completed search.")
    } catch {
      case coe: CiteObjectException => assert(coe.message contains "Type fails: ControlledVocabType does not match value 4")
      case t: Throwable =>  fail("Should have thrown CiteObjectException but threw " + t)
    }
  }

  it should "find citable objects satisfying numeric greater than on any value" in {
    val match2 = repo.numericGreaterThan(1)
    assert(match2.size== 2)
  }
  it should "find citable objects satisfying numeric greater than on a specified property" in {
    val match2 = repo.numericGreaterThan(seqProperty,1)
    assert(match2.size== 2)
  }
  it should "throw an exception types do not match when matching numeric greater than to" in {
    try {
      val badPairing = repo.numericGreaterThan(rvProperty,4)
      fail("Should not have completed search.")
    } catch {
      case coe: CiteObjectException => assert(coe.message contains "Type fails: ControlledVocabType does not match value 4")
      case t: Throwable =>  fail("Should have thrown CiteObjectException but threw " + t)
    }
  }


  it should "find citable objects satisfying numeric greater than or equal to on any value" in {
    val match2 = repo.numericGreaterThanOrEqual(2.0)
    assert(match2.size== 2)
  }
  it should "find citable objects satisfying numeric greater than or equal to on a specified property" in {
    val match2 = repo.numericGreaterThanOrEqual(seqProperty,2.0)
    assert(match2.size== 2)
  }
  it should "throw an exception when types do not match when matching numeric greater than or equal to" in {
    try {
      val badPairing = repo.numericGreaterThanOrEqual(rvProperty,4)
      fail("Should not have completed search.")
    } catch {
      case coe: CiteObjectException => assert(coe.message contains "Type fails: ControlledVocabType does not match value 4")
      case t: Throwable =>  fail("Should have thrown CiteObjectException but threw " + t)
    }
  }


  it should "find citable objects satisfying numeric range within on any value" in {
    val match2 = repo.numericWithin(2.0,5)
    assert(match2.size== 2)
  }
  it should "find citable objects satisfying numeric range within on a specified property" in {
    val match2 = repo.numericWithin(seqProperty,2.0,5)
    assert(match2.size== 2)
  }
  it should "throw an exception when types do not match when matching numeric range" in {
    try {
      val badPairing = repo.numericWithin(rvProperty,2,4)
      fail("Should not have completed search.")
    } catch {
      case iae: IllegalArgumentException => assert(iae.getMessage() contains "did not match type ControlledVocabType")
      case t: Throwable =>  fail("Should have thrown IllegalArgumentException but threw " + t)
    }
  }

  it should "find citable objects satisfying substring match on any value" in {
    val rectos = repo.stringContains("rect")
    assert(rectos.size == 2)
  }
  it should "find citable objects satisfying substring match on a specified property" in {
    val rectos = repo.stringContains(rvProperty,"rect",true)
    assert(rectos.size == 2)
  }
  it should "throw an exception when types do not match when matching substring" in {
    try {
      val badPairing = repo.stringContains(seqProperty,"rect",true)
      fail("Should not have completed search.")
    } catch {
      case coe: CiteObjectException => assert(coe.message == "Type NumericType did not match value rect.")
      case t: Throwable =>  fail("Should have thrown CiteObjectException but threw " + t)
    }
  }


  it should "find citable objects satisfying case-insensitive match on any value" in {
    val rectos = repo.stringContains("RECt",false)
    assert(rectos.size == 2)
  }
  it should "find citable objects satisfying case-insensitive match on a specified property" in {
    val rectos = repo.stringContains(rvProperty,"RECT",false)
    assert(rectos.size == 2)
  }


  it should "find citable objects satisfying RE match on any value" in {
    val rectos = repo.regexMatch("^rect.*")
    assert(rectos.size == 2)
  }
  it should "find citable objects satisfying RE match on a specified property" in {
    val rectos = repo.regexMatch(rvProperty,"^rect.*")
    assert(rectos.size == 2)
  }

  it should "throw an exception when types do not match when matching RE" in {
    try {
      val badPairing = repo.regexMatch(seqProperty,"^rect.*")
      fail("Should not have completed search.")
    } catch {
      case coe: CiteObjectException => assert(coe.message ==  "Type NumericType did not match value for ^rect.*.")
      case t: Throwable =>  fail("Should have thrown CiteObjectException but threw " + t)
    }
  }


  it should "find citable objects satisfying URN match on any value"  in {
      val codexCollection =Cite2Urn("urn:cite2:hmt:codex:")
      val codexPages = repo.urnMatch(codexCollection)
      assert(codexPages.size == 3)
  }
  it should "find citable objects satisfying URN match on a specified property" in {
    val codexCollection =Cite2Urn("urn:cite2:hmt:codex:")
    val codexPages = repo.urnMatch(codexProperty,codexCollection)
    assert(codexPages.size == 3)
  }
  it should "throw an exception if types do not match when matching URN" in {
    val codexCollection =Cite2Urn("urn:cite2:hmt:codex:")
    try {
      val badPairing = repo.urnMatch(rvProperty,codexCollection)
      fail("Should not have completed search.")
    } catch {
      case coe: CiteObjectException => assert(coe.message == "Type ControlledVocabType did not match value for urn:cite2:hmt:codex:.")
      case t: Throwable =>  fail("Should have thrown CiteObjectException but threw " + t)
    }
  }

  it should "search for specific properties in a repository with multiple collections" in pending

  it should "search for a URN in a collection with both CTS and CITE2 URN properties" in pending

}
