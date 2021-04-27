package bujo.domain

import cats.data.EitherT
import scala.concurrent.Future
import bujo.domain.model.Note
import bujo.domain.model.error.*
import cats.implicits.{catsSyntaxEither, catsStdInstancesForFuture}
import scala.concurrent.ExecutionContext.Implicits.global

object impl extends bujo.domain.api.Note:
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
  override def createNote(
      text: String
    )(using Note => Unit
    ): EitherT[Future, List[NoteCreationError], Note] =
    val validNote = Note.create(text) leftMap (_.map(NoteCreationError.apply))
    EitherT
      .fromEither(validNote)
      .leftMap(_.toList)
      .flatMap { note =>
        saveNote(note) leftMap (err => List(NoteCreationError(err)))
      }
end impl
