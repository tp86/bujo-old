import com.liyaos.forklift.slick._
import com.typesafe.config.{Config, ConfigFactory}

object Migrations
    extends App
    with SlickMigrationCommandLineTool
    with SlickMigrationCommands
    with SlickMigrationManager
    with SlickCodegen {
  override def tableNames: Seq[String] = Seq(
    "NOTES"
  )
  val conf = ConfigFactory.load("repo")
  override val generatedDir: String = conf.getString("repo.generated_code.location")
  override def pkgName(version: String): String = "bujo." + super.pkgName(version)
  MigrationSummary
  execCommands(args.toList)
}
