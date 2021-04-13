package bujo.domain

import scala.language.postfixOps
import org.scalatest.*
import flatspec.*
import matchers.*
import java.time.LocalDateTime

import bujo.domain.Entry.*

class ModelTest extends AnyFlatSpec with should.Matchers:

  "A Note" should "be created with current date" in {
    val note = Note("")
    assert(note.dateCreated isBefore LocalDateTime.now)
    assert(note.dateCreated isAfter (LocalDateTime.now minusSeconds 1))
  }
  
  it should "be created with empty tag list" in {
    val note = Note("")
    assert(note.tags isEmpty)
  }
