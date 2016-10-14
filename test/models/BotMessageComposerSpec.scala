package models

import org.junit.runner.RunWith
import org.specs2.mutable.Specification
import org.specs2.runner.JUnitRunner

@RunWith(classOf[JUnitRunner])
class BotMessageComposerSpec extends Specification {
  val surveyRespondent = SurveyRespondent(username = "bob")
  val messageComposer = BotMessageComposer()
  val result = messageComposer.createMessage(surveyRespondent)

  "#createMessage" should {
    "returns a BotMessage with the correct channel" in {
      result.channel must equalTo(surveyRespondent.username)
    }

    "returns a BotMessage with a greeting" in {
      val greeting: String = result.text.split("\n").head

      messageComposer.GREETINGS.contains(greeting) must equalTo(true)
    }

    "returns a BotMessage with the standard prompt" in {
      result.text must contain(messageComposer.PROMPT)
    }
  }
}
