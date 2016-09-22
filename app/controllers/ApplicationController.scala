package controllers

import models.{UserSubmissionRepository, SlackPostData, PostgresUserSubmissionUserSubmissionRepository}
import play.api.libs.json._
import play.api.mvc._

class ApplicationController extends Controller {

  def index = Action {
    Ok(views.html.index("Your new application is ready."))
  }

  def survey = Action { request =>
    val bodyAsJson = Json.toJson(request.body.asFormUrlEncoded.get)
    val result: JsResult[SlackPostData] = bodyAsJson.validate[SlackPostData]

    result match {
      case success: JsSuccess[SlackPostData] => {
        Ok("Success!")
      }
      case error: JsError => if (isTokenError(error)) Unauthorized else Ok("Your submission is wrong.")
    }
  }

  private def isTokenError(error: JsError): Boolean = {
    JsError.toJson(error).value.contains("obj.token[0]")
  }

  def data(repository: UserSubmissionRepository = PostgresUserSubmissionUserSubmissionRepository()) = Action {
    val submissions = repository.getAll
    Ok(views.html.data("Data")(submissions))
  }
}
