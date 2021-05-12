import slick.jdbc.SQLiteProfile.api._
import com.liyaos.forklift.slick.DBIOMigration


object M1 {
  class Notes(tag: Tag) extends Table[(Long, String)](tag, "NOTES") {
    def id = column[Long]("ID", O.PrimaryKey)
    def text = column[String]("TEXT", O.SqlType("VARCHAR(255)"))
    def * = (id, text)
  }
  val notes = TableQuery[Notes]
  Migrations.migrations = Migrations.migrations :+ DBIOMigration(1)(
    DBIO.seq(
      notes.schema.createIfNotExists
    ))
}
