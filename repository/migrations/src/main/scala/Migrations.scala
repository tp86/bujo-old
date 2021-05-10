import com.liyaos.forklift.slick._

object Migrations extends App 
with SlickMigrationCommandLineTool
with SlickMigrationCommands
with Codegen {
  MigrationSummary
  execCommands(args.toList)
}
