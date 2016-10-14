package tasks

import akka.actor.ActorSystem
import com.google.inject.{AbstractModule, Inject}
import models.{BotMessageComposer, BotMessageSender, PostgresSurveyRespondentRepository}
import org.joda.time.{DateTime, Seconds}
import play.api.Play.current
import play.api.inject.ApplicationLifecycle
import play.api.libs.concurrent.Execution.Implicits.defaultContext
import play.api.libs.ws.WS

import scala.concurrent.Future
import scala.concurrent.duration._

class BotTaskModule extends AbstractModule {
  override def configure() = {
    bind(classOf[BotTask]).asEagerSingleton()
  }
}

class BotTask @Inject() (actorSystem: ActorSystem, lifecycle: ApplicationLifecycle) {
    val currentTime = new DateTime()
    val targetTime = currentTime.withHourOfDay(16).withMinuteOfHour(45).withSecondOfMinute(0)
    val delayInSeconds: Int = Seconds.secondsBetween(currentTime, targetTime).getSeconds

    actorSystem.scheduler.schedule(delayInSeconds.second, 1.day) {
      if (play.api.Play.isDev(play.api.Play.current)) {
        val respondentRepository = PostgresSurveyRespondentRepository()
        val surveyRespondents = respondentRepository.getAll()
        val messageComposer = BotMessageComposer()
        val sender = BotMessageSender(WS.client)

        for (surveyRespondent <- surveyRespondents) {
          val message = messageComposer.createMessage(surveyRespondent)
          sender.sendMessage(message)
        }
      }
    }

  lifecycle.addStopHook{ () =>
    Future.successful(actorSystem.shutdown())
  }
}