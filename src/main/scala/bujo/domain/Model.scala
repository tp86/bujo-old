package bujo.domain

import scala.concurrent.Future
import cats.data.EitherT
import scala.concurrent.ExecutionContext.Implicits.global
import scala.util.{Success, Failure}
import java.time.LocalDateTime
import bujo.extensions.sequenceLeft

object model:
  final case class Tag(val name: String)

  opaque type NoteText = String

  final case class Note(
      text: NoteText,
      dateCreated: LocalDateTime,
      tags: Set[Tag])

  object NoteText:
    private[domain] def apply(text: String): NoteText = text

  object error:
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
    
  object Note:
    private def apply(text: NoteText): Note =
      new Note(text, LocalDateTime.now, Set.empty)
    def create(text: String): Either[Seq[error.TextValidationError], model.Note] =
      validateText(text)
        .map(NoteText.apply andThen Note.apply)

// Validations
def validateTextLength(text: String): Either[model.error.TextValidationError, String] =
  if text.length < 256 then Right(text)
  else
    Left(
      model.error.TextValidationError(
        "Note text cannot be longer than 255 characters"
      )
    )

def validateText(text: String): Either[Seq[model.error.TextValidationError], String] =
  List(validateTextLength(text)).sequenceLeft
