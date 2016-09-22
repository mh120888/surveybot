package models

import anorm.SqlParser._
import anorm._
import play.api.Play.current
import play.api.db._

case class PostgresUserSubmissionUserSubmissionRepository() extends UserSubmissionRepository {
  val allAttributes = "id, user_response, user_name"

  def getAll: List[UserSubmission] = DB.withConnection { implicit c =>
    SQL("select " + allAttributes + " from submission").as(userSubmission *)
  }

  val userSubmission = {
    get[Long]("id") ~
    get[String]("user_response") ~
    get[String]("user_name") map {
      case id ~ user_response ~ user_name => UserSubmission(Some(id), user_response, user_name)
    }
  }
}
