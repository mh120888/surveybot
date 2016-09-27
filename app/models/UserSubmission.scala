package models

import scala.collection.mutable
import scala.collection.mutable.ArrayBuffer

case class UserSubmission(id: Option[Long] = None, text: String, username: String) {
  def isValid(): Boolean = {
    this.validate()
    if (this.errors.isEmpty) true else false
  }

  private def validate(): Unit = {
    for (validation <- validations) {
      if (!validation.validationLogic(validation.field)) this.errors += validation.errorMessage
    }
  }

  def getErrors: Seq[String] = {
    this.errors
  }

  private var errors = ArrayBuffer[String]()

  private val required = (textToCheckFor: String) => {
    text.toLowerCase.contains(textToCheckFor)
  }

  private val inRange = (min: Int, max: Int, fieldToCheck: String) => {
    val submission: mutable.Map[String, String] = separateSubmission
    try {
      val time = submission.getOrElse(fieldToCheck, Float.PositiveInfinity.toString).toFloat
      (time >= min) && (time <= max)
    } catch {
      case _: NumberFormatException => false
    }
  }

  private val inRange0To12 = (fieldToCheck: String) => {
    inRange(0, 12, fieldToCheck)
  }

  private def separateSubmission: mutable.Map[String, String] = {
    var submission = scala.collection.mutable.Map[String, String]()
    val separatePieces = text.split(",").map(piece => piece.trim.split(":"))
    for (piece <- separatePieces) {
      if (piece.length > 1) {
        submission += (piece(0).trim.toLowerCase -> piece(1).trim)
      }
    }
    submission
  }

  private val validations = List(Validation("story", UserSubmission.STORY_REQUIRED, required),
                                 Validation("total", UserSubmission.TOTAL_TIME_REQUIRED, required),
                                 Validation("add", UserSubmission.ADD_TECH_DEBT_REQUIRED, required),
                                 Validation("remove", UserSubmission.REMOVE_TECH_DEBT_REQUIRED, required),
                                 Validation("total", UserSubmission.TOTAL_MUST_BE_IN_RANGE, inRange0To12),
                                 Validation("add", UserSubmission.ADD_MUST_BE_IN_RANGE, inRange0To12),
                                 Validation("remove", UserSubmission.REMOVE_MUST_BE_IN_RANGE, inRange0To12))

}

object UserSubmission {
  val STORY_REQUIRED = "Story is required"
  val TOTAL_TIME_REQUIRED = "Total time is required"
  val ADD_TECH_DEBT_REQUIRED = "Time adding technical debt is required"
  val REMOVE_TECH_DEBT_REQUIRED = "Time removing technical debt is required"
  val TOTAL_MUST_BE_IN_RANGE = "Total time must be in the range 0-12"
  val ADD_MUST_BE_IN_RANGE = "Time adding technical debt must be in the range 0-12"
  val REMOVE_MUST_BE_IN_RANGE = "Time removing technical debt must be in the range 0-12"

  def createFromSlackPostData(data: SlackPostData): UserSubmission = {
    UserSubmission(text = data.text, username = data.username)
  }
}