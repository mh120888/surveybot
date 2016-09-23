package controllers

import models.{PostgresUserSubmissionRepository, SlackPostData, UserSubmission}
import org.junit.runner._
import org.specs2.mock.Mockito
import org.specs2.mutable._
import org.specs2.runner._
import play.api.test.Helpers._
import play.api.test._

@RunWith(classOf[JUnitRunner])
class ApplicationControllerSpec extends Specification with Mockito {

  "#data" should {
    "return a response of 200 and displays submission data" in new WithApplication {
      val fakeRequest = FakeRequest(GET, "/does-not-matter")
      val mockRepository = mock[PostgresUserSubmissionRepository]
      mockRepository.getAll returns List(UserSubmission(text = "story: 6, total: 8, add: 1, remove: 6", username = "New User"))

      val result = new ApplicationController(repository = mockRepository).data.apply(fakeRequest)

      status(result) must equalTo(OK)
      contentAsString(result) must contain("story: 6, total: 8, add: 1, remove: 6")
    }
  }

  "#survey" should {
    "return a response of 200 with success text when submission is saved" in new WithApplication {
      SlackPostData.setTestSlackToken("ABCDEFG")
      val userSubmission = UserSubmission(text = "story: 1, total: 5, add: 3, remove: 2", username = "New User")
      val fakeRequest = FakeRequest(GET, "/does-not-matter")
        .withFormUrlEncodedBody(("token", "ABCDEFG"),
                                ("text", "story: 1, total: 5, add: 3, remove: 2"),
                                ("user_name", "New User"))
      val mockRepository = mock[PostgresUserSubmissionRepository]
      mockRepository.create(userSubmission) returns Some(2)

      val result = new ApplicationController(repository = mockRepository).survey.apply(fakeRequest)

      status(result) must equalTo(OK)
      contentAsString(result) must contain("Success!")
    }

    "return a response of 200 with error message when submission is not saved" in new WithApplication {
      SlackPostData.setTestSlackToken("ABCDEFG")
      val userSubmission = UserSubmission(text = "story: 1, total: 5, add: 3, remove: 2", username = "New User")
      val fakeRequest = FakeRequest(GET, "/does-not-matter")
        .withFormUrlEncodedBody(("token", "ABCDEFG"),
                                ("text", "story: 1, total: 5, add: 3, remove: 2"),
                                ("user_name", "New User"))
      val mockRepository = mock[PostgresUserSubmissionRepository]
      mockRepository.create(userSubmission) returns None

      val result = new ApplicationController(repository = mockRepository).survey.apply(fakeRequest)

      status(result) must equalTo(OK)
      contentAsString(result) must contain("Uh oh! I wasn't able to save that - please try submitting it again.")
    }
  }
}