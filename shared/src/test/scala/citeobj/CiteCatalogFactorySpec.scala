package edu.holycross.shot.citeobj
import org.scalatest.FlatSpec
import edu.holycross.shot.cite._
import scala.xml._
/**
*/
class CiteCatalogFactorySpec extends FlatSpec {

  val catalogString = """<?xml version="1.0" encoding="UTF-8"?><?xml-model href="CiteCollectionInventory.rng" type="application/xml" schematypens="http://relaxng.org/ns/structure/1.0"?><collectionService xmlns="http://chs.harvard.edu/xmlns/cite" xmlns:dc="http://purl.org/dc/elements/1.1/" version="1.0"> <citeCollection canonicalId="URN" label="Label" urn="urn:cite2:hmt:msA.v1:"> <namespaceMapping abbr="hmt" uri="http://www.homermultitext.org/datans"/> <dc:title xmlns="http://purl.org/dc/elements/1.1/">Folios of the Venetus A Manuscript</dc:title> <creator xmlns="http://purl.org/dc/elements/1.1/">Christopher Blackwell</creator> <creator xmlns="http://purl.org/dc/elements/1.1/">Neel Smith</creator> <description xmlns="http://purl.org/dc/elements/1.1/">Folios of the Venetus A manuscript.</description> <rights xmlns="http://purl.org/dc/elements/1.1/"> All data in this collection are available under the terms of the Creative Commons Attribution-Non-Commercial 3.0 Unported License, http://creativecommons.org/licenses/by-nc/3.0/deed.en_US</rights> <orderedBy property="Sequence"/> <source type="file" value="venetusA-v1.tsv"/> <citeProperty name="URN" label="The URN for this folio" type="cite2urn"></citeProperty> <citeProperty name="Sequence" label="Sequence" type="number"></citeProperty> <citeProperty name="Siglum" label="Manuscript Siglum" type="string"></citeProperty> <citeProperty name="Label" label="Label" type="string"/> <citeProperty name="CodexURN" label="Codex" type="cite2urn"/> <citeProperty name="RV" label="Recto or Verso" type="string"><valueList><value>recto</value>
  <value>verso</value></valueList></citeProperty> </citeCollection></collectionService>"""

  "A CITE catalog factory" should "make a catalog from a (long) string of XML" in {
    val cat = CiteCatalog.fromXmlString(catalogString)
    cat match {
      case c: CiteCatalog => assert(true)
      case _ => fail("Failed to build a CiteCatalog from XML node sequence")
    }
  }

  it should "make a catalog from an XML NodeSeq on the root element" in {
    val root = XML.loadString(catalogString)
    val cat = CiteCatalog.fromNodeSeq(root)
    cat match {
      case c: CiteCatalog => assert(true)
      case _ => fail("Failed to build a CiteCatalog from XML node sequence")
    }
  }

  it should "create Cite property definitions from citeProperty elements" in {
    val root = XML.loadString(catalogString)
    val propertyElements = root \\ "citeProperty"
    assert(propertyElements.size == 6)

    val firstProp = CiteCatalog.propDefFromXml(propertyElements(0),Cite2Urn("urn:cite2:hmt:msA.v1:"))

    assert(firstProp.urn ==  Cite2Urn("urn:cite2:hmt:msA.v1.URN:"))
    assert(firstProp.label == "The URN for this folio")
    assert (firstProp.propertyType.toString == "Cite2UrnType")
  }


  it should "create Cite collection definitions from citeCollection elements" in {
    val root = XML.loadString(catalogString)
    val collectionElements = root \\ "citeCollection"
    assert (collectionElements.size == 1)
    val collObject = CiteCatalog.collectionDefFromXml(collectionElements(0))
    assert(collObject.urn == Cite2Urn("urn:cite2:hmt:msA.v1:"))
    assert(collObject.collectionLabel == "Folios of the Venetus A Manuscript")
    assert(collObject.labellingProperty.get == Cite2Urn("urn:cite2:hmt:msA.v1.Label:"))
    assert(collObject.orderingProperty.get == Cite2Urn("urn:cite2:hmt:msA.v1:Sequence"))
    assert(collObject.propertyDefs.size == 6)

  }



}
