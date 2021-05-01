package bujo.domain

import model.error.NoteTextValidationError
import config.MAX_NOTE_TEXT_LENGTH

object validation:
  def validateText(
      text: String
    ): Either[Seq[model.error.ValidationError], String] =
    Either.cond(
      text.length <= MAX_NOTE_TEXT_LENGTH,
      text,
      List(NoteTextValidationError)
    )
