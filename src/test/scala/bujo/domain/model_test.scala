package bujo.domain

import scala.language.postfixOps
import org.scalatest.{Outcome, Failed}
import org.scalatest.flatspec
import scala.concurrent.ExecutionContext.Implicits.global
import scala.util.{Success, Failure}

import java.time.LocalDateTime

class CreateNote extends flatspec.FixtureAsyncFlatSpec:

  case class FixtureParam(text: String)

  def withFixture(test: OneArgAsyncTest) = 
    super.withFixture(test.toNoArgAsyncTest(FixtureParam("Some note text")))

  "a Note" should "be created given valid NoteText" in { f =>
    import bujo.domain.givens.saver
    createNote(f.text).value map { e =>
      e match
        case Left(errs) => fail(errs.map(_.message).mkString("\n"))
        case Right(note) => assert(note.isInstanceOf[Note])
    }
  }
  
  it should " be created with current timestamp" in { f =>
    import bujo.domain.givens.saver
    createNote(f.text).value map { e =>
      e match
        case Left(errs) => fail(errs.map(_.message).mkString("\n"))
        case Right(note) => {
          assert(note.dateCreated isAfter (LocalDateTime.now minusSeconds 1))
          assert(note.dateCreated isBefore LocalDateTime.now)
        }
    }
  }
  
  it should "be created with empty Tag set" in { f =>
    import bujo.domain.givens.saver
    createNote(f.text).value map { e =>
      e match
        case Left(errs) => fail(errs.map(_.message).mkString("\n"))
        case Right(note) => {
          assert(note.tags.isEmpty)
        }
    }
  }
