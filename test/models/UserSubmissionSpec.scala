package models

import org.junit.runner.RunWith
import org.specs2.mutable.Specification
import org.specs2.runner.JUnitRunner
import play.api.test.WithApplication

@RunWith(classOf[JUnitRunner])
class UserSubmissionSpec extends Specification {

  "UserSubmission" should {
    "#createFromSlackPostData creates a UserSubmission instance from SlackPostData" in new WithApplication {
      var data = SlackPostData(token = "lalala", text = "story: 100, total: 10, add: 5, remove: 4", username = "matt")
      var result: UserSubmission = UserSubmission.createFromSlackPostData(data)

      result.text must equalTo(data.text)
      result.username must equalTo(data.username)
    }

    "#isValid returns true for a valid UserSubmission" in new WithApplication {
      var userSubmission = UserSubmission(text="story: 1, total: 4, add: 0, remove: 1", username = "matt")

      userSubmission.isValid() must equalTo(true)
    }

    "#isValid returns false for an invalid UserSubmission" in new WithApplication {
      var userSubmission = UserSubmission(text="", username = "matt")

      userSubmission.isValid() must equalTo(false)
    }

    "after calling #isValid on a UserSubmission with no story present in text field, userSubmission.errors includes a message indicating story is required" in new WithApplication {
      var userSubmission = UserSubmission(text="", username = "matt")
      userSubmission.isValid()

      userSubmission.errors.mkString("\n") must contain("Story is required")
    }

    "after calling #isValid on a UserSubmission with no total time present in text field, userSubmission.errors includes a message indicating total time is required" in new WithApplication {
      var userSubmission = UserSubmission(text="", username = "matt")
      userSubmission.isValid()

      userSubmission.errors.mkString("\n") must contain("Total time is required")
    }

    "after calling #isValid on a UserSubmission with no time adding technical debt present in text field, userSubmission.errors includes a message indicating time adding technical debt is required" in new WithApplication {
      var userSubmission = UserSubmission(text="", username = "matt")
      userSubmission.isValid()

      userSubmission.errors.mkString("\n") must contain("Time adding technical debt is required")
    }

    "after calling #isValid on a UserSubmission with no time removing technical debt present in text field, userSubmission.errors includes a message indicating time removing technical debt is required" in new WithApplication {
      var userSubmission = UserSubmission(text="", username = "matt")
      userSubmission.isValid()

      userSubmission.errors.mkString("\n") must contain("Time removing technical debt is required")
    }
  }
}
