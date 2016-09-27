package models

import org.junit.runner.RunWith
import org.specs2.mutable.Specification
import org.specs2.runner.JUnitRunner
import play.api.test.WithApplication

@RunWith(classOf[JUnitRunner])
class UserSubmissionSpec extends Specification {

  "UserSubmission" should {
    "#createFromSlackPostData creates a UserSubmission instance from SlackPostData" in {
      var data = SlackPostData(token = "lalala", text = "story: 100, total: 10, add: 5, remove: 4", username = "matt")
      var result: UserSubmission = UserSubmission.createFromSlackPostData(data)

      result.text must equalTo(data.text)
      result.username must equalTo(data.username)
    }

    "#isValid returns true for a valid UserSubmission" in {
      var userSubmission = UserSubmission(text="story: 1, total: 4, add: 0, remove: 1", username = "matt")

      userSubmission.isValid() must equalTo(true)
    }

    "#isValid returns false for an invalid UserSubmission" in {
      var userSubmission = UserSubmission(text="", username = "matt")

      userSubmission.isValid() must equalTo(false)
    }

    "after calling #isValid on a UserSubmission with no story present in text field, userSubmission.errors includes a message indicating story is required" in {
      var userSubmission = UserSubmission(text="", username = "matt")
      userSubmission.isValid()

      userSubmission.getErrors().mkString("\n") must contain("Story is required")
    }

    "after calling #isValid on a UserSubmission with no total time present in text field, userSubmission.errors includes a message indicating total time is required" in {
      var userSubmission = UserSubmission(text="", username = "matt")
      userSubmission.isValid()

      userSubmission.getErrors().mkString("\n") must contain("Total time is required")
    }

    "after calling #isValid on a UserSubmission with no time adding technical debt present in text field, userSubmission.errors includes a message indicating time adding technical debt is required" in {
      var userSubmission = UserSubmission(text="", username = "matt")
      userSubmission.isValid()

      userSubmission.getErrors().mkString("\n") must contain("Time adding technical debt is required")
    }

    "after calling #isValid on a UserSubmission with no time removing technical debt present in text field, userSubmission.errors includes a message indicating time removing technical debt is required" in {
      var userSubmission = UserSubmission(text="", username = "matt")
      userSubmission.isValid()

      userSubmission.getErrors().mkString("\n") must contain("Time removing technical debt is required")
    }

    "after calling #isValid on a UserSubmission with text including 'total: 15', userSubmission.errors includes a message indicating total time must be in range" in {
      var userSubmission = UserSubmission(text="story: 1, total: 15, add: 0, remove: 0", username = "matt")
      userSubmission.isValid()

      userSubmission.getErrors().mkString("\n") must contain("Total time must be in the range 0-12")
    }

    "after calling #isValid on a UserSubmission with text including 'add: 15', userSubmission.errors includes a message indicating time adding technical debt must be in range" in {
      var userSubmission = UserSubmission(text="story: 1, total: 15, add: 15, remove: 0", username = "matt")
      userSubmission.isValid()

      userSubmission.getErrors().mkString("\n") must contain("Time adding technical debt must be in the range 0-12")
    }

    "after calling #isValid on a UserSubmission with text including 'remove: 15', userSubmission.errors includes a message indicating time removing technical debt must be in range" in {
      var userSubmission = UserSubmission(text="story: 1, total: 15, add: 0, remove: 15", username = "matt")
      userSubmission.isValid()

      userSubmission.getErrors().mkString("\n") must contain("Time removing technical debt must be in the range 0-12")
    }
  }
}
