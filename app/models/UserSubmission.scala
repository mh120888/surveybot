package models

import scala.collection.mutable.ArrayBuffer

case class UserSubmission(id: Option[Long] = None, text: String, username: String) {
  var errors = ArrayBuffer[String]()

  def isValid(): Boolean = {
    validate
    if (errors.isEmpty) true else false
  }

  private def validate: Unit = {
    for (validation <- validations) {
      if (validation.validationLogic(validation.field)) errors += validation.errorMessage
    }
  }

  private val required = (textToCheckFor: String) => {
    !text.toLowerCase.contains(textToCheckFor)
  }

  private val validations = List(Validation("story:", "Story is required", required),
                                 Validation("total:", "Total time is required", required),
                                 Validation("add:", "Time adding technical debt is required", required),
                                 Validation("remove:", "Time removing technical debt is required", required))


}

object UserSubmission {
  def createFromSlackPostData(data: SlackPostData): UserSubmission = {
    UserSubmission(text = data.text, username = data.username)
  }
}