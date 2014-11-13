package actors

import java.io.File
import javax.mail.search.FlagTerm
import javax.mail.{Flags, Folder}

import akka.actor.Actor
import com.google.api.client.googleapis.auth.oauth2.GoogleCredential
import com.google.api.client.http.HttpTransport
import com.google.api.client.http.javanet.NetHttpTransport
import com.google.api.client.json.JsonFactory
import com.google.api.client.json.jackson2.JacksonFactory
import com.sun.mail.imap.IMAPStore
import mail.oauth2.OAuth2Authenticator
import play.api.Logger
import scala.collection.JavaConversions._

import scala.concurrent.duration._

case object PollEmail

class MailActor(host: String, port: Int, userEmail: String, accountId: String, certPath: String, debug: Boolean) extends Actor {
  lazy val log = Logger("application." + this.getClass.getName)
  private val HTTP_TRANSPORT: HttpTransport = new NetHttpTransport
  private val JSON_FACTORY: JsonFactory = new JacksonFactory
  private val GMAIL_SCOPE: String = "https://mail.google.com/"

  import context.dispatcher

  val tick = context.system.scheduler.schedule(30 seconds, 30 seconds, self, PollEmail)

  override def postStop() = tick.cancel()

  def receive = {
    case PollEmail => try {
      val imapSslStore: IMAPStore = authenticateGmail
      val folder = imapSslStore.getFolder("Inbox")
      folder.open(Folder.READ_WRITE)
      val flagUnseen = new FlagTerm(new Flags(Flags.Flag.SEEN), false)

      //for (msg <- folder.search(flagUnseen)) {
      for (msg <- folder.getMessages) {
        Logger.info("Found email: " + msg.getSubject)
        Logger.info("Found email: " + msg.getContentType)
        msg.setFlag(Flags.Flag.SEEN, true)
      }
      imapSslStore.close()
    } catch {
      case e: Exception => log.warn("Exception connection to Gmail", e)
    }
  }

  private def authenticateGmail = {
    val authToken: String = getAccessToken(userEmail)
    OAuth2Authenticator.initialize
    OAuth2Authenticator.connectToImap(host, port, userEmail, authToken, debug)
  }

  private def getAccessToken(email: String): String = {
    val credential: GoogleCredential = new GoogleCredential.Builder()
      .setTransport(HTTP_TRANSPORT)
      .setJsonFactory(JSON_FACTORY)
      .setServiceAccountId(accountId)
      .setServiceAccountScopes(List(GMAIL_SCOPE))
      .setServiceAccountPrivateKeyFromP12File(new File(certPath))
      .setServiceAccountUser(email).build
    credential.refreshToken
    return credential.getAccessToken
  }
}

