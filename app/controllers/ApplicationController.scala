package controllers

import com.google.inject.Inject
import models._
import play.api.libs.json._
import play.api.mvc._

class ApplicationController @Inject()(submissionRepository: PostgresUserSubmissionRepository,
                                      respondentRepository: PostgresSurveyRespondentRepository) extends Controller {

  def index = Action {
    Ok(views.html.index("Your new application is ready."))
  }

  def survey = Action { request =>
    val result: JsResult[SlackPostData] = requestBodyAsJson(request).validate[SlackPostData]
    result match {
      case success: JsSuccess[SlackPostData] =>
        validateSubmissionAndReturnResponse(success)
      case error: JsError =>
        returnErrorResponse(error)
    }
  }

  def data = Action {
    val submissions = submissionRepository.getAll
    Ok(views.html.data("Data")(submissions))
  }

  def dashboard = Action { implicit request =>
    Ok(views.html.dashboard("Dashboard")(respondentRepository.getAll()))
  }

  def deleteSurveyRespondent(id: Long) = Action {
    val username = respondentRepository.findById(id).username
    val flashMessage = s"The user ${username} was removed"
//    println(respondentRepository.findById(id))
//    val flashMessage = "The user was removed"
    respondentRepository.deleteById(id)
    Redirect("/dashboard").flashing("success" -> flashMessage)
  }

  private def requestBodyAsJson(request: Request[AnyContent]): JsValue = {
    Json.toJson(request.body.asFormUrlEncoded.get)
  }

  private def validateSubmissionAndReturnResponse(success: JsSuccess[SlackPostData]): Result = {
    val userSubmission = UserSubmission.createFromSlackPostData(success.get)
    if (userSubmission.isValid()) {
      saveSubmissionAndReturnResponse(userSubmission)
    } else {
      Ok("There was a problem with your submission.\n - " + userSubmission.getErrors.mkString("\n - "))
    }
  }

  private def saveSubmissionAndReturnResponse(userSubmission: UserSubmission): Result = {
    val id: Option[Long] = submissionRepository.create(userSubmission)
    if (id.isDefined) {
      Ok("Success!")
    } else {
      Ok("Uh oh! I wasn't able to save that - please try submitting it again.")
    }
  }

  private def returnErrorResponse(error: JsError): Result = {
    if (isTokenError(error)) {
      Unauthorized
    } else if (isUsernameError(error)) {
      BadRequest
    } else {
      Ok("There was a problem.")
    }
  }

  private def isUsernameError(error: JsError): Boolean = {
    JsError.toJson(error).value.contains(USERNAME_ERROR_INDICATOR)
  }

  private def isTokenError(error: JsError): Boolean = {
    JsError.toJson(error).value.contains(TOKEN_ERROR_INDICATOR)
  }

  private val TOKEN_ERROR_INDICATOR = "obj.token[0]"
  private val USERNAME_ERROR_INDICATOR = "obj.user_name[0]"
}
