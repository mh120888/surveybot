package models

import play.api.Play
import play.api.data.validation.ValidationError
import play.api.libs.functional.syntax._
import play.api.libs.json.{JsPath, Reads}

case class SlackPostData (token: String,
                          text: String)

object SlackPostData {
  val SLACK_TOKEN = Play.current.configuration.getString("slack.integration").get
  var testSlackToken = ""

  def setTestSlackToken(token: String) {
    testSlackToken = token
  }

  private def getSlackToken: String = {
    if (play.Play.isTest) testSlackToken else SLACK_TOKEN
  }

  def correctToken: Reads[String] =
    Reads.StringReads.filter(ValidationError("Invalid token"))(str => { str.equals(getSlackToken) } )

  implicit def reads: Reads[SlackPostData] = (
    (JsPath \ "token")(0).read[String](correctToken) and
    (JsPath \ "text")(0).read[String]
  )(SlackPostData.apply _)

  def getSubmissions = {
    List("story: 5000, total: 4, add: 0, remove: 1")
  }
}
