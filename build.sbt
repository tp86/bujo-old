import sbt.librarymanagement.CrossVersionFunctions
import bloop.integrations.sbt.BloopDefaults

val scala3Version = "3.0.0-RC2"
lazy val scalatest = "org.scalatest" %% "scalatest" % "3.2.7"

lazy val AccTest = config("acceptance-test") extend Test
lazy val accTest = taskKey[Unit]("Executes acceptance tests.")
accTest := (AccTest / test).value

lazy val root = project
  .in(file("."))
  .configs(AccTest)
  .settings(
    name := "bujo-domain",
    version := "0.1.0",
    scalaVersion := scala3Version,
    Test / jacocoReportSettings := JacocoReportSettings()
      .withFormats(JacocoReportFormats.ScalaHTML, JacocoReportFormats.XML),
    inConfig(AccTest)(Defaults.testSettings ++ BloopDefaults.configSettings),
    libraryDependencies += scalatest % Test,
    libraryDependencies += scalatest % AccTest,
    libraryDependencies += "org.typelevel" %% "cats-core" % "2.5.0",
    libraryDependencies += ("com.typesafe.slick" %% "slick" % "3.3.3")
      .cross(CrossVersion.for3Use2_13),
    libraryDependencies += "org.slf4j" % "slf4j-nop" % "2.0.0-alpha1"
  )
