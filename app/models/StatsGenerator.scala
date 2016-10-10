package models

case class StatsGenerator(userSubmissions: List[UserSubmission]) {

  def getTotal(submissions: List[UserSubmission] = userSubmissions): Int = {
    submissions.map(_.getTotalTime).sum
  }

  def getPercentage(submissions: List[UserSubmission]): Float = {
    val totalHours = getTotal(userSubmissions)
    val activityHours = getTotal(submissions)

    (activityHours.toFloat/totalHours) * 100
  }

  def showPercentage(submissions: List[UserSubmission]): String = {
    s"${getPercentage(submissions).round}%"
  }

  def getAverageTime(submissions: List[UserSubmission]): Float = {
    getTotal(submissions).toFloat/submissions.length
  }
}
