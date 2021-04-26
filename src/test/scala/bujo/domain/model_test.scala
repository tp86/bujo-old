package bujo.domain

import scala.language.postfixOps
import org.scalatest.flatspec
import scala.concurrent.ExecutionContext.Implicits.global

import java.time.LocalDateTime

class CreateNote extends flatspec.FixtureAsyncFlatSpec:

  case class FixtureParam(text: String)

  def withFixture(test: OneArgAsyncTest) =
    super.withFixture(test.toNoArgAsyncTest(FixtureParam("Some note text")))

  "a Note" should "be created given valid NoteText" in { f =>
    import bujo.domain.givens.saver
    createNote(f.text).value map { e =>
      e match
        case Left(errs) => fail(errs.map(_.message).mkString("\n"))
        case Right(note) => {
          assert(note.isInstanceOf[Note])
          assert(note.text equals "Some note text")
        }
    }
  }

  it should " be created with current timestamp" in { f =>
    import bujo.domain.givens.saver
    createNote(f.text).value map { e =>
      e match
        case Left(errs) => fail(errs.map(_.message).mkString("\n"))
        case Right(note) => {
          assert(note.dateCreated isAfter (LocalDateTime.now minusSeconds 1))
          assert(note.dateCreated isBefore LocalDateTime.now)
        }
    }
  }

  it should "be created with empty Tag set" in { f =>
    import bujo.domain.givens.saver
    createNote(f.text).value map { e =>
      e match
        case Left(errs) => fail(errs.map(_.message).mkString("\n"))
        case Right(note) => {
          assert(note.tags.isEmpty)
        }
    }
  }

  it should "return a validation error when text is too long" in { _ =>
    import bujo.domain.givens.saver
    createNote("very long text ".repeat(18)).value map { e =>
      e match
        case Left(errs) => {
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
        }
        case Right(_) => fail("should return validation error but did not")
    }
  }

  it should "return a saving error when saving process failed" in { f =>
    val msg = "error while saving note"
    given (Note => Unit) = _ => throw java.io.IOException(msg)
    createNote(f.text).value map { e =>
      e match
        case Left(errs) => {
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
        }
        case Right(_) => fail("should return saving error but did not")
    }
  }

end CreateNote
