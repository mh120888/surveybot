package models

import org.junit.runner.RunWith
import org.specs2.mutable.Specification
import org.specs2.runner.JUnitRunner

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

    s"after calling #isValid on a UserSubmission with no story present in text field, userSubmission.errors includes ${UserSubmission.STORY_REQUIRED}" in {
      var userSubmission = UserSubmission(text="total: 1", username = "matt")
      userSubmission.isValid()

      userSubmission.getErrors.mkString("\n") must contain(UserSubmission.STORY_REQUIRED)
    }

    s"after calling #isValid on a UserSubmission with no total time present in text field, userSubmission.errors includes ${UserSubmission.TOTAL_TIME_REQUIRED}" in {
      var userSubmission = UserSubmission(text="", username = "matt")
      userSubmission.isValid()

      userSubmission.getErrors.mkString("\n") must contain(UserSubmission.TOTAL_MUST_BE_IN_RANGE)
    }

    s"after calling #isValid on a UserSubmission with no time adding technical debt present in text field, userSubmission.errors includes ${UserSubmission.ADD_TECH_DEBT_REQUIRED}" in {
      var userSubmission = UserSubmission(text="", username = "matt")
      userSubmission.isValid()

      userSubmission.getErrors.mkString("\n") must contain(UserSubmission.ADD_TECH_DEBT_REQUIRED)
    }

    s"after calling #isValid on a UserSubmission with no time removing technical debt present in text field, userSubmission.errors includes ${UserSubmission.REMOVE_TECH_DEBT_REQUIRED}" in {
      var userSubmission = UserSubmission(text="", username = "matt")
      userSubmission.isValid()

      userSubmission.getErrors.mkString("\n") must contain(UserSubmission.REMOVE_TECH_DEBT_REQUIRED)
    }

    s"after calling #isValid on a UserSubmission with text including 'total: 15', userSubmission.errors includes ${UserSubmission.TOTAL_MUST_BE_IN_RANGE}" in {
      var userSubmission = UserSubmission(text="story: 1, total: 15, add: 0, remove: 0", username = "matt")
      userSubmission.isValid()

      userSubmission.getErrors.mkString("\n") must contain(UserSubmission.TOTAL_MUST_BE_IN_RANGE)
    }

    s"after calling #isValid on a UserSubmission with text including 'add: 15', userSubmission.errors includes ${UserSubmission.ADD_MUST_BE_IN_RANGE}" in {
      var userSubmission = UserSubmission(text="story: 1, total: 15, add: 15, remove: 0", username = "matt")
      userSubmission.isValid()

      userSubmission.getErrors.mkString("\n") must contain(UserSubmission.ADD_MUST_BE_IN_RANGE)
    }

    s"after calling #isValid on a UserSubmission with text including 'remove: 15', userSubmission.errors includes ${UserSubmission.REMOVE_MUST_BE_IN_RANGE}" in {
      var userSubmission = UserSubmission(text="story: 1, total: 15, add: 0, remove: 15", username = "matt")
      userSubmission.isValid()

      userSubmission.getErrors.mkString("\n") must contain(UserSubmission.REMOVE_MUST_BE_IN_RANGE)
    }
  }
}
