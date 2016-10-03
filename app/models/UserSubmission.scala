package models

case class UserSubmission(id: Option[Long] = None, text: String, username: String) extends Validatable {
  private val required = (textToCheckFor: String) => {
    text.toLowerCase.contains(textToCheckFor)
  }

  val validations = List(Validation("story", UserSubmission.STORY_REQUIRED, required),
    Validation("total", UserSubmission.TOTAL_TIME_REQUIRED, required),
    Validation("add", UserSubmission.ADD_TECH_DEBT_REQUIRED, required),
    Validation("remove", UserSubmission.REMOVE_TECH_DEBT_REQUIRED, required))
}

object UserSubmission {
  val STORY_REQUIRED = "Story is required"
  val TOTAL_TIME_REQUIRED = "Total time is required"
  val ADD_TECH_DEBT_REQUIRED = "Time adding technical debt is required"
  val REMOVE_TECH_DEBT_REQUIRED = "Time removing technical debt is required"

  def createFromSlackPostData(data: SlackPostData): UserSubmission = {
    UserSubmission(text = data.text, username = data.username)
  }
}