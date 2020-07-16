package edu.holycross.shot.citeobj
import org.scalatest.FlatSpec
import edu.holycross.shot.cite._

import edu.holycross.shot.cite._

/**
*/
class ObjectQuerySpec extends FlatSpec {

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
urn:cite2:hmt:msA.v1.validated:#Passed Validation#Boolean#

#!citedata
siglum#sequence#urn#rv#label#codex#validated
msA#1#urn:cite2:hmt:msA.v1:1r#recto#Marcianus Graecus Z. 454 (= 822) (Venetus A) folio 1r#urn:cite2:hmt:codex:msA#true
msA#2#urn:cite2:hmt:msA.v1:1v#verso#Marcianus Graecus Z. 454 (= 822) (Venetus A) folio 1v#urn:cite2:hmt:codex:msA#true
msA#3#urn:cite2:hmt:msA.v1:2r#recto#Marcianus Graecus Z. 454 (= 822) (Venetus A) folio 2r#urn:cite2:hmt:codex:msA#false
"""

  val repo = CiteCollectionRepository(cex,"#",",")
  val oneRecto = repo.citableObject(Cite2Urn("urn:cite2:hmt:msA.v1:1r"))
  val oneVerso = repo.citableObject(Cite2Urn("urn:cite2:hmt:msA.v1:1v"))

  val rvProperty = Cite2Urn("urn:cite2:hmt:msA.v1.rv:")
  val seqProperty = Cite2Urn("urn:cite2:hmt:msA.v1.sequence:")
  val codexProperty = Cite2Urn("urn:cite2:hmt:msA.v1.codex:")

  "A CiteObject" should "report boolean result of matching any value"  in {
    /*
    println(s"""++++ ${oneRecto.valueEquals("recto")}""")
    println(s"""${oneRecto.urn}""")
    println(s"""${oneVerso.urn}""")
    */
    assert(oneRecto.valueEquals("recto"))
    /*
    assert(oneRecto.valueEquals("verso") == false)
    assert(oneRecto.valueEquals(1))
    assert(oneRecto.valueEquals(2) == false)

    val codex =Cite2Urn("urn:cite2:hmt:codex:msA")
    assert(oneRecto.valueEquals(codex))
    */
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
    assert(oneRecto.valueEquals(rvProperty,"verso") == false)
    assert(oneRecto.valueEquals(seqProperty,1))
  }

  it should "throw an exception if value and type do not match in matching property" in {
    try {
        val propCheck = oneRecto.valueEquals(rvProperty,2)
        fail("Should not have been able to check value")
    } catch {
      case coe: CiteObjectException => assert(coe.message == "Type fails: ControlledVocabType does not match value 2")
      case t: Throwable => fail("Should have thrown CiteObjectException but threw " + t)
    }
  }


  it should "report boolean result of numeric less than on a specified property" in {
      assert(oneRecto.numericLessThan(seqProperty,2))
      assert(oneRecto.numericLessThan(seqProperty,1.1))
      assert(oneRecto.numericLessThan(seqProperty,1) == false)
  }

  it should "throw an exception if value and type do not match in less than comparison" in {
    try {
        val propCheck = oneRecto.numericLessThan(rvProperty,2)
        fail("Should not have been able to check value")
    } catch {
      case coe: CiteObjectException => assert(coe.message contains "Type fails: ControlledVocabType does not match value 2")
      case t: Throwable => fail("Should have thrown CiteObjectException but threw " + t)
    }
  }

  it should "report boolean result of numeric less than or equal to on a specified property" in {
    assert(oneRecto.numericLessThanOrEqual(seqProperty,1))
    assert(oneRecto.numericLessThanOrEqual(seqProperty,1.5))
    assert(oneRecto.numericLessThanOrEqual(seqProperty,0.9) == false)
  }

  it should "throw an exception if value and type do not match in less than or equal comparison" in {
    try {
        val propCheck = oneRecto.numericLessThanOrEqual(rvProperty,2)
        fail("Should not have been able to check value")
    } catch {
      case coe: CiteObjectException => assert(coe.message contains "Type fails: ControlledVocabType does not match value 2")
      case t: Throwable => fail("Should have thrown CiteObjectException but threw " + t)
    }
  }

  it should "report boolean result of numeric greater than  on a specified property" in {
    assert(oneRecto.numericGreaterThan(seqProperty,1) == false)

    assert(oneRecto.numericGreaterThan(seqProperty,1.5) == false)
    assert(oneRecto.numericGreaterThan(seqProperty,0.9))
  }
  it should "throw an exception if value and type do not match in greater than  comparison" in {
    try {
        val propCheck = oneRecto.numericGreaterThan(rvProperty,2)
        fail("Should not have been able to check value")
    } catch {
      case coe: CiteObjectException => assert(coe.message contains "Type fails: ControlledVocabType does not match value 2")
      case t: Throwable => fail("Should have thrown CiteObjectException but threw " + t)
    }

  }
  it should "report boolean result of numeric greater than or equal to on a specified property" in {
    assert(oneRecto.numericGreaterThanOrEqual(seqProperty,1))

    assert(oneRecto.numericGreaterThanOrEqual(seqProperty,1.5) == false)
    assert(oneRecto.numericGreaterThanOrEqual(seqProperty,0.9))
  }
  it should "throw an exception if value and type do not match in greater than or equal comparison" in {
    try {
        val propCheck = oneRecto.numericGreaterThanOrEqual(rvProperty,2)
        fail("Should not have been able to check value")
    } catch {
      case coe: CiteObjectException => assert(coe.message contains "Type fails: ControlledVocabType does not match value 2")
      case t: Throwable => fail("Should have thrown CiteObjectException but threw " + t)
    }
  }
  it should "report boolean result of numeric range within on a specified property" in {
    assert(oneVerso.numericWithin(seqProperty,1,3))
    assert(oneVerso.numericWithin(seqProperty,1,2))
    assert(oneVerso.numericWithin(seqProperty,2,3))
    assert(oneVerso.numericWithin(seqProperty,1,2.1))
  }

  it should "throw an exception if lower bound value and type do not match in range comparison" in {
    try {
        val propCheck = oneVerso.numericWithin(rvProperty,1,3)
        fail("Should not have been able to check value")
    } catch {
      case iae:  IllegalArgumentException => assert(iae.getMessage() contains "did not match type ControlledVocabType")
      case t: Throwable => fail("Should have thrown IllegalArgumentException but threw " + t)
    }

  }

  it should "report boolean result of substring match on a specified property" in {
    assert(oneRecto.stringContains(rvProperty,"rect",true))

    assert(oneRecto.stringContains(rvProperty,"verso",true) == false)
  }
  it should "report boolean result of case-insensitive match on a specified property" in {
    assert(oneRecto.stringContains(rvProperty,"Rect",false))
  }
  it should "throw an exception if value and type do not match in substring comparison" in {

    try {
        val propCheck =   oneRecto.stringContains(seqProperty,"rect",true)
        fail("Should not have been able to check value")
    } catch {
      case coe: CiteObjectException => assert(coe.message == "Type NumericType did not match value rect.")
      case t: Throwable => fail("Should have thrown CiteObjectException but threw " + t)
    }
  }


  it should "report boolean result of RE match on a specified property" in {
    assert(oneRecto.regexMatch(rvProperty,"^rect.*"))

  }
  it should "throw an exception if value and type do not match in RE match" in {
    try {
        val propCheck =   oneRecto.regexMatch(seqProperty,"^rect.*")
        fail("Should not have been able to check value")
    } catch {
      case coe: CiteObjectException =>  assert(coe.message  == "Type NumericType did not match value for ^rect.*.")
      case t: Throwable => fail("Should have thrown CiteObjectException but threw " + t)
    }
  }

  it should "report boolean result of URN match on on any value" in {
    val codexCollection =Cite2Urn("urn:cite2:hmt:codex:")
    assert(oneRecto.urnMatch(codexCollection))
  }

  it should "report boolean result of URN match on a specified property" in {
    val codexCollection =Cite2Urn("urn:cite2:hmt:codex:")
    assert(oneRecto.urnMatch(codexProperty,codexCollection))
  }

  it should "throw an exception if URN type and value do not agree" in {
    val psg =CtsUrn("urn:cts:greekLit:tlg0012.tlg001:1.1")

    try  {
      val checkProp= oneRecto.urnMatch(codexProperty,psg)
      fail("Should not have been able to check value")
    } catch {
      case coe: CiteObjectException => assert(coe.message == "Type Cite2UrnType did not match value for urn:cts:greekLit:tlg0012.tlg001:1.1.")
      case t: Throwable => fail("Should have thrown CiteObjectException but threw " + t)
    }
  }

  it should "include label in unqualified string search" in {
    assert(oneRecto.stringContains("Venetus"))
    assert(oneRecto.stringContains("venetus") == false)
  }

  it should "respect case in searching label" in {
    assert(oneRecto.stringContains("venetus", false))
  }


  val orcacex = """
#!citecollections
URN#Description#Labelling property#Ordering property#License
urn:cite2:hmt:clausereading.v1:#Clauses in text of the Iliad#urn:cite2:hmt:clausereading.v1.deformation:#urn:cite2:hmt:clausereading.v1.seq:#CC-attribution-share-alike

#!citeproperties
Property#Label#Type#Authority list
urn:cite2:hmt:clausereading.v1.urn:#ORCA URN#Cite2Urn#
urn:cite2:hmt:clausereading.v1.passage:#Passage analyzed#CtsUrn#
urn:cite2:hmt:clausereading.v1.analysis:#Analysis#Cite2Urn#
urn:cite2:hmt:clausereading.v1.label:#Label#String#
urn:cite2:hmt:clausereading.v1.seq:#Sequence#Number#
urn:cite2:hmt:clausereading.v1.deformation:#Text deformation#String#

#!citedata
urn#passage#analysis#label#seq#deformation
urn:cite2:hmt:clausereading.v1:clause1#urn:cts:greekLit:tlg0012.tlg001.fuPers:1.1-1.2@ν[2]#urn:cite2:hmt:iliadicClauses.v1:imperative#clause 1#1#Μῆνιν ἄειδε θεὰ Πηληϊάδεω Ἀχιλῆος οὐλομένην
urn:cite2:hmt:clausereading.v1:clause2#urn:cts:greekLit:tlg0012.tlg001.fuPers:1.2@ἣ[1]-1.2@ε[2]#urn:cite2:hmt:iliadicClauses.v1:indicative#clause 2#2#ἣ μυρί᾽ Ἀχαιοῖς ἄλγε᾽ ἔθηκε
urn:cite2:hmt:clausereading.v1:clause3#urn:cts:greekLit:tlg0012.tlg001.fuPers:1.3@π[1]-1.4@ν[1]#urn:cite2:hmt:iliadicClauses.v1:indicative#clause 3#3#πολλὰς δ᾽ ἰφθίμους ψυχὰς Ἄϊδι προΐαψεν ἡρώων
urn:cite2:hmt:clausereading.v1:clause4#urn:cts:greekLit:tlg0012.tlg001.fuPers:1.4@α[1]-1.5@ι[1]#urn:cite2:hmt:iliadicClauses.v1:indicative#clause 4#4#αὐτοὺς δὲ ἑλώρια τεῦχε κύνεσσιν οἰωνοῖσί τε πᾶσ
urn:cite2:hmt:clausereading.v1:clause5#urn:cts:greekLit:tlg0012.tlg001.fuPers:1.5@Δ[1]-1.5@ή[1]#urn:cite2:hmt:iliadicClauses.v1:indicative#clause 5#5#Διὸς δ᾽ ἐτελείετο βουλή
urn:cite2:hmt:clausereading.v1:clause6#urn:cts:greekLit:tlg0012.tlg001.fuPers:1.6-1.7#urn:cite2:hmt:iliadicClauses.v1:subordinate#clause 6#6#ἐξ οὗ δὴ τὰ πρῶτα διαστήτην ἐρίσαντε Ἀτρεΐδης τε ἄναξ ἀνδρῶν καὶ δῖος Ἀχιλλεύς.
urn:cite2:hmt:clausereading.v1:clause7#urn:cts:greekLit:tlg0012.tlg001.fuPers:1.8#urn:cite2:hmt:iliadicClauses.v1:interrogative#clause 7#7#Τίς τάρ σφωε θεῶν ἔριδι ξυνέηκε μάχεσθαι;
urn:cite2:hmt:clausereading.v1:clause8#urn:cts:greekLit:tlg0012.tlg001.fuPers:1.9@Λ[1]-1.9@ς[3]#urn:cite2:hmt:iliadicClauses.v1:indicative#clause 8#8#Λητοῦς καὶ Διὸς υἱός [ξυνέηκε]
urn:cite2:hmt:clausereading.v1:clause9#urn:cts:greekLit:tlg0012.tlg001.fuPers:1.9@ὃ[1]-1.10@ν[5]#urn:cite2:hmt:iliadicClauses.v1:indicative#clause 9#9#ὃ γὰρ βασιλῆϊ χολωθεὶς νοῦσον ἀνὰ στρατὸν ὄρσε κακήν
urn:cite2:hmt:clausereading.v1:clause10#urn:cts:greekLit:tlg0012.tlg001.fuPers:1.10@ὀ[1]-1.10@ί[1]#urn:cite2:hmt:iliadicClauses.v1:indicative#clause 10#10#ὀλέκοντο δὲ λαοί
urn:cite2:hmt:clausereading.v1:clause11#urn:cts:greekLit:tlg0012.tlg001.fuPers:2.1@οὐδέ[1]-2.1@ἐΐσης[1]#urn:cite2:hmt:iliadicClauses.v1:indicative#clause 11#11#οὐδέ τι θυμὸς ἐδεύετο δαιτὸς ἐΐσης
"""

  it should "match URNs in properties" in {
    //println("----- got here 1")
    val orcaRepo = CiteCollectionRepository(orcacex,"#",",")
    //println("----- got here 2")
    val orcaObjects = orcaRepo.citableObjects
    //println("----- got here 3")
    val orca1 = orcaRepo.citableObject(Cite2Urn("urn:cite2:hmt:clausereading.v1:clause1"))
    val iliad = CtsUrn("urn:cts:greekLit:tlg0012.tlg001:")
    //println("matches Iliad? " + orca1.urnMatch(iliad))
  }



}
