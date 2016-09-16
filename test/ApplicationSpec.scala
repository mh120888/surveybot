import models.SlackPostData
import org.specs2.mutable._
import org.specs2.runner._
import org.junit.runner._
import play.api.libs.json.Json
import play.api.test._
import play.api.test.Helpers._

@RunWith(classOf[JUnitRunner])
class ApplicationSpec extends Specification {

  "Application" should {

    "POST /survey with correct token and text returns a response of 200 and success message" in new WithApplication{
      SlackPostData.setTestSlackToken("ABCDEFG")

      val result = route(FakeRequest(POST, "/survey")
          .withJsonBody(Json.parse(
          """ {
            |"token": "ABCDEFG",
            | "text": "story: 1, total: 5, add: 3, remove: 2"
            | }"""
            .stripMargin))).get

      status(result) must equalTo(OK)
      contentAsString(result) must contain ("Success!")
    }

    "POST /survey with correct token and no text returns a response of 200 and error message" in new WithApplication{
      SlackPostData.setTestSlackToken("xjdk333")

      val result = route(FakeRequest(POST, "/survey")
          .withJsonBody(Json.parse(
            """{
              |"token": "xjdk333"
              |}"""
              .stripMargin))).get

      status(result) must equalTo(OK)
      contentAsString(result) must contain ("Your submission is wrong.")
    }

    "POST /survey with incorrect token returns a response of 401" in new WithApplication{
      SlackPostData.setTestSlackToken("ABCDEFG")

      val result = route(FakeRequest(POST, "/survey")
        .withJsonBody(Json.parse(
          """{
            |"token": "HIJKLMN"
            |}""".stripMargin))).get

      status(result) must equalTo(UNAUTHORIZED)
    }

    "POST /bogus returns a response of 404" in new WithApplication{
      val result = route(FakeRequest(POST, "/bogus")).get

      status(result) must equalTo(NOT_FOUND)
    }
  }
}
