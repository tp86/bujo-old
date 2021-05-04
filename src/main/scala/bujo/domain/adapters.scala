package bujo.domain

import model.*
import bujo.domain.NoteApi

object NoteApiImpl extends NoteApi:
  def create(text: String) = Note.create(text)
  def search(text: String): List[Note] =
    given NoteRepository = NoteRepository
    Note.search(text)

val NoteRepository = new NoteRepository {
  def save(note: Note): Either[Seq[error.NoteSavingError], Note] =
    ???
  def getAll: List[Note] = ???
}
