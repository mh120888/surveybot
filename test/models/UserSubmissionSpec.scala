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

    "#validate returns a List containing an error message for a UserSubmission with no story present in text field" in new WithApplication {
      var userSubmission = UserSubmission(text="", username = "matt")
      userSubmission.validate

      userSubmission.errors.mkString("\n") must contain("Story is required")
    }

    "#validate returns a List containing an error message for a UserSubmission with no total time present in text field" in new WithApplication {
      var userSubmission = UserSubmission(text="", username = "matt")
      userSubmission.validate

      userSubmission.errors.mkString("\n") must contain("Total time is required")
    }

    "#validate returns a List containing an error message for a UserSubmission with no time adding technical debt present in text field" in new WithApplication {
      var userSubmission = UserSubmission(text="", username = "matt")
      userSubmission.validate

      userSubmission.errors.mkString("\n") must contain("Time adding technical debt is required")
    }

    "#validate returns a List containing an error message for a UserSubmission with no time removing technical debt present in text field" in new WithApplication {
      var userSubmission = UserSubmission(text="", username = "matt")
      userSubmission.validate

      userSubmission.errors.mkString("\n") must contain("Time removing technical debt is required")
    }
  }
}
