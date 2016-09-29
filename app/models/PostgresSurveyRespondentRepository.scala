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

  def findById(id: Option[Long]): SurveyRespondent = {
    DB.withConnection { implicit c =>
      SQL(s"SELECT * FROM survey_respondents where id = ('${id.get}')").as(surveyRespondent *).head
    }
  }

//  def getUserId(username: String, password: String): Option[Long] = DB.withConnection { implicit c =>
//    val rowOption = SQL("select id from users where username = {username} and password = {password} limit 1")
//      .on('username -> username, 'password -> password)
//      .apply
//      .headOption
//    rowOption match {
//      case Some(row) => Some(row[Long]("id"))  // the uid
//      case None => None
//    }
//  }
//
//  val thing = {
//    get[Long]("id") ~
//    get[String]("user_name")
//  }
  //  def delete(surveyRespondent: SurveyRespondent) {
  //    DB.withConnection { implicit c =>
  //      SQL(s"DELETE FROM survey_respondents where user_name = ('${surveyRespondent.username}')").executeUpdate();
  //    }
  //  }

  val surveyRespondent = {
    get[Long]("id") ~
      get[String]("user_name") map {
      case id ~ user_name => SurveyRespondent(Some(id), user_name)
    }
  }
}
