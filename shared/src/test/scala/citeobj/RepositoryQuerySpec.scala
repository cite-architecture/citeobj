package edu.holycross.shot.citeobj
import org.scalatest.FlatSpec
import edu.holycross.shot.cite._



class RepositoryLanguageSpec extends FlatSpec {


  val cex = """#!citecatalog
collection#urn:cite2:hmt:msA.v1:#Pages of the Venetus A manuscriptscript#urn:cite2:hmt:msA.v1.label:#urn:cite2:hmt:msA.v1.sequence:#CC-attribution-share-alike

property#urn:cite2:hmt:msA.v1.urn:#URN#Cite2Urn#
property#urn:cite2:hmt:msA.v1.label:#Label#String#
property#urn:cite2:hmt:msA.v1.siglum:#Manuscript siglum#String#
property#urn:cite2:hmt:msA.v1.sequence:#Page sequence#Number#
property#urn:cite2:hmt:msA.v1.rv:#Recto or Verso#String#recto,verso
property#urn:cite2:hmt:msA.v1.codex:#Codex URN#Cite2Urn#

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
  it should "find citable objects satisfying matching a specified property"  in pending
  it should "throw an exception types do not match when matching a specified property" in pending


  it should "find citable objects satisfying numeric less than on any value" in {
    val first3 = repo.numericLessThan(4)
    assert(first3.size== 3)
  }
  it should "find citable objects satisfying numeric less than on a specified property" in {
    val first3 = repo.numericLessThan(seqProperty,4)
    assert(first3.size== 3)
  }
  it should "throw an exception types do not match when matching numeric less" in pending


  it should "find citable objects satisfying numeric less than or equal to on any value" in pending
  it should "find citable objects satisfying numeric greater than on any value" in pending
  it should "find citable objects satisfying numeric greater than or equal to on any value" in pending
  it should "find citable objects satisfying numeric range within on any value" in pending
  it should "find citable objects satisfying substring match on any value" in pending
  it should "find citable objects satisfying case-insensitive match on any value" in pending
  it should "find citable objects satisfying RE match on any value" in pending




  it should "find citable objects satisfying numeric less than or equal to on a specified property" in pending
  it should "throw an exception types do not match when matching numeric less than or equal to" in pending

  it should "find citable objects satisfying numeric greater than on a specified property" in pending
  it should "throw an exception types do not match when matching numeric greater than to" in pending

  it should "find citable objects satisfying numeric greater than or equal to on a specified property" in pending
  it should "throw an exception when types do not match when matching numeric greater than or equal to" in pending


  it should "find citable objects satisfying numeric range within on a specified property" in pending
  it should "throw an exception when types do not match when matching numeric range" in pending


  it should "find citable objects satisfying substring match on a specified property" in pending
  it should "throw an exception when types do not match when matching substring" in pending


  it should "find citable objects satisfying case-insensitive match on a specified property" in pending
  it should "throw an exception when types do not match when matching case-insensitive substring" in pending


  it should "find citable objects satisfying RE match on a specified property" in pending
  it should "throw an exception when types do not match when matching RE" in pending


}
