package models

import org.joda.time.DateTime
import org.junit.runner._
import org.specs2.mutable._
import org.specs2.runner._

@RunWith(classOf[JUnitRunner])
class StatsGeneratorSpec extends Specification {
  "#getTotalTime" should {
    "return total hours for all activities" in {
      val userSubmissions = List(UserSubmission(text = "MEETING 2"), UserSubmission(), UserSubmission())
      val result = StatsGenerator(userSubmissions).getTotalTime

      result must equalTo(2)
    }
  }

  "#getTotalTimeForMeeting" should {
    "return total hours for meeting" in {
      val userSubmissions = List(UserSubmission(text = "MEETING 2"), UserSubmission(), UserSubmission())
      val result = StatsGenerator(userSubmissions).getTotalTimeForMeeting

      result must equalTo(2)
    }
  }

  "#getTotalTimeForStory" should {
    "return total hours for story" in {
      val userSubmissions = List(UserSubmission(text = "MEETING 2"), UserSubmission(text = "STORY ABC-10 5 10%"), UserSubmission())
      val result = StatsGenerator(userSubmissions).getTotalTimeForStory

      result must equalTo(5)
    }
  }

  "#getTotalTimeForBug" should {
    "return total hours for bug" in {
      val userSubmissions = List(UserSubmission(text = "MEETING 2"), UserSubmission(text = "BUG ABC-10 5 10%"), UserSubmission(text = "BUG XYZ-8 7 10%"))
      val result = StatsGenerator(userSubmissions).getTotalTimeForBug

      result must equalTo(12)
    }
  }

  "#getPercentageForMeeting" should {
    "return percentage of meeting" in {
      val userSubmissions = List(UserSubmission(text = "MEETING 2"), UserSubmission(text = "BUG ABC-10 5 10%"), UserSubmission(text = "BUG XYZ-8 3 10%"))
      val result = StatsGenerator(userSubmissions).getPercentageForMeeting

      result must equalTo(20.toFloat)
    }
  }

  "#getPercentageForStory" should {
    "return percentage of story" in {
      val userSubmissions = List(UserSubmission(text = "MEETING 2"), UserSubmission(text = "STORY ABC-10 5 10%"), UserSubmission(text = "STORY XYZ-8 3 10%"))
      val result = StatsGenerator(userSubmissions).getPercentageForStory

      result must equalTo(80.toFloat)
    }
  }

  "#getPercentageForBug" should {
    "return percentage of bug" in {
      val userSubmissions = List(UserSubmission(text = "MEETING 2"), UserSubmission(text = "BUG ABC-10 5 10%"), UserSubmission(text = "STORY XYZ-8 3 10%"))
      val result = StatsGenerator(userSubmissions).getPercentageForBug

      result must equalTo(50.toFloat)
    }
  }
}
