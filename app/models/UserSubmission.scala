package models

import scala.collection.mutable.ArrayBuffer

case class UserSubmission(id: Option[Long] = None, text: String, username: String) {
  def validate: Seq[String] = {
    var errors = ArrayBuffer[String]()
    for (element <- validations) {
      if (element._3(element._1)) {
        errors += element._2
      }
    }
    errors
  }

  val required = (textToCheckFor: String) => {
    !text.toLowerCase.contains(textToCheckFor)
  }

  val validations = List(("story:", "Story is required", required),
                          ("total:", "Total time is required", required),
                          ("add:", "Time adding technical debt is required", required),
                          ("remove:", "Time removing technical debt is required", required))

}

object UserSubmission {
  def createFromSlackPostData(data: SlackPostData): UserSubmission = {
    UserSubmission(text = data.text, username = data.username)
  }
}