package controllers

import dao.dao.MeetingDao
import models.MeetingFormats._
import models.MeetingPoint
import org.slf4j.{Logger, LoggerFactory}
import play.api.libs.concurrent.Execution.Implicits.defaultContext
import play.api.libs.json._
import play.api.mvc._
import reactivemongo.bson.BSONObjectID
import reactivemongo.extensions.json.dsl.JsonDsl

import scala.concurrent.Future

class MeetingPoints extends Controller with JsonDsl {

  private final val logger: Logger = LoggerFactory.getLogger(classOf[MeetingPoints])

  /**
   * Get meetingpoints for a User.
   *
   * @param id ID of the user.
   * @return A Ok [[play.api.mvc.Result]]
   */
  def owner(id: BSONObjectID) = Action.async {
    MeetingDao.findMeetingPointsForOwner(id).map(meeting => Ok(Json.toJson(meeting))).recover {
      case t: Throwable =>
        logger.error("Find ERROR", t)
        InternalServerError("Unknown error (Find).")
    }
  }

  /**
   * Push a MeetingPoint to a meeting
   *
   * @param id BSONObject will be updated.
   * @return A Ok [[play.api.mvc.Result]] or InternalServerError [[play.api.mvc.Results.Status]]
   */
  def push(id: BSONObjectID) = Action.async(BodyParsers.parse.json) { implicit request =>
    val meetingPointResult = request.body.validate[MeetingPoint]
    meetingPointResult.fold(
      errors => {
        Future.successful(BadRequest(Json.obj("status" -> "NOT OK", "message" -> JsError.toFlatJson(errors))))
      },
      meetingPoint => {
        MeetingDao.pushMeetingPoint(id, meetingPoint).map(_ => Ok(Json.obj("status" -> "OK", "message" -> "MeetingPoint pushed"))).recover {
          case t: Throwable =>
            logger.error("UPDATE ERROR", t)
            InternalServerError("Unknown error (UPDATE).")
        }
      }
    )
  }

}
