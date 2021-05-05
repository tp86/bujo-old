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

    extension (text: NoteText)
      def contains(str: String): Boolean = text contains str
  export text.*

  final case class Note(
      text: NoteText)
  object Note:
    def apply(text: NoteText): Note = new Note(text)

    def create(text: String): Either[Seq[error.Error], Note] =
      NoteText.create(text) map Note.apply

    def search(
        textToFind: String
      )(using noteRepository: NoteRepository
      ): List[Note] =
      noteRepository.getAll.filter(_.text contains textToFind)
