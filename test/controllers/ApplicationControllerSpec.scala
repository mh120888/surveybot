package controllers

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
      val mockRepository = mock[SlackPostDataRepository]
      mockRepository.getAll returns List("story: 6, total: 8,add: 1, remove: 6")

      val result = (new ApplicationController).data(repository = mockRepository).apply(fakeRequest)

      status(result) must equalTo(OK)
      contentAsString(result) must contain("story: 6, total: 8,add: 1, remove: 6")
    }
  }
}