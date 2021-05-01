package bujo.domain

import org.scalatest.featurespec.AnyFeatureSpec
import org.scalatest.GivenWhenThen

import bujo.domain.model.*
import bujo.domain.validation.validateText

class NoteFeatureTest extends AnyFeatureSpec, GivenWhenThen:

  Feature("Note") {
    Scenario("user creates note with valid text") {
      Given("user provides valid text")
      val text: String = "Some note text"
      assert(validateText(text).isRight)
      
      When("user creates note")
      val note = createNote(text)

      Then("note is created")
      note match 
        case Right(n) =>
          assert(n.isInstanceOf[Note])
          assert(n.text equals text)
        case Left(errs) => fail(s"failed with errors:\n\t${errs.map(_.message).mkString("\n\t")}")
    }
    
    Scenario("user creates note with invalid text") {
      Given("user provides invalid text")
      val invalidText: String = "invalid text".repeat(22)
      assert(validateText(invalidText).isLeft)
      
      When("user creates note")
      val note = createNote(invalidText)

      Then("note text validation error is returned")
      note match
        case Left(errs) =>
          assert(errs contains error.NoteTextValidationError)
        case Right(_) => fail("note should not be created with invalid text")
    }
  }
