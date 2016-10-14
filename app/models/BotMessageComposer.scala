package models

case class BotMessageComposer() {

  val GREETINGS = List("Howdy!", "What's up doc?", "Hello there!", "Suh dude!", "Hope you're having a great day!", "G'day mate!")
  val PROMPT = "Please input your time today. Type `help` for some helpful hints on the expected format."

  def createMessage(surveyRespondent: SurveyRespondent): BotMessage = {
    BotMessage(surveyRespondent.username, getText)
  }

  private def getText: String = {
    s"${getGreeting}\n ${PROMPT}"
  }

  private def getGreeting: String = {
    val randomNumber = Math.random() * GREETINGS.length
    GREETINGS(randomNumber.toInt)
  }
}
