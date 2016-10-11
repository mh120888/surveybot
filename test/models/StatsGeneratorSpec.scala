package models

import org.joda.time.DateTime
import org.junit.runner._
import org.specs2.mutable._
import org.specs2.runner._

@RunWith(classOf[JUnitRunner])
class StatsGeneratorSpec extends Specification {

  "#getTotalTime" should {
    "return total hours for all activities" in {
      val userSubmissions = List(UserSubmission(text = "MEETING 2"), UserSubmission(text = "BUG ABC-10 5 10%"), UserSubmission(text = "BUG XYZ-8 3 10%"))

      val result = StatsGenerator(userSubmissions).getTotalTime()

      result must equalTo(10)
    }
  }

  "#getPercentage" should {
    "return percentage of meeting" in {
      val userSubmissions = List(UserSubmission(text = "MEETING 2"), UserSubmission(text = "BUG ABC-10 5 10%"), UserSubmission(text = "BUG XYZ-8 3 10%"))
      val meetingSubmissions = userSubmissions.filter(submission => submission.isMeeting)

      val result = StatsGenerator(userSubmissions).getPercentage(meetingSubmissions)

      result must equalTo(20)
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

  "#getAverageTotalTime" should {
    "return average of hours spent on activity for story" in {
      val userSubmissions = List(UserSubmission(text = "STORY ABC-10 5 10%"), UserSubmission(text = "STORY XYZ-8 3 10%"))

      val result = StatsGenerator(userSubmissions).getAverageTotalTime(userSubmissions)

      result must equalTo(4)
    }
  }

  "#getAverageTimeUnderstandingCode" should {
    "return average percentage of time trying to understand the code" in {
      val userSubmissions = List(UserSubmission(text = "STORY ABC-10 5 20%"), UserSubmission(text = "STORY XYZ-8 3 10%"))

      val result = StatsGenerator(userSubmissions).getAverageTimeUnderstandingCode(userSubmissions)

      result must equalTo(15)
    }
  }
}
