package models

import org.junit.runner.RunWith
import org.specs2.mutable.Specification
import org.specs2.runner.JUnitRunner
import play.api.test.WithApplication

@RunWith(classOf[JUnitRunner])
class PostgresUserSubmissionRepositorySpec extends Specification {

  "PostgresUserSubmissionRepository" should {
    "#create returns Some[Long] and user submission exists in database when given valid user submission" in new WithApplication() {

      var result: Option[Long] = PostgresUserSubmissionRepository().create(UserSubmission(text = "lorem ipsum", username = "Test User"))

      result.isDefined must equalTo(true)
    }
  }
}
