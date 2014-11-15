package actors

import java.io.File
import javax.mail._
import javax.mail.internet.MimeMultipart
import javax.mail.search.FlagTerm

import akka.actor.Actor
import com.google.api.client.googleapis.auth.oauth2.GoogleCredential
import com.google.api.client.http.HttpTransport
import com.google.api.client.http.javanet.NetHttpTransport
import com.google.api.client.json.JsonFactory
import com.google.api.client.json.jackson2.JacksonFactory
import com.sun.mail.imap.IMAPStore
import dao.MeetingDao
import mail.oauth2.OAuth2Authenticator
import models.MeetingFormats._
import models.{ActionPoint, Decision, Meeting, Vote}
import net.fortuna.ical4j.data.CalendarBuilder
import net.fortuna.ical4j.model.component.VEvent
import net.fortuna.ical4j.model.{Date, Property, PropertyList}
import org.joda.time.DateTime
import play.api.Play.current
import play.api.{Logger, Play}

import scala.collection.JavaConversions._
import scala.concurrent.duration._
import scala.language.postfixOps

case object PollEmail

class MailActor(host: String, port: Int, userEmail: String, accountId: String, certPath: String, debug: Boolean) extends Actor {
  lazy val log = Logger("application." + this.getClass.getName)
  private val HTTP_TRANSPORT: HttpTransport = new NetHttpTransport
  private val JSON_FACTORY: JsonFactory = new JacksonFactory
  private val GMAIL_SCOPE: String = "https://mail.google.com/"

  import context.dispatcher

  val tick = context.system.scheduler.schedule(30 seconds, 30 seconds, self, PollEmail)

  override def postStop() = tick.cancel

  def receive = {
    case PollEmail => try {
      val imapSslStore: IMAPStore = authenticateGmail
      val folder = imapSslStore.getFolder("Inbox")
      folder.open(Folder.READ_WRITE)
      val flagUnseen = new FlagTerm(new Flags(Flags.Flag.SEEN), false)

      for (msg <- folder.search(flagUnseen)) {
        val subject = msg.getSubject
        Logger.info("Found email: " + subject)
        Logger.debug("Found email: " + msg.getContentType)
        val content = msg.getContent.asInstanceOf[MimeMultipart]
        content match {
          case multiPart: Multipart => {
            for (i <- 0 until multiPart.getCount) {
              val attachement = content.getBodyPart(i)
              if (attachement.getContentType.startsWith("TEXT/CALENDAR")) {
                // Outlook Inventation
                createMeetingFromAttachement(attachement)
              }
              if (attachement.getDisposition != null) {
                attachement.getDisposition.toLowerCase match {
                  case Part.ATTACHMENT => {
                    //Gmail
                    createMeetingFromAttachement(attachement)
                  }
                  case _ => Unit
                }
              }

            }
          }
          case _ => Unit
        }
        msg.setFlag(Flags.Flag.SEEN, true)
      }
      imapSslStore.close()
    } catch {
      case e: Exception => log.warn("Exception connection to Gmail", e)
    }
  }

  private def createMeetingFromAttachement(attachement: BodyPart) = {
    Logger.debug("Got Attachment: " + attachement.getFileName)
    val icsCal = new CalendarBuilder
    val calendar = icsCal.build(attachement.getInputStream)
    val vEvent = calendar.getComponent("VEVENT").asInstanceOf[VEvent]
    Logger.debug("Event: " + vEvent)
    val summary = vEvent.getSummary.getValue
    val startDate = vEvent.getStartDate.getDate
    val organizer = vEvent.getOrganizer.getValue
    val attendees = vEvent.getProperties(Property.ATTENDEE)
    val icsUuid = vEvent.getProperty(Property.UID).getValue
    createMeeting(organizer, summary, icsUuid, startDate, attendees)
  }

  private def createMeeting(organizer: String, summary: String, icsUuid: String, startDate: Date, attendees: PropertyList) = {
    val attendeeList = attendees.map(p => stripMailto(p.getValue)).toList
    MeetingDao.findMeetingsForIcsUuid(icsUuid).map(meetingList => {
      meetingList.headOption match {
        case Some(meeting) => {
          Logger.info("existing meeting found!! Update existing meeting")
          MeetingDao.updateById(meeting._id.get, meeting.copy(
            date = Some(new DateTime(startDate.getTime)),
            goal = summary,
            organizer = stripMailto(organizer),
            attendees = attendeeListFiler(attendeeList, stripMailto(organizer))
          ))
        }
        case None => {
          Logger.info("insert new meeting!")
          val meeting = Meeting(_id = None,
            date = Some(new DateTime(startDate.getTime)),
            goal = summary,
            organizer = stripMailto(organizer),
            color = None,
            icsUuid = Some(icsUuid),
            attendees = attendeeListFiler(attendeeList, stripMailto(organizer)),
            decisions = List.empty[Decision],
            actionPoints = List.empty[ActionPoint],
            votesUp = List.empty[Vote],
            votesDown = List.empty[Vote],
            created = Some(DateTime.now),
            updated = Some(DateTime.now)
          )
          MeetingDao.createMeeting(meeting)
        }
      }
    })
  }

  private def attendeeListFiler(attendeeList: List[String], organizer: String) = {
    attendeeList.filterNot(attendee => (attendee.equals(organizer) || attendee.equals(Play.configuration.getString("gmail.useremail").getOrElse("ibettermeeting@gmail.com"))))
  }

  private def stripMailto(string: String) = string.substring(7, string.length)

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

