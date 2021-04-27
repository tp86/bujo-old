package bujo.domain

import scala.concurrent.Future
import cats.data.EitherT

import bujo.domain.model
import bujo.domain.model.error.*

object api:
  trait Note:
    def createNote(text: String)(using model.Note => Unit): EitherT[Future, List[NoteCreationError], model.Note]
