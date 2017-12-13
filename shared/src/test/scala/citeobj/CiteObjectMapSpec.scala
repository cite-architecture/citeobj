package edu.holycross.shot.citeobj
import org.scalatest.FlatSpec
import edu.holycross.shot.cite._

import java.lang.System.currentTimeMillis

/**
*/
class CiteObjectMapSpec extends FlatSpec {

    val cex = """
// Library of texts for testing CITE Collections
// THIS IS NOT SCHOLARLY DATA
// The pound sign "#" is used as a column divider.
#!cexversion
3.0

#!citelibrary
name#Demo of Cite Collection with many object
urn#urn:cite2:dse:demo.2017a:threeCollections
license#public domain

#!ctscatalog
urn#citationScheme#groupName#workTitle#versionLabel#exemplarLabel#online#lang
urn:cts:greekLit:tlg0012.tlg001.msA:#book/line#Homeric poetry#Iliad#HMT project edition of the Venetus A##true#grc

#!ctsdata
urn:cts:greekLit:tlg0012.tlg001.msA:1.1#Μῆνιν ἄειδε θεὰ Πηληϊάδεω Ἀχιλῆος


#!citecollections
URN#Description#Labelling property#Ordering property#License
urn:cite2:hmt:test1.v1:#Demo Collection#urn:cite2:hmt:test1.v1.label:#urn:cite2:hmt:test1.v1.sequence:#CC-attribution-share-alike
urn:cite2:hmt:test2.v1:#Demo Collection#urn:cite2:hmt:test2.v1.label:#urn:cite2:hmt:test2.v1.sequence:#CC-attribution-share-alike
urn:cite2:hmt:test3.v1:#Demo Collection#urn:cite2:hmt:test3.v1.label:#urn:cite2:hmt:test3.v1.sequence:#CC-attribution-share-alike

#!citeproperties
Property#Label#Type#Authority list
urn:cite2:hmt:test1.v1.urn:#URN#Cite2Urn#
urn:cite2:hmt:test1.v1.label:#Label#String#
urn:cite2:hmt:test1.v1.sequence:#Page sequence#Number#

#!citeproperties
Property#Label#Type#Authority list
urn:cite2:hmt:test2.v1.urn:#URN#Cite2Urn#
urn:cite2:hmt:test2.v1.label:#Label#String#
urn:cite2:hmt:test2.v1.sequence:#Page sequence#Number#

#!citeproperties
Property#Label#Type#Authority list
urn:cite2:hmt:test3.v1.urn:#URN#Cite2Urn#
urn:cite2:hmt:test3.v1.label:#Label#String#
urn:cite2:hmt:test3.v1.sequence:#Page sequence#Number#

#!citedata
sequence#urn#label
1#urn:cite2:hmt:test1.v1:object1#Object 1
2#urn:cite2:hmt:test1.v1:object2#Object 2
3#urn:cite2:hmt:test1.v1:object3#Object 3
4#urn:cite2:hmt:test1.v1:object4#Object 4
5#urn:cite2:hmt:test1.v1:object5#Object 5
6#urn:cite2:hmt:test1.v1:object6#Object 6
7#urn:cite2:hmt:test1.v1:object7#Object 7
8#urn:cite2:hmt:test1.v1:object8#Object 8
9#urn:cite2:hmt:test1.v1:object9#Object 9
10#urn:cite2:hmt:test1.v1:object10#Object 10
11#urn:cite2:hmt:test1.v1:object11#Object 11
12#urn:cite2:hmt:test1.v1:object12#Object 12
13#urn:cite2:hmt:test1.v1:object13#Object 13
14#urn:cite2:hmt:test1.v1:object14#Object 14
15#urn:cite2:hmt:test1.v1:object15#Object 15
16#urn:cite2:hmt:test1.v1:object16#Object 16
17#urn:cite2:hmt:test1.v1:object17#Object 17
18#urn:cite2:hmt:test1.v1:object18#Object 18
19#urn:cite2:hmt:test1.v1:object19#Object 19
20#urn:cite2:hmt:test1.v1:object20#Object 20
21#urn:cite2:hmt:test1.v1:object21#Object 21
22#urn:cite2:hmt:test1.v1:object22#Object 22
23#urn:cite2:hmt:test1.v1:object23#Object 23
24#urn:cite2:hmt:test1.v1:object24#Object 24
25#urn:cite2:hmt:test1.v1:object25#Object 25

#!citedata
sequence#urn#label
1#urn:cite2:hmt:test2.v1:object1#Object 1
2#urn:cite2:hmt:test2.v1:object2#Object 2
3#urn:cite2:hmt:test2.v1:object3#Object 3
4#urn:cite2:hmt:test2.v1:object4#Object 4
5#urn:cite2:hmt:test2.v1:object5#Object 5
6#urn:cite2:hmt:test2.v1:object6#Object 6
7#urn:cite2:hmt:test2.v1:object7#Object 7
8#urn:cite2:hmt:test2.v1:object8#Object 8
9#urn:cite2:hmt:test2.v1:object9#Object 9
10#urn:cite2:hmt:test2.v1:object10#Object 10
11#urn:cite2:hmt:test2.v1:object11#Object 11
12#urn:cite2:hmt:test2.v1:object12#Object 12
13#urn:cite2:hmt:test2.v1:object13#Object 13
14#urn:cite2:hmt:test2.v1:object14#Object 14
15#urn:cite2:hmt:test2.v1:object15#Object 15
16#urn:cite2:hmt:test2.v1:object16#Object 16
17#urn:cite2:hmt:test2.v1:object17#Object 17
18#urn:cite2:hmt:test2.v1:object18#Object 18
19#urn:cite2:hmt:test2.v1:object19#Object 19
20#urn:cite2:hmt:test2.v1:object20#Object 20
21#urn:cite2:hmt:test2.v1:object21#Object 21
22#urn:cite2:hmt:test2.v1:object22#Object 22
23#urn:cite2:hmt:test2.v1:object23#Object 23
24#urn:cite2:hmt:test2.v1:object24#Object 24
25#urn:cite2:hmt:test2.v1:object25#Object 25

#!citedata
sequence#urn#label
1#urn:cite2:hmt:test3.v1:object1#Object 1
2#urn:cite2:hmt:test3.v1:object2#Object 2
3#urn:cite2:hmt:test3.v1:object3#Object 3
4#urn:cite2:hmt:test3.v1:object4#Object 4
5#urn:cite2:hmt:test3.v1:object5#Object 5
6#urn:cite2:hmt:test3.v1:object6#Object 6
7#urn:cite2:hmt:test3.v1:object7#Object 7
8#urn:cite2:hmt:test3.v1:object8#Object 8
9#urn:cite2:hmt:test3.v1:object9#Object 9
10#urn:cite2:hmt:test3.v1:object10#Object 10
11#urn:cite2:hmt:test3.v1:object11#Object 11
12#urn:cite2:hmt:test3.v1:object12#Object 12
13#urn:cite2:hmt:test3.v1:object13#Object 13
14#urn:cite2:hmt:test3.v1:object14#Object 14
15#urn:cite2:hmt:test3.v1:object15#Object 15
16#urn:cite2:hmt:test3.v1:object16#Object 16
17#urn:cite2:hmt:test3.v1:object17#Object 17
18#urn:cite2:hmt:test3.v1:object18#Object 18
19#urn:cite2:hmt:test3.v1:object19#Object 19
20#urn:cite2:hmt:test3.v1:object20#Object 20
21#urn:cite2:hmt:test3.v1:object21#Object 21
22#urn:cite2:hmt:test3.v1:object22#Object 22
23#urn:cite2:hmt:test3.v1:object23#Object 23
24#urn:cite2:hmt:test3.v1:object24#Object 24
25#urn:cite2:hmt:test3.v1:object25#Object 25"""


    val timeStart =  java.lang.System.currentTimeMillis()
    val repo = CiteCollectionRepository(cex,"#",",")
    val timeEnd =  java.lang.System.currentTimeMillis()
    println(s"\n=================\nBuilt 3-collection (3k) repo in: ${(timeEnd - timeStart)/1000} seconds")

  "A CITE Object Map" should  "have a map of [Cite2Urn,CiteObject]" in {
    val com:CiteObjectMap = repo.objects
    com.objectMap match {
      case v: Map[Cite2Urn,CiteObject] => assert(true)
      case _ => fail("Should have find a vector of cite property values")
    }
  }
  /*
  it should "support selecting all properties of an object using URN twiddling" in {
    val dataCollection = CiteCollectionData(dataV)

    val speech4 = dataCollection ~~ Cite2Urn("urn:cite2:hmt:speeches:speech4")
    assert(speech4.data.size == 2 )
    val props = List("speaker", "passage")
    for (prop <- speech4.data) {
      assert (props.contains(prop.urn.property))
    }
  }


  it should "support  selecting all instances of a property using URN twiddling" in {
    val dataCollection = CiteCollectionData(dataV)
    val speakers = dataCollection ~~ Cite2Urn("urn:cite2:hmt:speeches.v1.speaker:")
    assert(speakers.data.size == 2 )
    val speakerIds = List(Cite2Urn("urn:cite2:hmt:pers:pers1"), Cite2Urn("urn:cite2:hmt:pers:pers22"))

    for (speaker <- speakers.data) {
      assert (speakerIds.contains(speaker.propertyValue))
    }
  }

  it should "provide a function listing distinct properties in the collection" in {
    val dataCollection = CiteCollectionData(dataV)
    val expectedProps = Set(Cite2Urn("urn:cite2:hmt:speeches.v1.speaker:"),
    Cite2Urn("urn:cite2:hmt:speeches.v1.passage:"))

    assert(dataCollection.properties == expectedProps)
  }

  it should "provide a function listing distinct objects in the collection" in {
    val dataCollection = CiteCollectionData(dataV)
    val expectedObjects = Set(

      Cite2Urn("urn:cite2:hmt:speeches.v1:speech1"),
      Cite2Urn("urn:cite2:hmt:speeches.v1:speech4")
    )

    assert(dataCollection.objects == expectedObjects)
  }
  */
}
