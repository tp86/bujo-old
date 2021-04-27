package bujo.domain

import scala.language.postfixOps
import org.scalatest.flatspec
import scala.concurrent.ExecutionContext.Implicits.global

import java.time.LocalDateTime

import bujo.domain.impl.*
import bujo.domain.Model.Note

class CreateNote extends flatspec.FixtureAsyncFlatSpec:

  private def saver: (Note => Unit) = _ => ()

  case class FixtureParam(text: String)

  def withFixture(test: OneArgAsyncTest) =
    super.withFixture(test.toNoArgAsyncTest(FixtureParam("Some note text")))

  "a Note" should "be created given valid NoteText" in { f =>
    given (Note => Unit) = saver
    createNote(f.text).value map {
      case Left(errs) => fail(errs.map(_.message).mkString("\n"))
      case Right(note) =>
        assert(note.isInstanceOf[Note])
        assert(note.text equals "Some note text")
    }
  }

  it should " be created with current timestamp" in { f =>
    given (Note => Unit) = saver
    createNote(f.text).value map {
      case Left(errs) => fail(errs.map(_.message).mkString("\n"))
      case Right(note) =>
        assert(note.dateCreated isAfter (LocalDateTime.now minusSeconds 1))
        assert(note.dateCreated isBefore LocalDateTime.now)
    }
  }

  it should "be created with empty Tag set" in { f =>
    given (Note => Unit) = saver
    createNote(f.text).value map {
      case Left(errs) => fail(errs.map(_.message).mkString("\n"))
      case Right(note) => assert(note.tags.isEmpty)
    }
  }

  it should "return a validation error when text is too long" in { _ =>
    given (Note => Unit) = saver
    createNote("very long text ".repeat(18)).value map {
      case Left(errs) =>
        assert(
          errs
            .filter(_.isInstanceOf[NoteCreationError.ValidationError])
            .size > 0
        )
        assert(
          errs
            .find(_.isInstanceOf[NoteCreationError.ValidationError])
            .get
            .message == "Note text cannot be longer than 255 characters"
        )
      case Right(_) => fail("should return validation error but did not")
    }
  }

  it should "return a saving error when saving process failed" in { f =>
    val msg = "error while saving note"
    given (Note => Unit) = _ => throw java.io.IOException(msg)
    createNote(f.text).value map {
      case Left(errs) =>
        assert(
          errs
            .filter(_.isInstanceOf[NoteCreationError.SavingError])
            .size > 0
        )
        assert(
          errs
            .find(_.isInstanceOf[NoteCreationError.SavingError])
            .get
            .message == msg
        )
      case Right(_) => fail("should return saving error but did not")
    }
  }

end CreateNote
