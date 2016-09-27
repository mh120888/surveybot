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

  def getErrors(): Seq[String] = {
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

  private val validations = List(Validation("story", "Story is required", required),
                                 Validation("total", "Total time is required", required),
                                 Validation("add", "Time adding technical debt is required", required),
                                 Validation("remove", "Time removing technical debt is required", required),
                                 Validation("total", "Total time must be in the range 0-12", inRange0To12),
                                 Validation("add", "Time adding technical debt must be in the range 0-12", inRange0To12),
                                 Validation("remove", "Time removing technical debt must be in the range 0-12", inRange0To12))
}

object UserSubmission {
  def createFromSlackPostData(data: SlackPostData): UserSubmission = {
    UserSubmission(text = data.text, username = data.username)
  }
}