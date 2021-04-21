package bujo.domain

import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global
import java.time.LocalDateTime
import cats.data.{EitherT, ValidatedNel}
import cats.implicits.{catsSyntaxEither, catsStdInstancesForFuture, catsSyntaxValidatedId}
import cats.syntax.apply.*

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
  def message: String = this match
    case ValidationError(err) => err.message
    case SavingError(err) => err.message
object NoteCreationError:
  def apply(err: Error): NoteCreationError = 
    err match
      case e: TextValidationError => ValidationError(e)
      case e: NoteSavingError => SavingError(e)
      
// Validations
def validateText(text: String): ValidatedNel[TextValidationError, String] = 
  if text.length < 256 then
    text.validNel
  else
    TextValidationError("Note text cannot be longer than 255 characters").invalidNel

// Procedures
object NoteText:
  def apply(text: String): NoteText = text

object Note:
  def apply(text: NoteText): Note = new Note(text, LocalDateTime.now, Set.empty)
  def create(text: String): Either[List[TextValidationError], Note] = 
    validateText(text).map(NoteText.apply andThen Note.apply).toEither.leftMap(_.toList)

def saveNote(note: Note): EitherT[Future, NoteSavingError, Note] = EitherT.fromEither(Right(note))

def createNote(text: String): EitherT[Future, List[NoteCreationError], Note] = 
  val validNote = Note.create(text) leftMap (_.map(NoteCreationError.apply))
  EitherT.fromEither(validNote) flatMap {
    note => saveNote(note) leftMap (err => List(NoteCreationError(err)))
  }
