package models

case class UserSubmission(id: Option[Long] = None, text: String, username: String) extends Validatable {

  private val activityType: String = text.toUpperCase().split(" ").head
  private val numberOfRequiredParts: Int = if (isMeeting) 2 else 4
  private val requiredForActivity: String = if (isMeeting) UserSubmission.REQUIRED_FOR_MEETING else UserSubmission.REQUIRED_FOR_STORY_OR_BUG

  private val activityTypeChecking = () => {
    UserSubmission.ACTIVITY_TYPES.contains(activityType)
  }

  private val hasAllParts = () => {
    val parts = text.split(" ")

    parts.length >= numberOfRequiredParts
  }

  private def isMeeting: Boolean = {
   activityType == UserSubmission.MEETING
  }

  override val validations: Seq[Validation] = List(
    Validation(UserSubmission.ACTIVITY_TYPE_IS_REQUIRED, activityTypeChecking),
    Validation(requiredForActivity, hasAllParts)
  )
}

object UserSubmission {
  val BUG = "BUG"
  val MEETING = "MEETING"
  val STORY = "STORY"
  val ACTIVITY_TYPES = List(BUG, MEETING, STORY)
  val ACTIVITY_TYPE_IS_REQUIRED = "Activity type is required: BUG, STORY, MEETING"
  val REQUIRED_FOR_MEETING = "Hours spent on activity is required"
  val REQUIRED_FOR_STORY_OR_BUG =
    """
      | - Hours spent on activity is required
      | - Activity ID is required
      | - Percentage of time trying to understand the code is required
    """.stripMargin

  def createFromSlackPostData(data: SlackPostData): UserSubmission = {
    UserSubmission(text = data.text, username = data.username)
  }
}