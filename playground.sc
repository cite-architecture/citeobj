import edu.holycross.shot.citeobj._
import edu.holycross.shot.cite._
import java.lang.System.currentTimeMillis


val f = "jvm/src/test/resources/twoThousandObjects.cex"
val fhundredk = "jvm/src/test/resources/hundredThousandObjects.cex"
val f3hundredk = "jvm/src/test/resources/threeHundredThousandObjects.cex"
val f3k = "jvm/src/test/resources/threeThousandObjects.cex"
val testCex = "/Users/cblackwell/Dropbox/CITE/scala/Akka-work/scs-akka/cex/test.cex"
val fmillion = "jvm/src/test/resources/millionObjects.cex"
val fmillionX3 = "jvm/src/test/resources/millionBy3.cex"

val timeStart =  java.lang.System.currentTimeMillis()
val repo = CiteRepositorySource.fromFile(testCex)
val timeEnd =  java.lang.System.currentTimeMillis()
val repoSize = repo.data.data.size
println(s"\n=================\nconstructed repo of ${repoSize} properties in: ${(timeEnd - timeStart)/1000} seconds")
