package models

import org.junit.runner.RunWith
import org.specs2.mutable.Specification
import org.specs2.runner.JUnitRunner
import org.specs2.mock.Mockito


@RunWith(classOf[JUnitRunner])
class SurveyRespondentSpec extends Specification with Mockito {
  val mockRepo = mock[PostgresSurveyRespondentRepository]
  SurveyRespondent.repo = mockRepo
  val subject = SurveyRespondent(username = "matt")

  "#isValid()" should {
    "returns true when called on a valid SurveyRespondent" in {
      mockRepo.findByUsername(username = "matt") returns None

      subject.isValid() must equalTo(true)
    }

    "returns false when username already exists" in {
      mockRepo.findByUsername(username = "matt") returns Some(SurveyRespondent(username="matt"))

      subject.isValid() must equalTo(false)
      subject.errors.mkString("") must contain(SurveyRespondent.USERNAME_MUST_BE_UNIQUE)
    }
  }
}