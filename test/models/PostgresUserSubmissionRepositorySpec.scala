package models

import anorm._
import org.joda.time.DateTime
import org.junit.runner.RunWith
import org.specs2.mutable.Specification
import org.specs2.runner.JUnitRunner
import play.api.Play.current
import play.api.db.DB
import play.api.test.WithApplication

@RunWith(classOf[JUnitRunner])
class PostgresUserSubmissionRepositorySpec extends Specification {

  def setupDB() = {
    DB.withConnection { implicit c =>
      SQL("TRUNCATE table submissions").execute();
      SQL(
        s"""
             INSERT INTO submissions(created_at, user_response, user_name) VALUES ('2016-6-06T11:39:50.640-07:00', 'STORY 10 4 15%', 'fakebob');
             INSERT INTO submissions(created_at, user_response, user_name) VALUES ('2016-6-08T11:39:50.640-07:00', 'BUG 30 2 20%', 'malina');
             INSERT INTO submissions(created_at, user_response, user_name) VALUES ('2015-6-07T11:39:50.640-07:00', 'MEETING 3', 'fakeangus');
          """).executeInsert();
    }
  }

  "PostgresUserSubmissionRepository" should {
    "#create returns Some[Long] and user submission exists in database when given valid user submission" in new WithApplication() {
      var result: Option[Long] = PostgresUserSubmissionRepository().create(UserSubmission(text = "lorem ipsum", username = "Test User"))

      result.isDefined must equalTo(true)
    }

    "#getAllFromDateRange() returns all records from specified date range" in new WithApplication() {
      setupDB()
      var result: List[UserSubmission] = PostgresUserSubmissionRepository().getAllFromDateRange(new DateTime().minusYears(5), new DateTime)

      result.length must equalTo(3)
      result.last.username must equalTo("malina")
    }

    "#getAllFromDateRange() returns no records from specified date range" in new WithApplication() {
      var result: List[UserSubmission] = PostgresUserSubmissionRepository().getAllFromDateRange(new DateTime(1950, 6, 10, 0, 0), new DateTime(1950, 11, 28, 0, 0))

      result.length must equalTo(0)
    }

    "#getAll returns all records, ordered from oldest to newest" in new WithApplication() {
      setupDB()

      val result = PostgresUserSubmissionRepository().getAll
      result.length must equalTo(3)
      result.last.username must equalTo("malina")
    }
  }
}

