import models.{PostgresSurveyRespondentRepository, PostgresUserSubmissionRepository, SlackPostData, SurveyRespondent}
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

      val result = route(FakeRequest(POST, "/survey")
        .withFormUrlEncodedBody(("token", "ABCDEFG"),
                                ("text", "story: 1, total: 5, add: 3, remove: 2"),
                                ("user_name", "Matt"))).get

      status(result) must equalTo(OK)
      contentAsString(result) must contain ("Success!")
    }

    "with correct token and username, but no text, returns a response of 200 and an error message" in new WithApplication{
      SlackPostData.setTestSlackToken("xjdk333")

      val result = route(FakeRequest(POST, "/survey").withFormUrlEncodedBody(("token", "xjdk333"), ("user_name", "Matt"))).get

      status(result) must equalTo(OK)
      contentAsString(result) must contain ("There was a problem.")
    }

    "with correct token and username, but invalid text, returns a response of 200 and a descriptive error message" in new WithApplication{
      SlackPostData.setTestSlackToken("xjdk333")

      val result = route(FakeRequest(POST, "/survey").withFormUrlEncodedBody(("token", "xjdk333"), ("user_name", "Matt"), ("text", ""))).get

      status(result) must equalTo(OK)
      contentAsString(result) must contain ("There was a problem with your submission")
      contentAsString(result) must contain ("Story is required")
      contentAsString(result) must contain ("Total time is required")
      contentAsString(result) must contain ("Time adding technical debt is required")
      contentAsString(result) must contain ("Time removing technical debt is required")
    }

    "with correct token and username, but invalid text (total time not in range), returns a response of 200 and a descriptive error message" in new WithApplication {
      SlackPostData.setTestSlackToken("xjdk333")

      val result = route(FakeRequest(POST, "/survey").withFormUrlEncodedBody(("token", "xjdk333"), ("user_name", "Matt"), ("text", "story: 1, total: 15, add: 0, remove: 1"))).get

      status(result) must equalTo(OK)
      contentAsString(result) must contain ("Total time must be in the range 0-12")
    }

    "with invalid text does not save a new user submission to the database" in new WithApplication{
      SlackPostData.setTestSlackToken("xjdk333")
      val repo = PostgresUserSubmissionRepository()
      val initialCount = repo.getAll.length

      val result = route(FakeRequest(POST, "/survey").withFormUrlEncodedBody(("token", "xjdk333"), ("user_name", "Matt"), ("text", ""))).get
      val currentCount = repo.getAll.length

      currentCount must equalTo(initialCount)
    }

    "with incorrect token, no username, and no text returns a response of 401" in new WithApplication{
      SlackPostData.setTestSlackToken("ABCDEFG")

      val result = route(FakeRequest(POST, "/survey").withFormUrlEncodedBody(("token", "HIJKLMN"))).get

      status(result) must equalTo(UNAUTHORIZED)
    }

    "with correct token and text, but no username, returns a response of 400" in new WithApplication{
      SlackPostData.setTestSlackToken("xjdk333")

      val result = route(FakeRequest(POST, "/survey").withFormUrlEncodedBody(("token", "xjdk333"))).get

      status(result) must equalTo(BAD_REQUEST)
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
      SlackPostData.setTestSlackToken("ABCDEFG")

      val validSubmission = "story: 5, total: 4, add: 0, remove: 1"
      route(FakeRequest(POST, "/survey")
        .withFormUrlEncodedBody(("token", "ABCDEFG"),
                                ("text", validSubmission),
                                ("user_name", "Matt")))

      val result = route(FakeRequest(GET, "/data")).get

      status(result) must equalTo(OK)
      contentAsString(result) must contain (validSubmission)
    }
  }

  "GET /dashboard" should {
    "returns a response of 200" in new WithApplication{
      val result = route(FakeRequest(GET, "/dashboard")).get

      status(result) must equalTo(OK)
    }
  }

  "POST /survey_respondents/:id/delete" should {
    "return a response of 303" in new WithApplication{
      val repo = PostgresSurveyRespondentRepository()
      repo.create(SurveyRespondent(id = Option(5), username = "malina"))
      val result = route(FakeRequest(POST, "/survey_respondents/5/delete")).get

      status(result) must equalTo(SEE_OTHER)
    }
  }
}
