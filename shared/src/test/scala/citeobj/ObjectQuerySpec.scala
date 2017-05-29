package edu.holycross.shot.citeobj
import org.scalatest.FlatSpec
import edu.holycross.shot.cite._

import edu.holycross.shot.cite._

/**
*/
class ObjectQuerySpec extends FlatSpec {

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
  val citeObjects = repo.citableObjects
  val oneRecto = citeObjects(0)
  val oneVerso = citeObjects(1)

  val rvProperty = Cite2Urn("urn:cite2:hmt:msA.v1.rv:")
  val seqProperty = Cite2Urn("urn:cite2:hmt:msA.v1.sequence:")

  "A CiteObject" should "report boolean result of matching any value"  in {
    assert(oneRecto.valueEquals("recto"))
    assert(oneRecto.valueEquals("verso") == false)
    assert(oneRecto.valueEquals(1))
    assert(oneRecto.valueEquals(2) == false)
  }


  it should "report boolean result of numeric less than on any value" in {
    assert(oneRecto.numericLessThan(10))
    assert(oneRecto.numericLessThan(1.5))
  }

  it should "report boolean result of numeric less than or equal to on any value" in {
    assert(oneRecto.numericLessThanOrEqual(1))
    assert(oneRecto.numericLessThanOrEqual(1.0))
  }
  it should "report boolean result of numeric greater than on any value" in {
    assert(oneVerso.numericGreaterThan(1))
    assert(oneVerso.numericGreaterThan(1.9))
  }
  it should "report boolean result of numeric greater than or equal to on any value" in {
    assert(oneVerso.numericGreaterThanOrEqual(2))
    assert(oneVerso.numericGreaterThanOrEqual(2.0))
  }
  it should "report boolean result of numeric range within on any value" in {
    assert(oneVerso.numericWithin(1,3))
    assert(oneVerso.numericWithin(1,2))
    assert(oneVerso.numericWithin(2,3))
    assert(oneVerso.numericWithin(1,2.1))

  }
  it should "report boolean result of substring match on any value" in {
    assert(oneRecto.stringContains("rect"))
    assert(oneRecto.stringContains("verso") == false)
  }
  it should "report boolean result of case-insensitive match on any value" in {
    assert(oneRecto.stringContains("RECT") == false)
    assert(oneRecto.stringContains("RECT", false) )
  }
  it should "report boolean result of RE match on any value" in {
    assert(oneRecto.regexMatch("^rect.*"))
  }

  it should "report boolean result of matching a specified property"  in {

    assert(oneRecto.valueEquals(rvProperty,"recto"))
    /*
    assert(oneRecto.valueEquals("verso") == false)
    assert(oneRecto.valueEquals(1))
    assert(oneRecto.valueEquals(2) == false)
    */
  }
  it should "report boolean result of numeric less than on a specified property" in pending
  it should "report boolean result of numeric less than or equal to on a specified property" in pending
  it should "report boolean result of numeric greater than on a specified property" in pending
  it should "report boolean result of numeric greater than or equal to on a specified property" in pending
  it should "report boolean result of numeric range within on a specified property" in pending
  it should "report boolean result of substring match on a specified property" in pending
  it should "report boolean result of case-insensitive match on a specified property" in pending
  it should "report boolean result of RE match on a specified property" in pending

}


/*

/
case object CtsUrnType extends CitePropertyType

case object Cite2UrnType extends CitePropertyType

case object NumericType extends CitePropertyType

case object BooleanType extends CitePropertyType

case object StringType extends CitePropertyType

case object ControlledVocabType extends CitePropertyType
*/
