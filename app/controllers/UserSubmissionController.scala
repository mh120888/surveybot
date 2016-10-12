package controllers

import com.google.inject.Inject
import models._
import org.joda.time.DateTime
import play.api.i18n.{I18nSupport, MessagesApi}
import play.api.libs.json._
import play.api.mvc._

class UserSubmissionController @Inject()(submissionRepository: PostgresUserSubmissionRepository = PostgresUserSubmissionRepository(),
                                         timeCalculator: TimeCalculator = TimeCalculator(),
                                         val messagesApi: MessagesApi) extends Controller with I18nSupport {
  def index = Action {
    Ok(views.html.index("Your new application is ready."))
  }

  def challenge = Action { request =>
    val requestAsJSON = request.body.asJson.get
    val challenge = (requestAsJSON \ "challenge").get.toString()

    println(challenge)
    Ok(challenge)
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

  def data(weeksAgo: Int) = Action {
    val today = new DateTime().minusWeeks(weeksAgo)
    val startDate = timeCalculator.getStartOfWeek(today)
    val endDate = timeCalculator.getEndOfWeek(today)
    val submissions = submissionRepository.getAllFromDateRange(startDate, endDate)
    val storySubmissions = submissions.filter(submission => submission.isStory)
    val bugSubmissions = submissions.filter(submission => submission.isBug)
    val meetingSubmissions = submissions.filter(submission => submission.isMeeting)

    Ok(views.html.data(s"Submissions from ${startDate.toString("MM/d/yyyy")} to ${endDate.toString("MM/d/yyyy")}")(weeksAgo)(storySubmissions)(bugSubmissions)(meetingSubmissions)(StatsGenerator(submissions)))
  }

  def allData = Action {
    val submissions = submissionRepository.getAll
    val storySubmissions = submissions.filter(submission => submission.isStory)
    val bugSubmissions = submissions.filter(submission => submission.isBug)
    val meetingSubmissions = submissions.filter(submission => submission.isMeeting)

    Ok(views.html.data("All Submissions")(0)(storySubmissions)(bugSubmissions)(meetingSubmissions)(StatsGenerator(submissions)))
  }

  private def requestBodyAsJson(request: Request[AnyContent]): JsValue = {
    Json.toJson(request.body.asFormUrlEncoded.get)
  }

  private def validateSubmissionAndReturnResponse(success: JsSuccess[SlackPostData]): Result = {
    val userSubmission = UserSubmission.createFromSlackPostData(success.get)
    if (userSubmission.isValid()) {
      saveSubmissionAndReturnResponse(userSubmission)
    } else {
      Ok(s"There was a problem with your submission: ${userSubmission.text}\n\n" + userSubmission.getErrors.mkString("\n\n"))
    }
  }

  private def saveSubmissionAndReturnResponse(userSubmission: UserSubmission): Result = {
    val id: Option[Long] = submissionRepository.create(userSubmission)
    if (id.isDefined) {
      Ok(s"${userSubmission.text} was successfully submitted!")
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
