package controllers

import models.{PostgresUserSubmissionRepository, UserSubmission}
import org.junit.runner._
import org.specs2.mock.Mockito
import org.specs2.mutable._
import org.specs2.runner._
import play.api.test.Helpers._
import play.api.test._

@RunWith(classOf[JUnitRunner])
class ApplicationControllerSpec extends Specification with Mockito {

  "#data" should {
    "returns a response of 200 and displays submission data" in new WithApplication {
      val fakeRequest = FakeRequest(GET, "/does-not-matter")
      val mockRepository = mock[PostgresUserSubmissionRepository]
      mockRepository.getAll returns List(UserSubmission(text = "story: 6, total: 8, add: 1, remove: 6", userName = "New User"))

      val result = new ApplicationController(repository = mockRepository).data.apply(fakeRequest)

      status(result) must equalTo(OK)
      contentAsString(result) must contain("story: 6, total: 8, add: 1, remove: 6")
      contentAsString(result) must contain("New User")
    }
  }
}