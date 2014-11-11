package actors

import akka.actor.Actor
import com.notnoop.apns.APNS
import models.Meeting
import play.api.Play.current
import play.api.{Logger, Play}

class APNSActor extends Actor {
  lazy val log = Logger("application." + this.getClass.getName)
  val service = Play.application.configuration.getBoolean("apns.sandbox") match {
    case Some(true) =>
      APNS
        .newService
        .withCert(ApnsKeystorePath, ApnsKeystorePass)
        .withSandboxDestination().build
    case _ =>
      APNS
        .newService
        .withCert(ApnsKeystorePath, ApnsKeystorePass)
        .withProductionDestination().build
  }

  override def receive = {
    case PushNotificationWithMeeting(token, meeting) => {
      log.debug("Push a notification with meeting object")
      val goal = meeting.goal
      val payload = APNS.newPayload.alertBody(s"Meeting $goal has ended. Please Vote").badge(2).sound("default").customField("meetingId", meeting._id.map(_.stringify).getOrElse("")).build()
      try {
        service.push(token, payload)
      } catch {
        case e: Exception => {
          log.error("Exception sending push", e)
        }
      }
    }
    case PushNotification(deviceTokenList, message) => {
      log.debug("Push a notification")
      val payload = APNS.newPayload.alertBody(message).badge(1).sound("default").build()
      deviceTokenList.foreach { token =>
        try {
          service.push(token, payload)
        } catch {
          case e: Exception => {
            log.error("Exception sending push", e)
          }
        }
      }
    }

  }

  protected def ApnsKeystorePath = Play.application.configuration.getString("apns.keystore.path").getOrElse("certs/Dev.p12")

  protected def ApnsKeystorePass = Play.application.configuration.getString("apns.keystore.pass").getOrElse("bettermeeting")

}

sealed trait SocketMessage

case class PushNotification(deviceTokenList: List[String], message: String) extends SocketMessage
case class PushNotificationWithMeeting(token: String, meeting: Meeting) extends SocketMessage