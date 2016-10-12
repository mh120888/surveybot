package controllers

import models._
import org.joda.time.DateTime
import org.junit.runner._
import org.specs2.mock.Mockito
import org.specs2.mutable._
import org.specs2.runner._
import play.api.i18n.MessagesApi
import play.api.test.Helpers._
import play.api.test._

@RunWith(classOf[JUnitRunner])
class UserSubmissionControllerSpec extends Specification with Mockito {

  "#data" should {
    "return a response of 200 and display submission data" in {
      val fakeRequest = FakeRequest(GET, "/does-not-matter")
      val mockSubmissionRepository = mock[PostgresUserSubmissionRepository]
      val mockStory = mock[UserSubmission]
      mockStory.isStory returns true
      val mockBug = mock[UserSubmission]
      mockBug.isBug returns true
      val mockMeeting = mock[UserSubmission]
      mockMeeting.isMeeting returns true
      val mockCalculator = mock[TimeCalculator]
      val startDate = new DateTime()
      mockCalculator.getStartOfWeek(any[DateTime]) returns startDate
      mockCalculator.getEndOfWeek(any[DateTime]) returns startDate
      val allSubmissionsInRange = List(mockStory, mockBug, mockMeeting)
      mockSubmissionRepository.getAllFromDateRange(any[DateTime], any[DateTime]) returns allSubmissionsInRange
      val statsGenerator = StatsGenerator(allSubmissionsInRange)

      val weeksAgo = 0
      val expectedContent = views.html.data.render(s"Submissions from ${startDate.toString("MM/d/yyyy")} to ${startDate.toString("MM/d/yyyy")}",
        weeksAgo,
        allSubmissionsInRange.filter(submission => submission.isStory),
        allSubmissionsInRange.filter(submission => submission.isBug),
        allSubmissionsInRange.filter(submission => submission.isMeeting),
        statsGenerator)

      val result = new UserSubmissionController(timeCalculator = mockCalculator, submissionRepository = mockSubmissionRepository, messagesApi = mock[MessagesApi]).data(weeksAgo).apply(fakeRequest)

      status(result) must equalTo(OK)
      contentAsString(result) must contain(contentAsString(expectedContent))
    }
  }

  "#all_data" should {
    "return a response of 200 and displays all submission data" in {
      val fakeRequest = FakeRequest(GET, "/does-not-matter")
      val mockSubmissionRepository = mock[PostgresUserSubmissionRepository]
      val mockStory = mock[UserSubmission]
      mockStory.isStory returns true
      val mockBug = mock[UserSubmission]
      mockBug.isBug returns true
      val mockMeeting = mock[UserSubmission]
      mockMeeting.isMeeting returns true
      val allSubmissions = List(mockStory, mockBug, mockMeeting)
      mockSubmissionRepository.getAll returns allSubmissions
      val statsGenerator = StatsGenerator(allSubmissions)

      val expectedContent = views.html.data.render("All Submissions",
        0,
        allSubmissions.filter(submission => submission.isStory),
        allSubmissions.filter(submission => submission.isBug),
        allSubmissions.filter(submission => submission.isMeeting),
        statsGenerator)

      val result = new UserSubmissionController(submissionRepository = mockSubmissionRepository, messagesApi = mock[MessagesApi]).allData.apply(fakeRequest)

      status(result) must equalTo(OK)
      contentAsString(result) must contain(contentAsString(expectedContent))
    }
  }
}