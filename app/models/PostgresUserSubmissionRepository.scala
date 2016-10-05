package models

import anorm.SqlParser._
import anorm._
import org.joda.time.DateTime
import play.api.Play.current
import play.api.db._

case class PostgresUserSubmissionRepository() extends UserSubmissionRepository {
  val allAttributes = "id, user_response, user_name"

  def getAll: List[UserSubmission] = DB.withConnection { implicit c =>
    SQL("select " + allAttributes + " from submissions").as(userSubmission *)
  }

  def getAll(activity: String): List[UserSubmission] = {
    DB.withConnection { implicit c =>
      SQL("select " + allAttributes + s" from submissions WHERE user_response LIKE '${activity}%'").as(userSubmission *)
    }
  }

  def getAllFromDateRange(startDate: DateTime, endDate: DateTime): List[UserSubmission] = {
    DB.withConnection { implicit c =>
      SQL("select " + allAttributes + s" from submissions WHERE created_at between '${startDate}' and '${endDate}'").as(userSubmission *)
    }
  }

  def create(userSubmission: UserSubmission): Option[Long] = {
    DB.withConnection { implicit c =>
      SQL(s"""
           INSERT INTO submissions(created_at, user_response, user_name) VALUES ('${userSubmission.createdAt}', '${userSubmission.text}', '${userSubmission.username}')
          """).executeInsert();
    }
  }

  val userSubmission = {
    get[Long]("id") ~
      get[String]("user_response") ~
      get[String]("user_name") map {
      case id ~ user_response ~ user_name => UserSubmission(id = Some(id), text = user_response, username = user_name)
    }
  }
}
