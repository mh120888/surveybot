package models

import play.api.Play
import play.api.libs.ws.{WSClient, WSRequest, WSResponse}
import scala.concurrent.Future

case class BotMessageSender(ws: WSClient) {
  private val USERNAME = "surveybot"
  private val TOKEN = Play.current.configuration.getString("slack.botUserToken").get
  private val SLACK_API_URL = "https://slack.com/api/chat.postMessage"

  def sendMessage(botMessage: BotMessage): Future[WSResponse] = {
    buildRequest(botMessage).get()
  }

  def buildRequest(botMessage: BotMessage): WSRequest = {
     ws.url(SLACK_API_URL)
      .withQueryString(
        ("username", USERNAME),
        ("channel", s"@${botMessage.channel}"),
        ("token", TOKEN),
        ("text", botMessage.text))
  }
}
