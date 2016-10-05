package models

import anorm._
import org.joda.time.DateTime
import org.junit.runner.RunWith
import org.specs2.mutable.Specification
import org.specs2.runner.JUnitRunner
import play.api.db.DB
import play.api.test.WithApplication

@RunWith(classOf[JUnitRunner])
class PostgresUserSubmissionRepositorySpec extends Specification {

  "PostgresUserSubmissionRepository" should {
    "#create returns Some[Long] and user submission exists in database when given valid user submission" in new WithApplication() {
      var result: Option[Long] = PostgresUserSubmissionRepository().create(UserSubmission(text = "lorem ipsum", username = "Test User"))

      result.isDefined must equalTo(true)
    }

    "#getAllFromDateRange() returns all records from specified date range" in new WithApplication() {
      DB.withConnection { implicit c =>
        SQL("TRUNCATE table submissions").execute();
      }
      PostgresUserSubmissionRepository().create(UserSubmission())
      var result: List[UserSubmission] = PostgresUserSubmissionRepository().getAllFromDateRange(new DateTime().minusYears(5), new DateTime)

      result.length must equalTo(1)
    }

    "#getAllFromDateRange() returns no records from specified date range" in new WithApplication() {
      var result: List[UserSubmission] = PostgresUserSubmissionRepository().getAllFromDateRange(new DateTime(1950, 6, 10, 0, 0), new DateTime(1950, 11, 28, 0, 0))

      result.length must equalTo(0)
    }

    "#getAll() does not return records that do not match the specified type" in new WithApplication() {
      val initialCount = PostgresUserSubmissionRepository().getAll("STORY").length
      DB.withConnection { implicit c =>
        SQL(
          s"""
           INSERT INTO submissions(user_response, user_name) VALUES ('BUG TSF-100 3 20%', 'bob')
          """).executeInsert();
      }

      val result = PostgresUserSubmissionRepository().getAll("STORY")
      result.length must equalTo(initialCount)
    }

    "#getAll() returns all records of the specified type" in new WithApplication() {
      val initialCount = PostgresUserSubmissionRepository().getAll("STORY").length
      DB.withConnection { implicit c =>
        SQL(
          s"""
           INSERT INTO submissions(user_response, user_name) VALUES ('STORY TSF-489 5 50%', 'bob')
          """).executeInsert();
      }

      val result = PostgresUserSubmissionRepository().getAll("STORY")
      result.length must equalTo(initialCount + 1)
      result.last.isStory must equalTo(true)
    }
  }
}
