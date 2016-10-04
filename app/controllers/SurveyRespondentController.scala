package controllers

import com.google.inject.Inject
import models._
import play.api.i18n.{I18nSupport, MessagesApi}
import play.api.mvc._


class SurveyRespondentController @Inject()(respondentRepository: PostgresSurveyRespondentRepository = PostgresSurveyRespondentRepository(),
                                           val messagesApi: MessagesApi) extends Controller with I18nSupport {

  def dashboard = Action { implicit request =>
    Ok(views.html.dashboard("Dashboard")(respondentRepository.getAll()))
  }

  def deleteSurveyRespondent(id: Long) = Action { implicit request =>
    val username = respondentRepository.findById(id).username
    val flashMessage = s"The user ${username} was removed"
    respondentRepository.deleteById(id)
    Redirect("/dashboard").flashing("success" -> flashMessage)
  }

  def createSurveyRespondent = Action { request =>
    val data = request.body.asFormUrlEncoded.get
    SurveyRespondentForm.form.bindFromRequest(data).fold(
      formWithErrors => {
        BadRequest
      },
      surveyRespondentForm => {
        val surveyRespondent = SurveyRespondent(username = surveyRespondentForm.username)
        validateSurveyRespondentAndReturnResponse(surveyRespondent)
      }
    )
  }

  private def validateSurveyRespondentAndReturnResponse(surveyRespondent: SurveyRespondent): Result = {
    if (surveyRespondent.isValid()) {
      saveSurveyRespondentAndReturnResponse(surveyRespondent)
    } else {
      Redirect("/dashboard").flashing(
        "error" -> s"Username ${surveyRespondent.username} already exists"
      )
    }
  }

  private def saveSurveyRespondentAndReturnResponse(surveyRespondent: SurveyRespondent): Result = {
    val id: Option[Long] = respondentRepository.create(surveyRespondent)
    if (id.isDefined) {
      Redirect("/dashboard").flashing(
        "success" -> s"The user ${surveyRespondent.username} was added"
      )
    } else {
      InternalServerError
    }
  }
}
