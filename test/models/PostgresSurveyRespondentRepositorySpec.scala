package models

import anorm._
import org.specs2.mutable.Specification
import play.api.Play.current
import play.api.db._
import play.api.test.WithApplication

class PostgresSurveyRespondentRepositorySpec extends Specification {
  val repo = PostgresSurveyRespondentRepository()
  val surveyRespondent = SurveyRespondent(username = "malina")

  def deleteAll(): Boolean = {
    DB.withConnection { implicit c =>
      SQL("TRUNCATE table survey_respondents").execute();
    }
  }

  "PostgresSurveyRespondentRepository" should {
    "#create returns Some[Long] and surveyRespondent exists in database when given valid surveyRespondent" in new WithApplication {
      deleteAll()
      val result: Option[Long] = repo.create(surveyRespondent)

      result.isDefined must equalTo(true)
    }

    "#getAll returns one SurveyRespondent when there is only one SurveyRespondent in the db" in new WithApplication {
      deleteAll()
      repo.create(surveyRespondent)

      val results = repo.getAll()

      results.length must equalTo(1)
    }

    "#findById returns one SurveyRespondent given a SurveyRespondent's ID" in new WithApplication {
      val id = repo.create(surveyRespondent).get

      val result = repo.findById(id)

      result.username must equalTo("malina")
      result.id must equalTo(Option(id))
    }

    "#deleteById removes a single record in the db given a SurveyRespondent's ID" in new WithApplication {
      deleteAll()
      val id = repo.create(surveyRespondent).get

      repo.deleteById(id)

      val results = repo.getAll()
      results.length must equalTo(0)
    }
  }
}
