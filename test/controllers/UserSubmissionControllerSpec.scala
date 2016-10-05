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
      mockSubmissionRepository.getAll returns List(UserSubmission(text = "story: 6, total: 8, add: 1, remove: 6", username = "New User"))

      val result = new UserSubmissionController(submissionRepository = mockSubmissionRepository, messagesApi = mock[MessagesApi]).data.apply(fakeRequest)

      status(result) must equalTo(OK)
      contentAsString(result) must contain("story: 6, total: 8, add: 1, remove: 6")
    }
  }
}