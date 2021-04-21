package bujo.domain

import scala.language.postfixOps
import org.scalatest.{Outcome, Failed}
import org.scalatest.flatspec
import scala.concurrent.ExecutionContext.Implicits.global
import scala.util.{Success, Failure}

import java.time.LocalDateTime

class NoteMakerTest extends flatspec.FixtureAsyncFlatSpec:

  case class FixtureParam(text: String)

  def withFixture(test: OneArgAsyncTest) = 
    super.withFixture(test.toNoArgAsyncTest(FixtureParam("Some note text")))

  "createNote" should "create a Note given valid NoteText" in { f =>
    createNote(f.text).value map { e =>
      e match
        case Left(errs) => fail(errs.map(_.message).mkString("\n"))
        case Right(note) => assert(note.isInstanceOf[Note])
    }
  }
  
  it should "create a Note with current timestamp" in { f =>
    createNote(f.text).value map { e =>
      e match
        case Left(errs) => fail(errs.map(_.message).mkString("\n"))
        case Right(note) => {
          assert(note.dateCreated isAfter (LocalDateTime.now minusSeconds 1))
          assert(note.dateCreated isBefore LocalDateTime.now)
        }
    }
  }
  
  it should "create a Note with empty Tag set" in { f =>
    createNote(f.text).value map { e =>
      e match
        case Left(errs) => fail(errs.map(_.message).mkString("\n"))
        case Right(note) => {
          assert(note.tags.isEmpty)
        }
    }
  }
