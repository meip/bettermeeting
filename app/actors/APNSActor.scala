package actors

import akka.actor.Actor
import com.notnoop.apns.APNS
import play.api.Logger

class APNSActor extends Actor {
  lazy val log = Logger("application." + this.getClass.getName)
  val service = APNS.newService.withCert("certs/Dev.p12", "bettermeeting").withSandboxDestination.build

  override def receive = {
    case PushNotification(deviceTokenList) => {
      log.debug("Push a notification")
      val payload=APNS.newPayload.alertBody("msgToSend").badge(1).sound("default").customField("myField","myFieldValue").build()
      deviceTokenList.foreach(service.push(_, payload))
    }

  }

}

sealed trait SocketMessage

case class PushNotification(deviceTokenList: List[String]) extends SocketMessage
