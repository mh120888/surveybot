package models

import anorm.SqlParser._
import anorm._
import play.api.Play.current
import play.api.db._

case class PostgresSurveyRespondentRepository() {
  def create(surveyRespondent: SurveyRespondent): Option[Long] = {
    DB.withConnection { implicit c =>
      SQL(s"""
           INSERT INTO survey_respondents(user_name) VALUES ('${surveyRespondent.username}')
          """).executeInsert();
    }
  }

  def getAll(): List[SurveyRespondent] = DB.withConnection { implicit c =>
    SQL("SELECT * FROM survey_respondents").as(surveyRespondent *)
  }

  def findById(id: Long): SurveyRespondent = {
    DB.withConnection { implicit c =>
      SQL(s"SELECT * FROM survey_respondents where id = ('${id}')").as(surveyRespondent *).head
    }
  }

  def deleteById(id: Long) {
    DB.withConnection { implicit c =>
      SQL(s"DELETE FROM survey_respondents where id = ('${id}')").executeUpdate();
    }
  }

  val surveyRespondent = {
    get[Long]("id") ~
      get[String]("user_name") map {
      case id ~ user_name => SurveyRespondent(Some(id), user_name)
    }
  }
}
