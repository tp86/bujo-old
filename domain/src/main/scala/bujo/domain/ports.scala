package bujo.domain

import model.{error => _, *}
import model.error.Error

trait NoteRepository:
  object error:
    trait NoteSavingError extends Error
  def getAll: List[Note]
  def save(note: Note): Either[Seq[error.NoteSavingError], Note]

trait NoteApi:
  def create(text: String): Either[Seq[Error], Note]
  def search(text: String): List[Note]
