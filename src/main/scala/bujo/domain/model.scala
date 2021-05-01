package bujo.domain

import bujo.domain.validation.validateText

object model:
  object error:
    trait Error:
      def message: String
    trait ValidationError extends Error
    object NoteTextValidationError extends ValidationError:
      val message = "Note text is invalid"
  import error.*

  private object text:
    opaque type NoteText = String
    object NoteText:
      private def apply(text: String): NoteText = text
      def create(text: String): Either[Seq[ValidationError], NoteText] =
        validateText(text) map NoteText.apply
  export text.*

  final case class Note(
      text: NoteText)
  object Note:
    private def apply(text: NoteText): Note = new Note(text)
    def createNote(text: String): Either[Seq[Error], Note] =
      NoteText.create(text) map Note.apply
  export Note.createNote
