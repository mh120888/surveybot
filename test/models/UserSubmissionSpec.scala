package models

import org.junit.runner.RunWith
import org.specs2.mutable.Specification
import org.specs2.runner.JUnitRunner
import play.api.libs.json.{JsError, Json}
import play.api.test.WithApplication

@RunWith(classOf[JUnitRunner])
class UserSubmissionSpec extends Specification {

  "UserSubmission" should {
    "#createFromSlackPostData creates a UserSubmission instance from SlackPostData" in new WithApplication {

      var data = SlackPostData(token = "lalala", text = "story: 100, total: 10, add: 5, remove: 4", username = "matt")
      var result: UserSubmission = UserSubmission.createFromSlackPostData(data)

      result.text must equalTo(data.text)
      result.username must equalTo(data.username)
    }
  }
}


//Repository.save(usersubmission)