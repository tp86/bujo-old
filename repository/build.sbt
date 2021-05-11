addCommandAlias("migrate", "migrations/run")

lazy val repo = project
  .in(file("."))
  .aggregate(migrations, generatedCode)
  .settings(
    scalaVersion := "2.13.5",
  )

lazy val migrationsDeps = Seq(
  "com.typesafe.slick" %% "slick" % "3.3.3",
  "com.liyaos" %% "scala-forklift-slick" % "0.3.2",
  "com.typesafe.slick" %% "slick-hikaricp" % "3.3.3",
  "org.slf4j" % "slf4j-nop" % "2.0.0-alpha1",
  "org.xerial" % "sqlite-jdbc" % "3.34.0",
)

lazy val migrations = project
  .in(file("migrations"))
  .dependsOn(generatedCode)
  .settings(
    scalaVersion := "2.13.5",
    resolvers += Resolver.jcenterRepo,
    libraryDependencies ++= migrationsDeps,
    Compile / unmanagedResourceDirectories += baseDirectory.value / ".." / "src/main/resources/",
    Compile / resourceGenerators += Def.task {
      val file = baseDirectory.value / "src/main/resources" / "migrations.conf"
      val contents = s"handled_location=${(baseDirectory.value / "src/main/scala").getCanonicalPath}"
      IO.write(file, contents)
      Seq(file)
    }.taskValue,
  )

lazy val generatedCode = project
  .in(file("generated_code"))
  .settings(
    scalaVersion := "2.13.5",
  )
