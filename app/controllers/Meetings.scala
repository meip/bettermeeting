package controllers

import dao.dao.MeetingDao
import models.MeetingFormats._
import models.{ActionPoint, Meeting}
import org.slf4j.{Logger, LoggerFactory}
import play.api.libs.concurrent.Execution.Implicits.defaultContext
import play.api.libs.json._
import play.api.mvc._
import reactivemongo.bson.BSONObjectID
import reactivemongo.extensions.json.dsl.JsonDsl
import services.Security

import scala.concurrent.Await
import scala.concurrent.duration.Duration

class Meetings extends Controller with JsonDsl with Security with AuthenticatedAction {

  private final val logger: Logger = LoggerFactory.getLogger(classOf[Meetings])

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
      MeetingDao.updateById(id, meeting)
    }, "Meeting updated", "Unknown error (UPDATE) meeting"
  )


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

}
