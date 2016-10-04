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
class SurveyRespondentControllerSpec extends Specification with Mockito {

  "#deleteSurveyRespondent" should {
    "return a response of 303 and displays a flash message" in new WithApplication {
      val firstRespondent = SurveyRespondent(id = Option(6), username = "malina")
      val fakeRequest = FakeRequest(POST, "/survey_respondents/6/delete")
      val mockSubmissionRepository = mock[PostgresUserSubmissionRepository]
      val mockRespondentRepository = mock[PostgresSurveyRespondentRepository]
      mockRespondentRepository.findById(6) returns firstRespondent

      val result = new SurveyRespondentController(respondentRepository = mockRespondentRepository, messagesApi = mock[MessagesApi])
        .deleteSurveyRespondent(6).apply(fakeRequest)

      status(result) must equalTo(SEE_OTHER)
      flash(result).get("success") must beSome("The user malina was removed")
    }
  }
}