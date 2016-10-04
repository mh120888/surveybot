package models

import org.junit.runner.RunWith
import org.specs2.mutable.Specification
import org.specs2.runner.JUnitRunner

@RunWith(classOf[JUnitRunner])
class UserSubmissionSpec extends Specification {

  "UserSubmission" should {
    "#createFromSlackPostData creates a UserSubmission instance from SlackPostData" in {
      val data = SlackPostData(token = "lalala", text = "BUG TM-99 5 20%", username = "matt")
      val result: UserSubmission = UserSubmission.createFromSlackPostData(data)

      result.text must equalTo(data.text)
      result.username must equalTo(data.username)
    }

    "#isValid returns true for a valid UserSubmission" in {
      val userSubmission = UserSubmission(text="STORY TM-100 4 40%", username = "matt")

      userSubmission.isValid() must equalTo(true)
    }

    "#isValid returns false for an invalid UserSubmission" in {
      val userSubmission = UserSubmission(text="", username = "matt")

      userSubmission.isValid() must equalTo(false)
    }
  }
}
