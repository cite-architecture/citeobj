package edu.holycross.shot.citeobj
import org.scalatest.FlatSpec
import edu.holycross.shot.cite._

import edu.holycross.shot.cite._

/**
*/
class PackageObjectSpec extends FlatSpec {

  "The package object" should "convert CSV text to a map"  in {
    val src = """C1,C2,C3
r1a,r1b,r1c
r2a,r2b,r2c
"""
    val mapped = mapsForDelimited(src,",")
    val r1 = mapped(0)
    assert(r1("C1") == "r1a")
    assert(r1("C3") == "r1c")
  }

  it should "determine if a value is valid for a property definition" in {
    val propDef = CitePropertyDef(Cite2Urn("urn:cite2:hmt:speeches.v1.passage:"),"Text passage", CtsUrnType)
    assert(validValue(CtsUrn("urn:cts:greekLit:tlg0012.tlg001:1.7"), propDef))
    assert(validValue("urn:cts:greekLit:tlg0012.tlg001:1.7", propDef) == false)
  }

}
