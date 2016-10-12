package models

case class StatsGenerator(userSubmissions: List[UserSubmission]) {

  def getTotalTime(submissions: List[UserSubmission] = userSubmissions): Int = {
    submissions.map(_.getTotalTime).sum
  }

  def getPercentage(submissions: List[UserSubmission]): Float = {
    val totalTime = getTotalTime(userSubmissions)
    val activityTime = getTotalTime(submissions)

    (activityTime.toFloat/totalTime) * 100
  }

  def showPercentage(submissions: List[UserSubmission]): String = {
    s"${getPercentage(submissions).round}%"
  }

  def getAverageTotalTime(submissions: List[UserSubmission]): Float = {
    getTotalTime(submissions).toFloat/submissions.length
  }

  def getAverageTimeUnderstandingCode(submissions: List[UserSubmission]): Float = {
    val sum = submissions.map(_.getTimeUnderstandingCode).sum.toFloat
    sum/submissions.length
  }

  def getTotalPercentage: Int = {
    if (userSubmissions.length == 0) 0 else 100
  }
}
