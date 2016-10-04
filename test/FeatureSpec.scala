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
                                ("text", "STORY TSF-489 5 20%"),
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
      contentAsString(result) must contain ("There was a problem with your submission.")
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

      val validSubmission = "MEETING 4"
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
    "returns a response of 303" in new WithApplication {
      val repo = PostgresSurveyRespondentRepository()
      val id = repo.create(SurveyRespondent(username = "malina")).get

      val result = route(FakeRequest(POST, "/survey_respondents/" + id + "/delete")).get

      status(result) must equalTo(SEE_OTHER)
    }
  }

  "POST /survey_respondents" should {
    "returns a 303 response that includes the text 'The user dave was added' when given valid data" in new WithApplication {
      val result = route(FakeRequest(POST, "/survey_respondents")
        .withFormUrlEncodedBody(("username", "dave"))).get

      status(result) must equalTo(SEE_OTHER)
      flash(result).get("success") must beSome("The user dave was added")
    }

    "returns a 400 response when given invalid data" in new WithApplication {
      val result = route(FakeRequest(POST, "/survey_respondents")
        .withFormUrlEncodedBody(("something_incorrect", "matt"))).get

      status(result) must equalTo(BAD_REQUEST)
    }
  }
}
