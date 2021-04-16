package bujo.domain

import scala.language.postfixOps
import org.scalatest.flatspec

import java.time.LocalDateTime

class NoteMakerTest extends flatspec.FixtureAnyFlatSpec:

  case class FixtureParam(note: Note)

  def withFixture(test: OneArgTest) = 
    val noteText = NoteText("Some note text")
    val note = makeNote(noteText)
    withFixture(test.toNoArgTest(FixtureParam(note)))

  "A NoteMaker" should "make a Note given NoteText" in { f =>
    assert(f.note.isInstanceOf[Note])
  }
  
  it should "make a Note with current timestamp" in { f =>
    assert(f.note.dateCreated isAfter (LocalDateTime.now minusSeconds 1))
    assert(f.note.dateCreated isBefore LocalDateTime.now)
  }
  
  it should "make a Note with empty Tag set" in { f =>
    assert(f.note.tags.isEmpty)
  }
