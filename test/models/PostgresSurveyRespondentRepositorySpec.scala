package models

import org.specs2.mutable.Specification
import play.api.test.WithApplication
import play.api.db._
import play.api.Play.current
import anorm._

class PostgresSurveyRespondentRepositorySpec extends Specification {

  def deleteAll(): Boolean = {
    DB.withConnection { implicit c =>
      SQL("TRUNCATE table survey_respondents").execute();
    }
  }

  "PostgresSurveyRespondentRepository" should {
    "#create returns Some[Long] and surveyRespondent exists in database when given valid surveyRespondent" in new WithApplication(){
      val repo = PostgresSurveyRespondentRepository()
      deleteAll()
      val result: Option[Long] = repo.create(SurveyRespondent(username = "malina"))

      result.isDefined must equalTo(true)
    }

    "#getAll returns one SurveyRespondent when there is only one SurveyRespondent in the db" in new WithApplication(){
      val repo = PostgresSurveyRespondentRepository()
      deleteAll()
      repo.create(SurveyRespondent(username = "malina"))

      val results = repo.getAll()

      results.length must equalTo(1)
    }

    "#findById returns one SurveyRespondent given a SurveyRespondent's ID" in new WithApplication {
      val repo = PostgresSurveyRespondentRepository()
      repo.create(SurveyRespondent(username = "malina"))

//      val lastRecordId = repo.getAll().last.id
//      val result = repo.findById(lastRecordId)
//      result.username must equalTo("malina")
    }

//    "#delete removes a single record in the db" in new WithApplication {
//      val repo = PostgresSurveyRespondentRepository()
//      deleteAll()
//
//    }
  }

//    "#delete removes a single record in the db" in new WithApplication(){
//      val repo = PostgresSurveyRespondentRepository()
//      deleteAll()
//      repo.create(SurveyRespondent(username = "matt"))
//
//      repo.delete(SurveyRespondent(username = "matt"))
//
//      val results = repo.getAll()
//      results.length must equalTo(0)
//    }
//  }
}
