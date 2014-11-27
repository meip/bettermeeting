package controllers

import actors.{APNSActor, PushNotificationWithMeeting}
import akka.actor.Props
import dao.{MeetingDao, UserDao}
import models.MeetingFormats._
import models.{Meeting, User, Vote}
import org.slf4j.{Logger, LoggerFactory}
import play.api.libs.concurrent.Execution.Implicits.defaultContext
import play.api.libs.json._
import play.api.mvc._
import play.libs.Akka
import reactivemongo.bson.BSONObjectID
import reactivemongo.extensions.json.dsl.JsonDsl
import services.Security

import scala.concurrent.duration.Duration
import scala.concurrent.{Await, Future}

class Meetings extends Controller with JsonDsl with Security with AuthenticatedAction {

  private final val logger: Logger = LoggerFactory.getLogger(classOf[Meetings])

  /**
   * Get Meeting.
   *
   * @param id ID of the meeting.
   * @return A Ok [[play.api.mvc.Result]]
   */
  def get(id: BSONObjectID) = Authenticated.async {
    MeetingDao.get(id).map {
      case meeting => Ok(Json.toJson(meeting))
    }
  }

  /**
   * Gets all Meeting.
   *
   * @return A Ok [[play.api.mvc.Result]]
   */
  def list = Authenticated.async {
    MeetingDao.listMeetings.map {
      case meetings => Ok(Json.toJson(meetings))
    }
  }

  /**
   * Gets all Meeting for user.
   *
   * @return A Ok [[play.api.mvc.Result]]
   */
  def findMyMeetings = Authenticated.async { implicit request =>
    MeetingDao.findMeetingsForUser(request.user.email).map {
      case meetings => Ok(Json.toJson(meetings))
    }
  }

  /**
   * Creates and persists meetings with coming HTTP request data.
   *
   * @return A Ok [[play.api.mvc.Result]] or InternalServerError [[play.api.mvc.Results.Status]]
   */
  def create = authenticatedAction[Meeting](meeting => {
    MeetingDao.createMeeting(meeting).map(
      _ => {
        val lastId = for {
          lastHead <- Await.result(MeetingDao.findAll(sort = $id(-1)), Duration.fromNanos(500000000l)).headOption
          id <- lastHead._id
        } yield id
        meeting._id = lastId
        Created(Json.obj("status" -> "OK", "message" -> JsString(meeting._id.get.stringify)))
      }).recover {
      case t: Throwable =>
        logger.error("CREATE ERROR", t)
        InternalServerError("Unknown error (CREATE).")
    }
  })

  /**
   * Updates meeting
   *
   * @param id BSONObject will be updated.
   * @return A Ok [[play.api.mvc.Result]] or InternalServerError [[play.api.mvc.Results.Status]]
   */
  def update(id: BSONObjectID) = authenticatedActionWithRecover[Meeting](
    meeting => {
      meeting._id = Some(id)
      meeting.actionPoints.filter(_._id.isEmpty).foreach(_._id = Some(BSONObjectID.generate))
      meeting.decisions.filter(_._id.isEmpty).foreach(_._id = Some(BSONObjectID.generate))
      MeetingDao.updateById(id, meeting)
    }, "Meeting updated", "Unknown error (UPDATE) meeting"
  )

  /**
   * Votes a meeting on goal
   *
   * @param id BSONObject will be updated.
   * @return A Ok [[play.api.mvc.Result]] or InternalServerError [[play.api.mvc.Results.Status]]
   */
  def voteOnGoal(id: BSONObjectID, voteValue: Int) = Authenticated.async { implicit request =>
    MeetingDao.voteOnGoal(id, Vote(request.user.email, voteValue, None, None)).map(_ => Ok(Json.obj("status" -> "OK", "message" -> "Vote on goal succeed"))).recover {
      case t: Throwable =>
        logger.error("VoteUp error", t)
        InternalServerError("Unknown error (voteOnGoal).")
    }
  }

  /**
   * Votes a meeting on efficiency
   *
   * @param id BSONObject will be updated.
   * @return A Ok [[play.api.mvc.Result]] or InternalServerError [[play.api.mvc.Results.Status]]
   */
  def voteOnEfficiency(id: BSONObjectID, voteValue: Int) = Authenticated.async { implicit request =>
    MeetingDao.voteOnEfficiency(id, Vote(request.user.email, voteValue, None, None)).map(_ => Ok(Json.obj("status" -> "OK", "message" -> "Vote on efficiency succeed"))).recover {
      case t: Throwable =>
        logger.error("Vote efficiency error", t)
        InternalServerError("Unknown error (voteOnEfficiency).")
    }
  }

  /**
   * Deletes a meeting from database.
   *
   * @param id BSONObject will deleted.
   * @return A Ok [[play.api.mvc.Result]] or InternalServerError [[play.api.mvc.Results.Status]]
   */
  def delete(id: BSONObjectID) = Authenticated.async {
    MeetingDao.deleteMeeting(id).map(_ => Ok(Json.obj("status" -> "OK", "message" -> "Meeting deleted"))).recover {
      case t: Throwable =>
        logger.error("DELETE ERROR", t)
        InternalServerError("Unknown error (DELETE).")
    }
  }

  def finish(id: BSONObjectID) = Authenticated.async { implicit request =>
    MeetingDao.findById(id).map {
      case Some(meeting: Meeting) => {
        val apnsActor = Akka.system.actorOf(Props[APNSActor])
        val goal = meeting.goal
        (meeting.attendees :+ meeting.organizer).map(attendeeEmail => {
          UserDao.findByEMail(attendeeEmail).map {
            case Some(user: User) => user.pushToken.map(pushToken => {
              val token = pushToken
              apnsActor ! PushNotificationWithMeeting(pushToken, meeting)
              user
            })
          }
        })
        MeetingDao.updateById(id, meeting.copy(status = Some("finished")))
      }
    }.map(_ => Ok(Json.obj("status" -> "OK", "message" -> "Meeting finished"))).recover {
      case t: Throwable =>
        logger.error("FINISH MEETING ERROR", t)
        InternalServerError("Unknown error (finish).")
    }
  }
}
