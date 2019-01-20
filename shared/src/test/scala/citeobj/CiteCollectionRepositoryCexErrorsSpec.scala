package edu.holycross.shot.citeobj
import org.scalatest.FlatSpec
import edu.holycross.shot.cite._


/**
*/
class CiteCollectionRepositoryCexErrorsSpec extends FlatSpec {

 
  "A Cite Collection repository" should "provide a helpful error when defined properties don't match what is in #!citedata" in {
  val conflicted = """
#!citelibrary
license#public domain
name#Demo library
urn#urn:cite2:cex:democex.2017a:test

#!citecollections
URN#Description#Labelling property#Ordering property#License
urn:cite2:fufolio:poxy2099.v1:#Papyrus POxy 2099#urn:cite2:fufolio:poxy2099.v1.label:#urn:cite2:fufolio:poxy2099.v1.sequence:#Public Domain

#!citeproperties
Property#Label#Type#Authority list
urn:cite2:fufolio:poxy2099.v1.sequence:#Page sequence#Number#
urn:cite2:fufolio:poxy2099.v1.urn:#URN#Cite2Urn#
urn:cite2:fufolio:poxy2099.v1.rv:#Recto or Verso#String#recto,verso
urn:cite2:fufolio:poxy2099.v1.label:#Label#String#
urn:cite2:fufolio:poxy2099.v1.defaultimg:#Default image#Cite2Urn#

#!citedata
NOTsequence#urn#rv#label#defaultimg
0#urn:cite2:fufolio:poxy2099.v1:1#recto#P.Oxy 2099#urn:cite2:fufolio:papyri.2018a:POxy0017n2099a01

""" 
  try {
      val lib = CiteCollectionRepository(conflicted, "#",",")
      fail("Should not have made CiteLibrary")
    } catch {
      case coe: CiteObjectException => println(coe.getMessage()); true
      case t: Throwable => fail("Should have thrown an CiteObjectException Exception: " + t)
    }
}


"A Cite Collection repository" should "provide a helpful error when an object has too fiew fields" in {
  val conflicted = """
#!citelibrary
license#public domain
name#Demo library
urn#urn:cite2:cex:democex.2017a:test

#!citecollections
URN#Description#Labelling property#Ordering property#License
urn:cite2:fufolio:poxy2099.v1:#Papyrus POxy 2099#urn:cite2:fufolio:poxy2099.v1.label:#urn:cite2:fufolio:poxy2099.v1.sequence:#Public Domain

#!citeproperties
Property#Label#Type#Authority list
urn:cite2:fufolio:poxy2099.v1.sequence:#Page sequence#Number#
urn:cite2:fufolio:poxy2099.v1.urn:#URN#Cite2Urn#
urn:cite2:fufolio:poxy2099.v1.rv:#Recto or Verso#String#recto,verso
urn:cite2:fufolio:poxy2099.v1.label:#Label#String#
urn:cite2:fufolio:poxy2099.v1.defaultimg:#Default image#Cite2Urn#

#!citedata
sequence#urn#rv#label#defaultimg
0urn:cite2:fufolio:poxy2099.v1:1#recto#P.Oxy 2099#urn:cite2:fufolio:papyri.2018a:POxy0017n2099a01
1#urn:cite2:fufolio:poxy2099.v1:2#verso#P.Oxy 2099#urn:cite2:fufolio:papyri.2018a:POxy0017n2099a01

""" 
  try {
      val lib = CiteCollectionRepository(conflicted, "#",",")
      fail("Should not have made CiteLibrary")
    } catch {
      case coe: CiteObjectException => println(coe.getMessage()); true
      case t: Throwable => fail("Should have thrown an CiteObjectException Exception: " + t)
    }

}

"A Cite Collection repository" should "provide a helpful error when an object has too fiew fields in the second line" in {
  val conflicted = """
#!citelibrary
license#public domain
name#Demo library
urn#urn:cite2:cex:democex.2017a:test

#!citecollections
URN#Description#Labelling property#Ordering property#License
urn:cite2:fufolio:poxy2099.v1:#Papyrus POxy 2099#urn:cite2:fufolio:poxy2099.v1.label:#urn:cite2:fufolio:poxy2099.v1.sequence:#Public Domain

#!citeproperties
Property#Label#Type#Authority list
urn:cite2:fufolio:poxy2099.v1.sequence:#Page sequence#Number#
urn:cite2:fufolio:poxy2099.v1.urn:#URN#Cite2Urn#
urn:cite2:fufolio:poxy2099.v1.rv:#Recto or Verso#String#recto,verso
urn:cite2:fufolio:poxy2099.v1.label:#Label#String#
urn:cite2:fufolio:poxy2099.v1.defaultimg:#Default image#Cite2Urn#

#!citedata
sequence#urn#rv#label#defaultimg
0#urn:cite2:fufolio:poxy2099.v1:1#recto#P.Oxy 2099#urn:cite2:fufolio:papyri.2018a:POxy0017n2099a01
1urn:cite2:fufolio:poxy2099.v1:2#verso#P.Oxy 2099#urn:cite2:fufolio:papyri.2018a:POxy0017n2099a01

""" 
  try {
      val lib = CiteCollectionRepository(conflicted, "#",",")
      fail("Should not have made CiteLibrary")
    } catch {
      case coe: CiteObjectException => println(coe.getMessage()); true
      case t: Throwable => fail("Should have thrown an CiteObjectException Exception: " + t)
    }
}



}
