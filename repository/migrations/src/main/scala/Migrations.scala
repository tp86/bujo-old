import com.liyaos.forklift.slick._

import config.RepoSettings

object Migrations
    extends App
    with SlickMigrationCommandLineTool
    with SlickMigrationCommands
    with SlickMigrationManager
    with SlickCodegen {
  override def tableNames: Seq[String] = Seq(
    "NOTES",
  )
  val conf = RepoSettings()
  override val generatedDir: String = conf.location
  override def pkgName(version: String): String =
    Seq(conf.pkgPrefix, super.pkgName(version)).mkString(".")
  MigrationSummary
  execCommands(args.toList)
}
