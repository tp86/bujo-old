package bujo.domain

import scala.concurrent.Future
import java.time.LocalDateTime

// Model entities
final case class Tag(val name: String)

opaque type NoteText = String
private[domain] object NoteText:
  def apply(text: String): NoteText = text

final case class Note(text: NoteText, dateCreated: LocalDateTime, tags: Set[Tag])

// Errors
trait Error:
  def message: String

class TextValidationError(private val msg: String) extends Error:
  val message = msg
class NoteSavingError(private val msg: String) extends Error:
  val message = msg

type ValidationError = TextValidationError

type NoteCreationError = ValidationError | NoteSavingError

// Procedures
type NoteCreator = String => Either[Seq[NoteCreationError], Note]
type TextValidator = String => Either[Seq[TextValidationError], NoteText]
type NoteMaker = NoteText => Note
type NoteSaver = Note => Future[Either[Seq[NoteSavingError], Note]]
