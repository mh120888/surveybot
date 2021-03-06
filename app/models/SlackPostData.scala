package models

import play.api.Play
import play.api.data.validation.ValidationError
import play.api.libs.functional.syntax._
import play.api.libs.json.{JsPath, Reads}

case class SlackPostData (token: String,
                          text: String,
                          username: String)

object SlackPostData {
  val SURVEY_SLASH_COMMAND_TOKEN = Play.current.configuration.getString("slack.surveySlashCommandToken").get
  var testSlackToken = ""

  def setTestSlackToken(token: String) {
    testSlackToken = token
  }

  private def getSlackToken: String = {
    if (play.Play.isTest) testSlackToken else SURVEY_SLASH_COMMAND_TOKEN
  }

  def correctToken: Reads[String] =
    Reads.StringReads.filter(ValidationError("Invalid token"))(str => { str.equals(getSlackToken) } )

  implicit def reads: Reads[SlackPostData] = (
    (JsPath \ "token")(0).read[String](correctToken) and
    (JsPath \ "text")(0).read[String] and
    (JsPath \ "user_name")(0).read[String]
  )(SlackPostData.apply _)
}
