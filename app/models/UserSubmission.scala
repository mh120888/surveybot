package models

case class UserSubmission(id: Option[Long] = None, text: String, username: String) {

}

object UserSubmission {
  def createFromSlackPostData(data: SlackPostData): UserSubmission = {
    UserSubmission(text = data.text, username = data.username)
  }
}