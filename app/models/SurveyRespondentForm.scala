package models

import play.api.data.Form
import play.api.data.Forms._

case class SurveyRespondentForm(username: String)

object SurveyRespondentForm {
  val form = Form(
    mapping(
      "username" -> text
    )(SurveyRespondentForm.apply)(SurveyRespondentForm.unapply))
}