package models

import org.joda.time.DateTime
import org.junit.runner._
import org.specs2.mutable._
import org.specs2.runner._

@RunWith(classOf[JUnitRunner])
class StatsGeneratorSpec extends Specification {

  "#getTotal" should {
    "return total hours for all activities" in {
      val userSubmissions = List(UserSubmission(text = "MEETING 2"), UserSubmission(text = "BUG ABC-10 5 10%"), UserSubmission(text = "BUG XYZ-8 3 10%"))

      val result = StatsGenerator(userSubmissions).getTotal()

      result must equalTo(10)
    }
  }

  "#getPercentage" should {
    "return percentage of meeting" in {
      val userSubmissions = List(UserSubmission(text = "MEETING 2"), UserSubmission(text = "BUG ABC-10 5 10%"), UserSubmission(text = "BUG XYZ-8 3 10%"))
      val meetingSubmissions = userSubmissions.filter(submission => submission.isMeeting)

      val result = StatsGenerator(userSubmissions).getPercentage(meetingSubmissions)

      result must equalTo(20.toFloat)
    }
  }

  "#showPercentage" should {
    "return formatted percentage as a string" in {
      val userSubmissions = List(UserSubmission(text = "MEETING 2"), UserSubmission(text = "BUG ABC-10 5 10%"), UserSubmission(text = "BUG XYZ-8 3 10%"))
      val meetingSubmissions = userSubmissions.filter(submission => submission.isMeeting)

      val result = StatsGenerator(userSubmissions).showPercentage(meetingSubmissions)

      result must equalTo("20%")
    }
  }

  "#getAverageTime" should {
    "return average of hours spent on activity for story" in {
      val userSubmissions = List(UserSubmission(text = "STORY ABC-10 5 10%"), UserSubmission(text = "STORY XYZ-8 3 10%"))
      val storySubmissions = List(UserSubmission(text = "STORY ABC-10 5 10%"), UserSubmission(text = "STORY XYZ-8 3 10%"))

      val result = StatsGenerator(userSubmissions).getAverageTime(userSubmissions)

      result must equalTo(4)
    }
  }
}
