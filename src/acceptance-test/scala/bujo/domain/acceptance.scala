package bujo.domain

import org.scalatest.featurespec.AnyFeatureSpec
import org.scalatest.GivenWhenThen

import bujo.domain.model.{error => _, *}
import bujo.domain.validation.*

class NoteFeatureTest extends AnyFeatureSpec, GivenWhenThen:

  Feature("Basic Note operations") {
    Scenario("User creates note with valid text") {
      Given("User provides valid text")
      val text: String = "Some note text"
      assert(validateText(text).isRight)

      When("User creates note")
      val note = Note.create(text)

      Then("Note is created")
      note match
        case Right(n) =>
          assert(n.isInstanceOf[Note])
          assert(n.text equals text)
        case Left(errs) =>
          fail(
            s"Failed with errors:\n\t${errs.map(_.message).mkString("\n\t")}"
          )
    }

    Scenario("User creates note with invalid text") {
      Given("User provides invalid text")
      val invalidText: String = "invalid text".repeat(22)
      assert(validateText(invalidText).isLeft)

      When("User creates note")
      val note = Note.create(invalidText)

      Then("Note text validation error is returned")
      note match
        case Left(errs) =>
          assert(errs exists (_.isInstanceOf[error.NoteTextValidationError]))
        case Right(_) => fail("Note should not be created with invalid text")
    }

    Scenario("User searches for Notes matching text") {
      Given("Existing Notes")
      val notes = List("Some note text", "Interesting text")
        .map(Note.create)
        .map {
          case Left(errs) =>
            fail(
              s"Failed with errors:\n\t${errs.map(_.message).mkString("\n\t")}"
            )
          case Right(note) => note
        }
      
        When("User seaches for Notes using text")
        pending
        
        Then("Matching Notes are returned")
        pending
    }
  }
