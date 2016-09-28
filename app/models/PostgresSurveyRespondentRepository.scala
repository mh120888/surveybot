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
    SQL("select * from survey_respondents").as(surveyRespondent *)
  }

  val surveyRespondent = {
    get[Long]("id") ~
      get[String]("user_name") map {
      case id ~ user_name => SurveyRespondent(Some(id), user_name)
    }
  }
}
