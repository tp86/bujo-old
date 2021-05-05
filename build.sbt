import bloop.integrations.sbt.BloopDefaults

val scala3Version = "3.0.0-RC2"
lazy val scalatest = "org.scalatest" %% "scalatest" % "3.2.7"

lazy val AccTest = config("acceptance-test") extend(Test)
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
    libraryDependencies += "org.typelevel" %% "cats-core" % "2.5.0"
  )
