package models

import org.joda.time.DateTime
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

    "#isValid returns true for a valid UserSubmission for MEETING" in {
      val userSubmission = UserSubmission(text="MEETING 2", username = "tictactoeislife")

      userSubmission.isValid() must equalTo(true)
    }

    "#isValid returns false for an invalid UserSubmission with insufficient parts and include correct error message" in {
      val userSubmission = UserSubmission(text="STORY ABC-25 5", username = "malina")

      userSubmission.isValid() must equalTo(false)
      userSubmission.getErrors.mkString(",") must contain(UserSubmission.REQUIRED_FOR_STORY_OR_BUG)
    }

    "#isValid returns false for an invalid UserSubmission with too many parts and includes correct error message" in {
      val userSubmission = UserSubmission(text="MEETING 5 5", username = "tictactoeislife")

      userSubmission.isValid() must equalTo(false)
      userSubmission.getErrors.mkString(",") must contain(UserSubmission.REQUIRED_FOR_MEETING)
    }

    "#isValid returns false for an invalid UserSubmission with empty text and includes correct error messages" in {
      val userSubmission = UserSubmission(text="", username = "matt")

      userSubmission.isValid() must equalTo(false)
      userSubmission.getErrors.mkString(",") must contain(UserSubmission.ACTIVITY_TYPE_IS_REQUIRED)
      userSubmission.getErrors.mkString(",") must contain(UserSubmission.REQUIRED_FOR_STORY_OR_BUG)
      userSubmission.getErrors.mkString(",") must contain(UserSubmission.REQUIRED_FOR_MEETING)
    }

    "#isValid returns false for an invalid UserSubmission with wrong activity type and includes correct error messages" in {
      val userSubmission = UserSubmission(text = "STANDUP 5", username = "malina")

      userSubmission.isValid() must equalTo(false)
      userSubmission.getErrors.mkString(",") must contain(UserSubmission.ACTIVITY_TYPE_IS_REQUIRED)
      userSubmission.getErrors.mkString(",") must contain(UserSubmission.REQUIRED_FOR_STORY_OR_BUG)
      userSubmission.getErrors.mkString(",") must contain(UserSubmission.REQUIRED_FOR_MEETING)
    }

    "#isStory returns true if the submission is a story" in {
      val userSubmission = UserSubmission(text = "STORY TSF-44 5 50%", username = "Bob")

      userSubmission.isStory must equalTo(true)
    }

    "#isBug returns true if the submision is a bug" in {
      val userSubmission = UserSubmission(text = "BUG TSF-44 5 50%", username = "Bob")

      userSubmission.isBug must equalTo(true)
    }

    "#isMeeting returns true if the submision is a meeting" in {
      val userSubmission = UserSubmission(text = "Meeting 2", username = "Bob")

      userSubmission.isMeeting must equalTo(true)
    }

    "#showDate returns a properly formatted date" in {
      val currentDate = new DateTime()
      val userSubmission = UserSubmission(text = "STORY TSF-44 5 50%", username = "Bob", createdAt = currentDate)

      userSubmission.showDate must equalTo(s"${currentDate.dayOfWeek.getAsText} ${currentDate.toString("MM/d/yyyy")}")
    }

    "#showActivityID returns the activityID for a STORY" in {
      val currentDate = new DateTime()
      val userSubmission = UserSubmission(text = "STORY TSF-44 5 50%", username = "Bob", createdAt = currentDate)

      userSubmission.showActivityID must equalTo("TSF-44")
    }

    "#showTime returns the time for a STORY" in {
      val currentDate = new DateTime()
      val userSubmission = UserSubmission(text = "STORY TSF-44 5 50%", username = "Bob", createdAt = currentDate)

      userSubmission.showTimeForStoryOrBug must equalTo("5")
    }

    "#showTime returns the time for a MEETING" in {
      val userSubmission = UserSubmission(text = "MEETING 2", username = "Bob")

      userSubmission.showTimeForMeeting must equalTo("2")
    }

    "#showPercentage returns the percentage of time spent trying to understand code for a STORY" in {
      val currentDate = new DateTime()
      val userSubmission = UserSubmission(text = "STORY TSF-44 5 50%", username = "Bob", createdAt = currentDate)

      userSubmission.showPercentage must equalTo("50%")
    }
  }
}
