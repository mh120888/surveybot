package controllers

import models._
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
      val mockSubmissionRepository = mock[PostgresUserSubmissionRepository]
      val listOfStories = List(UserSubmission(text = "STORY TSF-489 5 50%", username = "Matt"))
      val listOfBugs = List(UserSubmission(text = "BUG TSF-100 1 40%", username = "Matt"))
      val listOfMeetings = List(UserSubmission(text = "MEETING 2", username = "Matt"))
      mockSubmissionRepository.getAll(UserSubmission.STORY) returns listOfStories
      mockSubmissionRepository.getAll(UserSubmission.BUG) returns listOfBugs
      mockSubmissionRepository.getAll(UserSubmission.MEETING) returns listOfMeetings
      val expectedContent = views.html.data.render("Data", listOfStories, listOfBugs, listOfMeetings)

      val result = new UserSubmissionController(submissionRepository = mockSubmissionRepository, messagesApi = mock[MessagesApi]).data.apply(fakeRequest)

      status(result) must equalTo(OK)
      contentAsString(result) must contain(contentAsString(expectedContent))
    }
  }
}