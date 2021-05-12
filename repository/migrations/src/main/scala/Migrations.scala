import com.liyaos.forklift.slick._

object Migrations
    extends App
    with SlickMigrationCommandLineTool
    with SlickMigrationCommands
    with SlickMigrationManager
    with SlickCodegen {
  override def tableNames: Seq[String] = Seq(
    "NOTES"
  )
  override val generatedDir: String = "repository/generated_code/src/main/scala"
  override def pkgName(version: String): String = "bujo." + super.pkgName(version)
  MigrationSummary
  execCommands(args.toList)
}
