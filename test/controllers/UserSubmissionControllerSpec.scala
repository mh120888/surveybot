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
    "return a response of 200 and displays submission data" in {
      val fakeRequest = FakeRequest(GET, "/does-not-matter")
      val mockStoryRepository = mock[PostgresUserSubmissionRepository]
      val mockStory = mock[UserSubmission]
      mockStory.isStory returns true
      val mockBug = mock[UserSubmission]
      mockBug.isBug returns true
      val mockMeeting = mock[UserSubmission]
      mockMeeting.isMeeting returns true

      val allSubmissionsInRange = List(mockStory, mockBug, mockMeeting)
      mockStoryRepository.getAllFromDateRange(any[DateTime], any[DateTime]) returns allSubmissionsInRange

      val expectedContent = views.html.data.render("Data",
        allSubmissionsInRange.filter(submission => submission.isStory),
        allSubmissionsInRange.filter(submission => submission.isBug),
        allSubmissionsInRange.filter(submission => submission.isMeeting))

      val result = new UserSubmissionController(submissionRepository = mockStoryRepository, messagesApi = mock[MessagesApi]).data.apply(fakeRequest)

      status(result) must equalTo(OK)
      contentAsString(result) must contain(contentAsString(expectedContent))
    }
  }
}