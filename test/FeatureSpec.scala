import models.SlackPostData
import org.junit.runner._
import org.specs2.mutable._
import org.specs2.runner._
import play.api.test.Helpers._
import play.api.test._

@RunWith(classOf[JUnitRunner])
class FeatureSpec extends Specification {

  "POST /survey" should {

    "with correct token and text returns a response of 200 and success message" in new WithApplication{
      SlackPostData.setTestSlackToken("ABCDEFG")

      val result = route(FakeRequest(POST, "/survey").withFormUrlEncodedBody(("token", "ABCDEFG"), ("text", "story: 1, total: 5, add: 3, remove: 2"))).get

      status(result) must equalTo(OK)
      contentAsString(result) must contain ("Success!")
    }

    "with correct token and no text returns a response of 200 and error message" in new WithApplication{
      SlackPostData.setTestSlackToken("xjdk333")

      val result = route(FakeRequest(POST, "/survey").withFormUrlEncodedBody(("token", "xjdk333"))).get

      status(result) must equalTo(OK)
      contentAsString(result) must contain ("Your submission is wrong.")
    }

    "with incorrect token returns a response of 401" in new WithApplication{
      SlackPostData.setTestSlackToken("ABCDEFG")

      val result = route(FakeRequest(POST, "/survey").withFormUrlEncodedBody(("token", "HIJKLMN"))).get

      status(result) must equalTo(UNAUTHORIZED)
    }
  }

  "POST /bogus" should {
    "returns a response of 404" in new WithApplication{
      val result = route(FakeRequest(POST, "/bogus")).get

      status(result) must equalTo(NOT_FOUND)
    }
  }

  "GET /data" should {
    "returns a response of 200 and displays submission data" in new WithApplication{
      val result = route(FakeRequest(GET, "/data")).get

      status(result) must equalTo(OK)
      contentAsString(result) must contain ("story: 5, total: 4, add: 0, remove: 1")
    }
  }
}
