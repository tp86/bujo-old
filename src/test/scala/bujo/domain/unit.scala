package bujo.domain

import scala.language.postfixOps
import org.scalatest.flatspec
import scala.concurrent.ExecutionContext.Implicits.global

import java.time.LocalDateTime

import bujo.domain.model.*

abstract class NoteTestBase extends flatspec.AnyFlatSpec:
  private def failWithErrors(errors: Seq[error.Error]) =
    fail(errors.map(_.message).mkString("\n"))
  
  protected def assertNote(maybeNote: Either[Seq[error.Error], Note])(assertionBody: Note => Unit): Unit =
    maybeNote match
      case Left(errs) => failWithErrors(errs)
      case Right(note) => assertionBody(note)

end NoteTestBase

class NoteTest extends NoteTestBase:

  "a Note" should "be created given valid NoteText" in {
    val validText = "Some note text"
    val noteText = NoteText.create(validText)
    assertNote(noteText.map(Note(_))) { note =>
      assert(note.isInstanceOf[Note])
      assert(note.text equals validText)
    }
  }

end NoteTest
