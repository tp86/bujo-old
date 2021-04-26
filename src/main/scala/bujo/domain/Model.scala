package bujo.domain

import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global
import scala.util.{Success, Failure}
import java.time.LocalDateTime
import cats.data.EitherT
import cats.implicits.{
  catsSyntaxEither,
  catsStdInstancesForFuture,
}
import bujo.extensions.sequenceLeft

// Model entities
final case class Tag(val name: String)

opaque type NoteText = String

final case class Note(
    text: NoteText,
    dateCreated: LocalDateTime,
    tags: Set[Tag])

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
    case SavingError(err)     => err.message
object NoteCreationError:
  def apply(err: Error): NoteCreationError =
    err match
      case e: TextValidationError => ValidationError(e)
      case e: NoteSavingError     => SavingError(e)

// Validations
def validateTextLength(text: String): Either[TextValidationError, String] =
  if text.length < 256 then Right(text)
  else
    Left(
      TextValidationError(
        "Note text cannot be longer than 255 characters"
      )
    )

def validateText(text: String): Either[Seq[TextValidationError], String] =
  List(validateTextLength(text)).sequenceLeft

// Procedures
object NoteText:
  private[domain] def apply(text: String): NoteText = text
  
object Note:
  private def apply(text: NoteText): Note =
    new Note(text, LocalDateTime.now, Set.empty)
  def create(text: String): Either[Seq[TextValidationError], Note] =
    validateText(text)
      .map(NoteText.apply andThen Note.apply)

private def saveNote(
    note: Note
  )(using saver: Note => Unit
  ): EitherT[Future, NoteSavingError, Note] =
  val fnote = Future(saver.apply(note))
    .map(_ => Right(note))
    .recover { case error =>
      Left(NoteSavingError(error.getMessage))
    }
  EitherT(fnote)

def createNote(
    text: String
  )(using Note => Unit
  ): EitherT[Future, List[NoteCreationError], Note] =
  val validNote = Note.create(text) leftMap (_.map(NoteCreationError.apply))
  EitherT.fromEither(validNote)
    .leftMap(_.toList)
    .flatMap { note =>
      saveNote(note) leftMap (err => List(NoteCreationError(err)))
    }
