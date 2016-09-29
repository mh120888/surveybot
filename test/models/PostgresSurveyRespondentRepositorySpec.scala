package models

import org.junit.runner.RunWith
import org.specs2.mutable.Specification
import org.specs2.runner.JUnitRunner
import play.api.libs.json.{JsError, Json}
import play.api.test.WithApplication

class PostgresSurveyRespondentRepositorySpec extends Specification {

  "PostgresSurveyRespondentRepository" should {
    "#create returns Some[Long] and surveyRespondent exists in database when given valid surveyRespondent" in new WithApplication(){
      val repo = PostgresSurveyRespondentRepository()

      val result: Option[Long] = repo.create(SurveyRespondent(username = "malina"))

      result.isDefined must equalTo(true)
    }

    "#getAll returns one SurveyRespondent when there is only one SurveyRespondent in the db" in new WithApplication(){
      val repo = PostgresSurveyRespondentRepository()
      repo.deleteAll()
      repo.create(SurveyRespondent(username = "malina"))

      val results = repo.getAll()

      results.length must equalTo(1)
    }

    "#deleteAll deletes all records in the db" in new WithApplication(){
      val repo = PostgresSurveyRespondentRepository()
      repo.create(SurveyRespondent(username = "matt"))

      repo.deleteAll()

      val results = repo.getAll()
      results.length must equalTo(0)
    }
  }
}
