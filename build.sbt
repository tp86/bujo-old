import sbt.librarymanagement.CrossVersionFunctions
import bloop.integrations.sbt.BloopDefaults

ThisBuild / version := "0.1.0"
ThisBuild / scalaVersion := "3.0.0-RC2"

lazy val scalatest = "org.scalatest" %% "scalatest" % "3.2.7"

lazy val bujo = project
  .in(file("."))
  .aggregate(domain, accTests, repo)
  .settings(
    name := "bujo",
  )

lazy val domainDeps = Seq(
  scalatest % Test,
  "org.typelevel" %% "cats-core" % "2.5.0",
)

lazy val domain = project
  .in(file("domain"))
  .settings(
    name := "bujo-domain",
    Test / jacocoReportSettings := JacocoReportSettings()
      .withFormats(JacocoReportFormats.ScalaHTML, JacocoReportFormats.XML),
    libraryDependencies ++= domainDeps,
  )

lazy val accTestsDeps = Seq(
  scalatest % Test,
)

lazy val accTests = project
  .in(file("acceptance-tests"))
  .dependsOn(domain)
  .settings(
    name := "bujo-acceptance-tests",
    libraryDependencies ++= accTestsDeps,
  )

lazy val repo = project
  .in(file("repository"))
  .dependsOn(domain)
  .settings(
    name := "bujo-repository",
  )
