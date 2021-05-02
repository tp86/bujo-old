package bujo.domain

import bujo.domain.validation.validateText
import bujo.domain.validation.error.ValidationError

object model:
  object error:
    trait Error:
      def message: String

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
    def create(text: String): Either[Seq[error.Error], Note] =
      NoteText.create(text) map Note.apply
