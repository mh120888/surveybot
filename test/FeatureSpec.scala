import models._
import org.joda.time.DateTime
import org.junit.runner._
import org.specs2.mutable.Specification
import org.specs2.runner._
import play.api.test.Helpers._
import play.api.test.{WithApplication, _}

@RunWith(classOf[JUnitRunner])
class FeatureSpec extends Specification {

  "GET /data" should {
    "return a response of 200 and include submission data from current week" in new WithApplication{
      PostgresUserSubmissionRepository().create(UserSubmission(createdAt = new DateTime(1999, 1, 1, 0, 0), text = "STORY XYZ 4 50%"))
      PostgresUserSubmissionRepository().create(UserSubmission(createdAt = new DateTime(), text = "STORY TSF-489 5 50%"))
      SlackPostData.setTestSlackToken("ABCDEFG")

      val result = route(FakeRequest(GET, "/data")).get

      status(result) must equalTo(OK)
      contentAsString(result) must contain("TSF-489")
      contentAsString(result) must not contain "XYZ"
    }
  }

  "GET /all_data" should {
    "return a response of 200 and include all submission data" in new WithApplication{
      PostgresUserSubmissionRepository().create(UserSubmission(createdAt = new DateTime(1999, 1, 1, 0, 0), text = "STORY XYZ 5 50%"))
      PostgresUserSubmissionRepository().create(UserSubmission(createdAt = new DateTime(), text = "STORY TSF-489 5 50%"))
      SlackPostData.setTestSlackToken("ABCDEFG")

      val result = route(FakeRequest(GET, "/all_data")).get

      status(result) must equalTo(OK)
      contentAsString(result) must contain("TSF-489")
      contentAsString(result) must contain("XYZ")
    }
  }

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

    "with correct token and username, but no text field, returns a response of 200 and an error message" in new WithApplication{
      SlackPostData.setTestSlackToken("xjdk333")

      val result = route(FakeRequest(POST, "/survey").withFormUrlEncodedBody(("token", "xjdk333"), ("user_name", "Matt"))).get

      status(result) must equalTo(OK)
      contentAsString(result) must contain ("There was a problem.")
    }

    "with correct token and username, but empty text, returns a response of 200 and a descriptive error message" in new WithApplication{
      SlackPostData.setTestSlackToken("xjdk333")

      val result = route(FakeRequest(POST, "/survey").withFormUrlEncodedBody(("token", "xjdk333"), ("user_name", "Matt"), ("text", ""))).get

      status(result) must equalTo(OK)
      contentAsString(result) must contain ("There was a problem with your submission.")
    }

    "with correct token and username, but invalid text due to wrong activity type, returns a response of 200 and a descriptive error message" in new WithApplication{
      SlackPostData.setTestSlackToken("xjdk333")

      val result = route(FakeRequest(POST, "/survey").withFormUrlEncodedBody(("token", "xjdk333"), ("user_name", "Matt"), ("text", "STANDUP 1"))).get

      status(result) must equalTo(OK)
      contentAsString(result) must contain ("There was a problem with your submission.")
      contentAsString(result) must contain ("Activity type is required: BUG, STORY, MEETING")
    }

    "with correct token and username, but invalid text due to wrong number of parts, returns a response of 200 and a descriptive error message" in new WithApplication{
      SlackPostData.setTestSlackToken("xjdk333")

      val result = route(FakeRequest(POST, "/survey").withFormUrlEncodedBody(("token", "xjdk333"), ("user_name", "Matt"), ("text", "BUG 1 50%"))).get

      status(result) must equalTo(OK)
      contentAsString(result) must contain ("There was a problem with your submission.")
      contentAsString(result) must contain (UserSubmission.REQUIRED_FOR_STORY_OR_BUG)
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
   "return a response of 200 and include submission data from current week" in new WithApplication{
      PostgresUserSubmissionRepository().create(UserSubmission(createdAt = new DateTime(1999, 1, 1, 0, 0), text = "STORY XYZ 5 50%"))
      PostgresUserSubmissionRepository().create(UserSubmission(createdAt = new DateTime(), text = "STORY TSF-489 5 50%"))
      SlackPostData.setTestSlackToken("ABCDEFG")

      val result = route(FakeRequest(GET, "/data")).get

      status(result) must equalTo(OK)
      contentAsString(result) must contain("TSF-489")
      contentAsString(result) must not contain "XYZ"
    }

    "allow a user to navigate to a page that shows the previous week's data" in new WithBrowser {
      val timeCalculator = TimeCalculator()
      val currentDate = new DateTime().minusWeeks(2)
      val startDate = timeCalculator.getStartOfWeek(currentDate).toString("MM/d/yyyy")
      val endDate = timeCalculator.getEndOfWeek(currentDate).toString("MM/d/yyyy")

      browser.goTo("/data?weeksAgo=1")
      browser.$("#previous-week").click()
      browser.url must equalTo("/data?weeksAgo=2")
      browser.$("h1").getTexts.get(0) must equalTo(s"Submissions from ${startDate} to ${endDate}")
    }
  }

  "GET /data?weeksAgo=1" should {
    "return a response of 200 and include submissions from the previous week" in new WithApplication {
      PostgresUserSubmissionRepository().create(UserSubmission(createdAt = new DateTime(1999, 1, 1, 0, 0), text = "STORY XYZ 5 50%"))
      PostgresUserSubmissionRepository().create(UserSubmission(createdAt = new DateTime().minusWeeks(1), text = "STORY TSF-500 5 50%"))
      SlackPostData.setTestSlackToken("ABCDEFG")

      val result = route(FakeRequest(GET, "/data?weeksAgo=1")).get

      status(result) must equalTo(OK)
      contentAsString(result) must contain("TSF-500")
      contentAsString(result) must not contain "XYZ"
    }
  }

  "GET /all_data" should {
    "return a response of 200 and include all submission data" in new WithApplication{
      PostgresUserSubmissionRepository().create(UserSubmission(createdAt = new DateTime(1999, 1, 1, 0, 0), text = "STORY XYZ 5 50%"))
      PostgresUserSubmissionRepository().create(UserSubmission(createdAt = new DateTime(), text = "STORY TSF-489 5 50%"))
      SlackPostData.setTestSlackToken("ABCDEFG")

      val result = route(FakeRequest(GET, "/all_data")).get

      status(result) must equalTo(OK)
      contentAsString(result) must contain("TSF-489")
      contentAsString(result) must contain("XYZ")
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
