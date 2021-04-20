package bujo.domain

import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global
import java.time.LocalDateTime
import cats.data.EitherT
import cats.implicits.{catsSyntaxEither, catsStdInstancesForFuture}

// Model entities
final case class Tag(val name: String)

opaque type NoteText = String

final case class Note(text: NoteText, dateCreated: LocalDateTime, tags: Set[Tag])

// Errors
trait Error:
  def message: String

class TextValidationError(private val msg: String) extends Error:
  val message = msg
class NoteSavingError(private val msg: String) extends Error:
  val message = msg
enum NoteCreationError:
  case ValidationError(err: TextValidationError)
  case SavingError(err: NoteSavingError)

// Procedures
object NoteText:
  def apply(text: String): NoteText = text
def validateText(text: String): Either[TextValidationError, NoteText] = ???
object Note:
  def apply(text: NoteText): Note = new Note(text, LocalDateTime.now, Set.empty)
def saveNote(note: Note): EitherT[Future, NoteSavingError, Note] = ???
def createNote(text: String): EitherT[Future, NoteCreationError, Note] = 
  val validatedNote = validateText(text) map Note.apply leftMap NoteCreationError.ValidationError.apply
  EitherT.fromEither(validatedNote) flatMap {
    note => saveNote(note) leftMap NoteCreationError.SavingError.apply
  }
