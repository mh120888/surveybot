package models

import decorators.UserSubmissionDecorator
import org.joda.time.DateTime

case class UserSubmission(id: Option[Long] = None, createdAt: DateTime = new DateTime(), text: String = "", username: String = "") extends Validatable with UserSubmissionDecorator {

  private val activityType: String = text.toUpperCase().split(" ").head
  private val numberOfRequiredParts: Int = if (isMeeting) 2 else 4

  def isStory: Boolean = {
    isType(UserSubmission.STORY)
  }

  def isBug: Boolean = {
    isType(UserSubmission.BUG)
  }

  def isMeeting: Boolean = {
    isType(UserSubmission.MEETING)
  }

  def getTotalTime: Int = {
    val index = if (isMeeting) 1 else 2

    try {
      text.split(" ")(index).toInt
    } catch {
      case e: Exception => 0
    }
  }

  private def isType(typeIdentifier: String) = {
    activityType == typeIdentifier
  }

  private val requiredForActivity: String = {
    if (isMeeting) {
      UserSubmission.REQUIRED_FOR_MEETING
    } else if (isStoryOrBug) {
      UserSubmission.REQUIRED_FOR_STORY_OR_BUG
    } else {
      UserSubmission.REQUIRED_FOR_MEETING + "\n\n" + UserSubmission.REQUIRED_FOR_STORY_OR_BUG
    }
  }

  private val activityTypeChecking = () => {
    UserSubmission.ACTIVITY_TYPES.contains(activityType)
  }

  private val hasAllParts = () => {
    val parts = text.split(" ")

    parts.length == numberOfRequiredParts
  }

  private def isStoryOrBug: Boolean = {
    isStory || isBug
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
  val REQUIRED_FOR_MEETING = "For MEETING:\n * Hours spent on activity is required"
  val REQUIRED_FOR_STORY_OR_BUG = "For STORY or BUG:\n * Hours spent on activity is required\n * Activity ID is required\n * Percentage of time trying to understand the code is required"

  def createFromSlackPostData(data: SlackPostData): UserSubmission = {
    UserSubmission(text = data.text, username = data.username)
  }
}