package models

import org.junit.runner.RunWith
import org.specs2.mutable.Specification
import org.specs2.runner.JUnitRunner

@RunWith(classOf[JUnitRunner])
class BotMessageComposerSpec extends Specification {
  "#createMessage" should {
    "returns a BotMessage with the correct channel" in {
      val surveyRespondent = SurveyRespondent(username = "bob")
      val result = BotMessageComposer().createMessage(surveyRespondent)

      result.channel must equalTo(surveyRespondent.username)
    }

    "returns a BotMessage with a greeting" in {
      val surveyRespondent = SurveyRespondent(username = "bob")
      val messageComposer = BotMessageComposer()
      val result = messageComposer.createMessage(surveyRespondent).text.split("\n").head

      messageComposer.GREETINGS.contains(result) must equalTo(true)
    }

    "returns a BotMessage with the standard prompt" in {
      val surveyRespondent = SurveyRespondent(username = "bob")
      val messageComposer = BotMessageComposer()
      val result = messageComposer.createMessage(surveyRespondent)

      result.text must contain(messageComposer.PROMPT)
    }
  }
}
