package bujo.domain

import scala.concurrent.Future
import cats.data.EitherT

import bujo.domain.Model

object api:
  trait Note:
    def createNote(text: String)(using Model.Note => Unit): EitherT[Future, List[NoteCreationError], Model.Note]
