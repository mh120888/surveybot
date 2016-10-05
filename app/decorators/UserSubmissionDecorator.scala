package decorators

import org.joda.time.DateTime

trait UserSubmissionDecorator {
  val username: String
  val createdAt: DateTime
  val text: String

  def showDate: String = {
    s"${createdAt.dayOfWeek.getAsText} ${createdAt.toString("MM/d/yyyy")}"
  }

  def showActivityID: String = {
    text.split(" ")(1)
  }

  def showTimeForMeeting: String = {
    text.split(" ")(1)
  }

  def showTimeForStoryOrBug: String = {
    text.split(" ")(2)
  }

  def showPercentage: String = {
    text.split(" ")(3)
  }

  private def displayText: String = {
    val temp = text.split(" ")
    temp.slice(1, temp.length).mkString(" ")
  }
}