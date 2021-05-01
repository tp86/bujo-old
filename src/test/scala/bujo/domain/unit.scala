package bujo.domain

import scala.language.postfixOps
import org.scalatest.flatspec
import scala.concurrent.ExecutionContext.Implicits.global

import java.time.LocalDateTime

import bujo.domain.model.*

class NoteTest extends flatspec.FixtureAnyFlatSpec:

  case class FixtureParam(text: String)

  def withFixture(test: OneArgTest) =
    super.withFixture(test.toNoArgTest(FixtureParam("Some note text")))

  "a Note" should "be created given valid NoteText" in { f =>
    createNote(f.text) match
      case Left(errs) => fail(errs.map(_.message).mkString("\n"))
      case Right(note) =>
        assert(note.isInstanceOf[Note])
        assert(note.text equals "Some note text")
  }

end NoteTest
