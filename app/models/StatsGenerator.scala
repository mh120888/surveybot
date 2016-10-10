package models

case class StatsGenerator(userSubmissions: List[UserSubmission]) {

  def getTotalTime: Int = {
    userSubmissions.map(_.getTotalTime).sum
  }

  def getTotalTimeForMeeting: Int = {
    userSubmissions.filter(submission => submission.isMeeting).map(_.getTotalTime).sum
  }

  def getTotalTimeForStory: Int = {
    userSubmissions.filter(submission => submission.isStory).map(_.getTotalTime).sum
  }

  def getTotalTimeForBug: Int = {
    userSubmissions.filter(submission => submission.isBug).map(_.getTotalTime).sum
  }

  def getPercentageForMeeting: Float = {
    val totalhours = getTotalTime
    val meetingHours = getTotalTimeForMeeting

    (meetingHours.toFloat/totalhours) * 100
  }

  def getPercentageForStory: Float = {
    val totalhours = getTotalTime
    val storyHours = getTotalTimeForStory

    (storyHours.toFloat/totalhours) * 100
  }

  def getPercentageForBug: Float = {
    val totalhours = getTotalTime
    val bugHours = getTotalTimeForBug

    (bugHours.toFloat/totalhours) * 100
  }
}
