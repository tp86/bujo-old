import com.liyaos.forklift.slick._

object Migrations
    extends App
    with SlickMigrationCommandLineTool
    with SlickMigrationCommands
    with SlickMigrationManager
    with SlickCodegen {
  override def tableNames: Seq[String] = Seq(
    "notes"
  )
  execCommands(args.toList)
}
