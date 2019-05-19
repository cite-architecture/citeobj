//name := "CITE Object library"

//crossScalaVersions in ThisBuild := Seq("2.10.6","2.11.8", "2.12.4")
//scalaVersion := (crossScalaVersions in ThisBuild).value.last


lazy val supportedScalaVersions = List("2.10.6","2.11.8", "2.12.4")

lazy val root = project.in(file(".")).
    aggregate(crossedJVM, crossedJS).
    settings(
      crossScalaVersions := Nil,
      publish / skip := true
    )

lazy val crossed = crossProject.in(file(".")).
    settings(
      name := "citeobj",
      organization := "edu.holycross.shot",
      version := "7.3.3",
      licenses += ("GPL-3.0",url("https://opensource.org/licenses/gpl-3.0.html")),
      resolvers += Resolver.jcenterRepo,
      resolvers += Resolver.bintrayRepo("neelsmith", "maven"),
      libraryDependencies ++= Seq(
        "org.scala-js" %% "scalajs-stubs" % scalaJSVersion % "provided",
        "org.scalatest" %%% "scalatest" % "3.0.1" % "test",
        "edu.holycross.shot.cite" %%% "xcite" % "4.0.2",

        "edu.holycross.shot" %%% "cex" % "6.3.3"
      )
    ).
    jvmSettings(
      tutTargetDirectory := file("docs"),
      tutSourceDirectory := file("shared/src/main/tut"),
      crossScalaVersions := supportedScalaVersions
    ).
    jsSettings(
      skip in packageJSDependencies := false,
      scalaJSUseMainModuleInitializer in Compile := true,
      crossScalaVersions := supportedScalaVersions


    )

lazy val crossedJVM = crossed.jvm.enablePlugins(TutPlugin)
lazy val crossedJS = crossed.js
