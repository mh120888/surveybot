package controllers

import com.google.inject.Inject
import models.{PostgresUserSubmissionRepository, SlackPostData, UserSubmission}
import play.api.libs.json._
import play.api.mvc._

class ApplicationController @Inject()(repository: PostgresUserSubmissionRepository) extends Controller {

  def index = Action {
    Ok(views.html.index("Your new application is ready."))
  }

  def survey = Action { request =>
    val bodyAsJson = Json.toJson(request.body.asFormUrlEncoded.get)
    val result: JsResult[SlackPostData] = bodyAsJson.validate[SlackPostData]

    result match {
      case success: JsSuccess[SlackPostData] =>
        validateSubmissionAndReturnResponse(success)
      case error: JsError =>
        returnErrorResponse(error)
    }
  }

  private def validateSubmissionAndReturnResponse(success: JsSuccess[SlackPostData]): Result = {
    val userSubmission = UserSubmission.createFromSlackPostData(success.get)
    userSubmission.validate
    if (userSubmission.errors.isEmpty) {
      saveSubmissionAndReturnResponse(userSubmission)
    } else {
      Ok("There was a problem with your submission.\n - " + userSubmission.errors.mkString("\n - "))
    }

  }

  private def saveSubmissionAndReturnResponse(userSubmission: UserSubmission): Result = {
    val id: Option[Long] = repository.create(userSubmission)

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
    JsError.toJson(error).value.contains("obj.user_name[0]")
  }

  private def isTokenError(error: JsError): Boolean = {
    JsError.toJson(error).value.contains("obj.token[0]")
  }

  def data = Action {
    val submissions = repository.getAll
    Ok(views.html.data("Data")(submissions))
  }
}
