package bujo.domain

import java.time.LocalDateTime
import cats.data.{Validated,NonEmptyChain}

def makeNote: NoteMaker = text => Note(text, LocalDateTime.now, Set.empty)

def validateText: TextValidator = ???

def createNote: NoteCreator = s => validateText(s) map makeNote
