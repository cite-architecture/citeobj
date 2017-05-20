package edu.holycross.shot.citeobj

import scala.scalajs.js
import edu.holycross.shot.cite._
import edu.holycross.shot.cex._

object Main extends js.JSApp {
  def main(): Unit = {

        val cexSrc = """#!citelibrary
name#demo
urn#urn:cite2:cex:testcoll:hdt1node
license#public domain

#!ctscatalog

urn#citationScheme#groupName#workTitle#versionLabel#exemplarLabel#online
urn:cts:greekLit:tlg0016.tlg001.loebeng:#book/section#Herodotus#Histories#English. trans. Godley [1921]##true

#!ctsdata

urn:cts:greekLit:tlg0016.tlg001.loebeng:1.0#This is the Showing forth of the Inquiry of Herodotus of Halicarnassos, to the end that neither the deeds of men may be forgotten by lapse of time, nor the works great and marvellous, which have been produced some by Hellenes and some by Barbarians, may lose their renown; and especially that the causes may be remembered for which these waged war with one another.

#!citelibrary
name#demo
urn#urn:cite2:cex:testcoll:hdt1node
license#public domain

#!citecatalog
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


#!imagedata

# Lines are structured as:
# collection#protocol#image source initializier#rights property

urn:cite2:hmt:vaimg.v1:#CITE image URL#http://www.homermultitext.org/hmtdigital/images?#urn:cite2:hmt:msA.v1.rights:
urn:cite2:hmt:vaimg.v1:#CITE image string#http://www.homermultitext.org/hmtdigital/images?#urn:cite2:hmt:msA.v1.rights:
urn:cite2:hmt:vaimg.v1:#local jpeg file#./images#urn:cite2:hmt:msA.v1.rights:
urn:cite2:hmt:vaimg.v1:#local jpeg string#./images#urn:cite2:hmt:msA.v1.rights:
"""


        val cex = CexParser(cexSrc)
        println("Parsed a long cex string into " + cex.blocks.size + " blocks.")
        val catSrc = cex.block("citecatalog")

        // This is awkward, and should be changed in an upgrade to CiteCatalog.
        // (Issue filed.) Fortunately, that's not terribly important, since the
        // expectation is that you will build a full repository and access its
        // catalog from the repository object, as below.
        val cat = CiteCatalog(catSrc(0),"#",",")
        assert (cat.size == 1, "Wrong number of collections in catalog! (${cat.size})")
        println(s"Made an individual catalog with ${cat.size} collection(s)." )

        val data = CiteCollectionData(cexSrc,"#",",")
        assert(data.size == 18, "Wrong number of property values in data! (${data.size})")
        println(s"Made an individual data set with ${data.size} property values.")

        val repo = CiteCollectionRepository(cexSrc,"#",",")
        assert (cat.size == 1, "Repository has wrong number of collections in catalog! (${cat.size})")
        assert(data.size == 18, "Repository has wrong number of property values in data! (${data.size})")
        println(s"Made a collection repository directly from CEX, with ${repo.catalog.size} cataloged collection and ${repo.data.size} property values.")

        val imgOption = ImageExtensions(cexSrc)
        imgOption match {
          case imgs: Option[ImageExtensions] => {
            println(s"Instantiated ${imgs.size} image extension")
            val mapPair = imgs.get.protocolMap.toSeq(0)

            val collection = mapPair._1
            val sourceVector = mapPair._2

            val img = Cite2Urn("urn:cite2:hmt:vaimg:VA012RN_0013")
            println(s"On collection ${collection}, found ${sourceVector.size} image extensions.")
            println(s"For image ${img}, they produce:")
            for (s <- sourceVector) {
              println(s"${s.binaryImageSource(img)}  (${s.protocol})")
            }
          
          }
          case _ => println("Failed to make image extensions option")
        }

  }
}
