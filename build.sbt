val scala3Version = "3.0.0-RC2"

lazy val root = project
  .in(file("."))
  .settings(
    name := "bujo-domain",
    version := "0.1.0",

    scalaVersion := scala3Version,

    libraryDependencies += "org.scalatest" %% "scalatest" % "3.2.7" % "test",
    libraryDependencies += "org.typelevel" %% "cats-core" % "2.5.0"
  )
