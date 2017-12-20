/* 

A little script to generate a CEX file containing any numbers of collections, of any size.

Edit the two top vals.
Run in the terminal with, e.g.: `scala -nc collObjects.scala > test.cex`

( `-nc` means "new compiler", which seems sometimes to be necessary. )

*/


val numInCollection:Int = 10000
val numCollections:Int =5 

val cexHeader:String = """// Library of texts for testing CITE Collections
// THIS IS NOT SCHOLARLY DATA
// The pound sign "#" is used as a column divider.
#!cexversion
3.0

#!citelibrary
name#Demo of Cite Collection with many object
urn#urn:cite2:dse:demo.2017a:test
license#public domain

#!ctscatalog
urn#citationScheme#groupName#workTitle#versionLabel#exemplarLabel#online#lang
urn:cts:greekLit:tlg0012.tlg001.msA:#book/line#Homeric poetry#Iliad#HMT project edition of the Venetus A##true#grc

#!ctsdata
urn:cts:greekLit:tlg0012.tlg001.msA:1.1#Μῆνιν ἄειδε θεὰ Πηληϊάδεω Ἀχιλῆος

"""

println(cexHeader)

println("#!citecollections")
println("URN#Description#Labelling property#Ordering property#License")

for (k <- 1 to numCollections){
  val urnbase:String = s"urn:cite2:hmt:test${k}.v1"
  println(s"${urnbase}:#Demo Collection ${k}#${urnbase}.label:#${urnbase}.sequence:#CC-attribution-share-alike")
}

println()

for (k <- 1 to numCollections){ 
  val urnbase:String = s"urn:cite2:hmt:test${k}.v1"

  println("#!citeproperties")
  println("Property#Label#Type#Authority list")
  println(s"${urnbase}.urn:#URN#Cite2Urn#")
  println(s"${urnbase}.label:#Label#String#")
  println(s"${urnbase}.sequence:#Sequence#Number#")
  println(s"${urnbase}.string1:#Dummy String 1#String#")
  println(s"${urnbase}.string2:#Dummy String 2#String#")
  println(s"${urnbase}.string3:#Dummy String 3#String#")
  println(s"${urnbase}.testCtsUrn:#testCtsUrn#CtsUrn#")
  println(s"${urnbase}.testCite2Urn:#testCite2Urn#Cite2Urn#")
  println(s"${urnbase}.testBoolean:#testBoolean#Boolean#")

  println()

  println("#!citedata")
  println("sequence#urn#label#string1#string2#string3#testCtsUrn#testCite2Urn#testBoolean")

  for (i <- 1 to numInCollection){
      val ts:String = s"${i}#${urnbase}:object${i}#Object ${i}#String 1#String 2#String 3#urn:cts:greekLit:group.work.ed:${i}#urn:cite2:ns:collection.v:item${i}#${i % 2 == 0}"
      println(ts)
  }
  println()
}
 

