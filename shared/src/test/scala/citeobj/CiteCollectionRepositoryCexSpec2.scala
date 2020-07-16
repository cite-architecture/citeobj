package edu.holycross.shot.citeobj
import org.scalatest.FlatSpec
import edu.holycross.shot.cite._


/**
*/
class CiteCollectionRepositoryCexSpec2 extends FlatSpec {

  val cex = """#!citecollections
URN#Description#Labelling property#Ordering property#License
urn:cite2:fufolio:poxy2099.v1:#Papyrus POxy 2099#urn:cite2:fufolio:poxy2099.v1.label:#urn:cite2:fufolio:poxy2099.v1.sequence:#Public Domain
urn:cite2:fufolio:poxy2000.v1:#Papyrus POxy 2099#urn:cite2:fufolio:poxy2000.v1.mylabel:#urn:cite2:fufolio:poxy2000.v1.sequence:#Public Domain

#!citeproperties
Property#Label#Type#Authority list
urn:cite2:fufolio:poxy2099.v1.urn:#URN#Cite2Urn#
urn:cite2:fufolio:poxy2099.v1.label:#Label#String#
urn:cite2:fufolio:poxy2099.v1.sequence:#Page sequence#Number#
urn:cite2:fufolio:poxy2099.v1.rv:#Recto or Verso#String#recto,verso
urn:cite2:fufolio:poxy2099.v1.defaultimg:#Default image#Cite2Urn#

#!citedata
sequence#urn#rv#label#defaultimg
0#urn:cite2:fufolio:poxy2099.v1:1#recto#P.Oxy 2099#urn:cite2:fufolio:papyri.2018a:POxy0017n2099a01

#!citeproperties
Property#Label#Type#Authority list
urn:cite2:fufolio:poxy2000.v1.urn:#URN#Cite2Urn#
urn:cite2:fufolio:poxy2000.v1.mylabel:#Label#String#
urn:cite2:fufolio:poxy2000.v1.sequence:#Page sequence#Number#
urn:cite2:fufolio:poxy2000.v1.rv:#Recto or Verso#String#recto,verso
urn:cite2:fufolio:poxy2000.v1.defaultimg:#Default image#Cite2Urn#

#!citedata
urn#sequence#rv#mylabel#defaultimg
urn:cite2:fufolio:poxy2000.v1:1#0#recto#P.Oxy poxy2000#urn:cite2:fufolio:papyri.2018a:POxy0017n2099a01
"""

val cex2 = """#!citecollections
URN#Description#Labelling property#Ordering property#License
urn:cite2:fufolio:poxy2099.v1:#Papyrus POxy 2099#urn:cite2:fufolio:poxy2099.v1.label:#urn:cite2:fufolio:poxy2099.v1.sequence:#Public Domain
urn:cite2:fufolio:poxy2000.v1:#Papyrus POxy 2099#urn:cite2:fufolio:poxy2000.v1.mylabel:#urn:cite2:fufolio:poxy2000.v1.sequence:#Public Domain

#!citeproperties
Property#Label#Type#Authority list
urn:cite2:fufolio:poxy2099.v1.label:#Label#String#
urn:cite2:fufolio:poxy2099.v1.sequence:#Page sequence#Number#
urn:cite2:fufolio:poxy2099.v1.defaultimg:#Default image#Cite2Urn#
urn:cite2:fufolio:poxy2099.v1.urn:#URN#Cite2Urn#
urn:cite2:fufolio:poxy2099.v1.rv:#Recto or Verso#String#recto,verso

#!citedata
sequence#urn#rv#label#defaultimg
0#urn:cite2:fufolio:poxy2099.v1:1#recto#P.Oxy 2099#urn:cite2:fufolio:papyri.2018a:POxy0017n2099a01

#!citeproperties
Property#Label#Type#Authority list
urn:cite2:fufolio:poxy2000.v1.mylabel:#Label#String#
urn:cite2:fufolio:poxy2000.v1.urn:#URN#Cite2Urn#
urn:cite2:fufolio:poxy2000.v1.sequence:#Page sequence#Number#
urn:cite2:fufolio:poxy2000.v1.defaultimg:#Default image#Cite2Urn#
urn:cite2:fufolio:poxy2000.v1.rv:#Recto or Verso#String#recto,verso

#!citedata
urn#sequence#rv#mylabel#defaultimg
urn:cite2:fufolio:poxy2000.v1:1#0#recto#P.Oxy poxy2000#urn:cite2:fufolio:papyri.2018a:POxy0017n2099a01
"""

  "A Cite Collection repository" should "be able to construct a repository from CEX source" in {
    val repo = CiteCollectionRepository(cex,"#",",")
    repo match {
      case ccr: CiteCollectionRepository => assert(true)
      case _ => fail("Should have made CiteCollectionRepository")
    }
  }

  it should "serialize a CCR as CEX" in {
       val repo = CiteCollectionRepository(cex,"#",",")
       val newCex: String = repo.cex()
       println(newCex)
       val newRepo = CiteCollectionRepository(newCex,"#",",")
  }

it should "serialize a CCR as CEX regardless of sequencing of properties in the original" in {
       val repo = CiteCollectionRepository(cex2,"#",",")
       val newCex: String = repo.cex()
       val newRepo = CiteCollectionRepository(newCex,"#",",")
  }



}
