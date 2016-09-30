package models

case class SurveyRespondent (id: Option[Long] = None, username: String, survey_id: Int = 1) extends Validatable {
  private val unique = (textToCheckFor: String) => {
    var surveyRespondent: Option[SurveyRespondent] = SurveyRespondent.repo.findByUsername(this.username)
    if (surveyRespondent == null) {
      surveyRespondent = None
    }
    if (surveyRespondent.isDefined) false else true
  }

  override val validations: Seq[Validation] = List(Validation("username", SurveyRespondent.USERNAME_MUST_BE_UNIQUE, unique))
}

object SurveyRespondent {
  var repo: PostgresSurveyRespondentRepository = PostgresSurveyRespondentRepository()
  val USERNAME_MUST_BE_UNIQUE = "Username must be unique"
}