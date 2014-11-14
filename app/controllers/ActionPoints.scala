package controllers

import dao.MeetingDao
import models.ActionPoint
import models.MeetingFormats._
import org.slf4j.{Logger, LoggerFactory}
import play.api.libs.concurrent.Execution.Implicits.defaultContext
import play.api.libs.json.Json
import play.api.mvc._
import reactivemongo.bson.BSONObjectID
import reactivemongo.extensions.json.dsl.JsonDsl
import services.Security

import scala.concurrent.Await
import scala.concurrent.duration.Duration

class ActionPoints extends Controller with JsonDsl with Security with AuthenticatedAction {

  private final val logger: Logger = LoggerFactory.getLogger(classOf[ActionPoints])

  /**
   * Gets all Meeting for user.
   *
   * @return A Ok [[play.api.mvc.Result]]
   */
  def findMyActionPoints = Authenticated.async { implicit request =>
    MeetingDao.findActionPointsForOwner(request.user.email).map {
      case actionPoints => Ok(Json.toJson(actionPoints))
    }
  }

  /**
   * Push a ActionPoint to a meeting
   *
   * @param id BSONObject will be updated.
   * @return A Ok [[play.api.mvc.Result]] or InternalServerError [[play.api.mvc.Results.Status]]
   */
  def pushActionPoint(id: BSONObjectID) = authenticatedActionWithRecover[ActionPoint](
    actionPoint => {
      if (actionPoint._id.isEmpty) actionPoint._id = Some(BSONObjectID.generate)
      val meeting = Await.result(MeetingDao.findById(id), Duration.fromNanos(500000000000l))
      MeetingDao.pushActionPoint(id, actionPoint)
    }, "ActionPoint pushed", "Unknown Error(push ActionPoint)"
  )

  /**
   * Updates ActionPoint
   *
   * @param id BSONObject will be updated.
   * @return A Ok [[play.api.mvc.Result]] or InternalServerError [[play.api.mvc.Results.Status]]
   */
  def update(id: BSONObjectID) = authenticatedActionWithRecover[ActionPoint](
    actionPoint => {
      actionPoint._id = Some(id)
      MeetingDao.updateActionPointById(id, actionPoint)
    }, "ActionPoint updated", "Unknown error (UPDATE) actionPoint"
  )

  /**
   * Deletes a actionPoint from the meeting.
   *
   * @param id BSONObject will deleted.
   * @return A Ok [[play.api.mvc.Result]] or InternalServerError [[play.api.mvc.Results.Status]]
   */
  def delete(id: BSONObjectID) = Authenticated.async {
    MeetingDao.deleteActionPoint(id).map(_ => Ok(Json.obj("status" -> "OK", "message" -> "ActionPoint deleted"))).recover {
      case t: Throwable =>
        logger.error("DELETE ERROR", t)
        InternalServerError("Unknown error (DELETE).")
    }
  }

}
