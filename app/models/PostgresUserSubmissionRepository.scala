package models

import anorm.SqlParser._
import anorm._
import play.api.Play.current
import play.api.db._

case class PostgresUserSubmissionRepository() extends UserSubmissionRepository {
  val allAttributes = "id, user_response, user_name"

  def getAll: List[UserSubmission] = DB.withConnection { implicit c =>
    SQL("select " + allAttributes + " from submissions").as(userSubmission *)
  }

  def create(userSubmission: UserSubmission): Option[Long] = {
    DB.withConnection { implicit c =>
      SQL(s"""
           INSERT INTO submissions(user_response, user_name) VALUES ('${userSubmission.text}', '${userSubmission.username}')
          """).executeInsert();
    }
  }

  val userSubmission = {
    get[Long]("id") ~
    get[String]("user_response") ~
    get[String]("user_name") map {
      case id ~ user_response ~ user_name => UserSubmission(Some(id), user_response, user_name)
    }
  }
}
