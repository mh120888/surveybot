package models

import org.junit.runner.RunWith
import org.specs2.mutable.Specification
import org.specs2.runner.JUnitRunner
import play.api.libs.json.{JsError, Json}
import play.api.test.WithApplication

@RunWith(classOf[JUnitRunner])
class SlackPostDataSpec extends Specification {

  "SlackPostData" should {
    "#reads returns a JsSuccess if given a valid token and valid text" in new WithApplication{
      SlackPostData.setTestSlackToken("hi")

      val validJson = Json.parse("""{"token":["hi"], "text": ["something"]}""")
      val result = SlackPostData.reads.reads(validJson)

      result.isSuccess must equalTo(true)
    }

    "#reads returns a JsError if given an invalid token" in new WithApplication{
      SlackPostData.setTestSlackToken("hi")

      val jsonWithInvalidToken = Json.parse("""{"token":["invalid token"], "text": ["something"]}""")
      val result = SlackPostData.reads.reads(jsonWithInvalidToken)

      result.isError must equalTo(true)
      result.recoverTotal( e => JsError.toJson(e).value.contains("obj.token[0]") must equalTo(true) )
    }

    "#reads returns a JsError if given a valid token and no text" in new WithApplication{
      SlackPostData.setTestSlackToken("hi")

      val jsonWithNoText = Json.parse("""{"token":["hi"]}""")
      val result = SlackPostData.reads.reads(jsonWithNoText)

      result.isError must equalTo(true)
      result.recoverTotal( e => JsError.toJson(e).value.contains("obj.text[0]") must equalTo(true) )
    }

    "#reads returns a JsError if given no token and valid text" in new WithApplication{
      SlackPostData.setTestSlackToken("hi")

      val jsonWithNoToken = Json.parse("""{"text": ["something"]}""")
      val result = SlackPostData.reads.reads(jsonWithNoToken)

      result.isError must equalTo(true)
      result.recoverTotal( e => JsError.toJson(e).value.contains("obj.token[0]") must equalTo(true) )
    }
  }
}



