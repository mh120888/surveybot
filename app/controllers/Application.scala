package controllers

import play.api.libs.json.{JsDefined, JsLookupResult}
import play.api.mvc._

class Application extends Controller {

  def index = Action {
    Ok(views.html.index("Your new application is ready."))
  }

  def survey = Action { request =>
    val result: JsLookupResult = request.body.asJson.get \ "text"
    result match {
      case JsDefined(result) => Ok("Success!")
      case _ => Ok("Your submission is wrong.")
    }
  }

}
