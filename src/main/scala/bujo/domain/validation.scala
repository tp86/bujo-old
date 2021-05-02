package bujo.domain

import model.error.Error
import config.MAX_NOTE_TEXT_LENGTH

object validation:
  object error:
    trait ValidationError extends Error
    trait NoteTextValidationError extends ValidationError
    object NoteTextIsTooLongError extends NoteTextValidationError:
      val message = s"Note text cannot be longer than ${MAX_NOTE_TEXT_LENGTH} characters"
  def validateText(
      text: String
    ): Either[Seq[error.NoteTextValidationError], String] =
    Either.cond(
      text.length <= MAX_NOTE_TEXT_LENGTH,
      text,
      List(error.NoteTextIsTooLongError)
    )
