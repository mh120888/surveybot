package models

case class UserSubmission(id: Option[Long] = None, text: String, username: String) extends Validatable {

  val activityTypeChecking = () => {
    val activityType: String = text.toUpperCase().split(" ").head
    UserSubmission.ACTIVITY_TYPES.contains(activityType)
  }

  override val validations: Seq[Validation] = List(
    Validation(UserSubmission.ACTIVITY_TYPE_IS_REQUIRED, activityTypeChecking)
  )
}

object UserSubmission {
  val BUG = "BUG"
  val MEETING = "MEETING"
  val STORY = "STORY"
  val ACTIVITY_TYPES = List(BUG, MEETING, STORY)
  val ACTIVITY_TYPE_IS_REQUIRED = "Activity type is required"

  def createFromSlackPostData(data: SlackPostData): UserSubmission = {
    UserSubmission(text = data.text, username = data.username)
  }
}