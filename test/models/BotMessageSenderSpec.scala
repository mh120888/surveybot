package models

import org.junit.runner.RunWith
import org.specs2.mutable.Specification
import org.specs2.runner.JUnitRunner
import play.api.Play
import play.api.libs.ws.WS
import play.api.test.WithApplication

@RunWith(classOf[JUnitRunner])
class BotMessageSenderSpec extends Specification {

  "#buildRequest" should {
    "returns a WSRequest with the correct URL and params" in new WithApplication {
      val botMessage = BotMessage("malina", "Hi there")

      val result = BotMessageSender(WS.client).buildRequest(botMessage)
      result.url must equalTo("https://slack.com/api/chat.postMessage")
      result.queryString.getOrElse("username", List("")).head must equalTo("SurveyBot")
      result.queryString.getOrElse("channel", List("")).head must equalTo("@malina")
      result.queryString.getOrElse("token", List("")).head must equalTo(Play.current.configuration.getString("slack.token").get)
      result.queryString.getOrElse("text", List("")).head must equalTo("Hi there")
    }
  }
}
