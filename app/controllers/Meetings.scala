package controllers

import dao.dao.MeetingDao
import models.Meeting
import models.MeetingFormats._
import org.slf4j.{Logger, LoggerFactory}
import play.api.libs.concurrent.Execution.Implicits.defaultContext
import play.api.libs.json._
import play.api.mvc._
import reactivemongo.bson.BSONObjectID
import reactivemongo.extensions.json.dsl.JsonDsl

import scala.concurrent.Future

class Meetings extends Controller with JsonDsl {

  private final val logger: Logger = LoggerFactory.getLogger(classOf[Meetings])

  /**
   * Creates and persists meetings with coming HTTP request data.
   *
   * @return A Ok [[play.api.mvc.Result]] or InternalServerError [[play.api.mvc.Results.Status]]
   */
  def create = Action.async(BodyParsers.parse.json) { implicit request =>
    val meetingResult = request.body.validate[Meeting]
    meetingResult.fold(
      errors => {
        Future.successful(BadRequest(Json.obj("status" -> "NOT OK", "message" -> JsError.toFlatJson(errors))))
      },
      meeting => {
        MeetingDao.createMeeting(meeting).map(
          _ => Created(Json.obj("status" -> "OK", "message" -> s"Meeting ${meeting.goal} sent."))).recover {
          case t: Throwable =>
            logger.error("CREATE ERROR", t)
            InternalServerError("Unknown error (CREATE).")
        }
      }
    )
  }

  /**
   * Updates meeting
   *
   * @param id BSONObject will be updated.
   * @return A Ok [[play.api.mvc.Result]] or InternalServerError [[play.api.mvc.Results.Status]]
   */
  def update(id: BSONObjectID) = Action.async(BodyParsers.parse.json) { implicit request =>
    val meetingResult = request.body.validate[Meeting]
    meetingResult.fold(
      errors => {
        Future.successful(BadRequest(Json.obj("status" -> "NOT OK", "message" -> JsError.toFlatJson(errors))))
      },
      meeting => {
        meeting._id = Some(id)
        MeetingDao.updateById(id, meeting).map(_ => Ok(Json.obj("status" -> "OK", "message" -> "Meeting updated"))).recover {
          case t: Throwable =>
            logger.error("UPDATE ERROR", t)
            InternalServerError("Unknown error (UPDATE).")
        }
      }
    )
  }

  /**
   * Get Meeting.
   *
   * @param id ID of the meeting.
   * @return A Ok [[play.api.mvc.Result]]
   */
  def get(id: BSONObjectID) = Action.async {
    MeetingDao.get(id).map {
      case None => Ok(Json.toJson(""))
      case meeting => Ok(Json.toJson(meeting))
    }
  }


  /**
   * Gets all Meeting.
   *
   * @return A Ok [[play.api.mvc.Result]]
   */
  def list = Action.async {
    MeetingDao.listMeetings.map {
      case Nil => Ok(Json.toJson(""))
      case meetings => Ok(Json.toJson(meetings))
    }
  }

  /**
   * Deletes a meeting from database.
   *
   * @param id BSONObject will deleted.
   * @return A Ok [[play.api.mvc.Result]] or InternalServerError [[play.api.mvc.Results.Status]]
   */
  def delete(id: BSONObjectID) = Action.async {
    MeetingDao.deleteMeeting(id).map(_ => Ok(Json.obj("status" -> "OK", "message" -> "Meeting deleted"))).recover {
      case t: Throwable =>
        logger.error("DELETE ERROR", t)
        InternalServerError("Unknown error (DELETE).")
    }
  }

}
