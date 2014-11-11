package controllers

import dao.LeaderBaordDao
import dao.dao.MeetingDao
import models.LeaderBoardFormats._
import org.slf4j.{Logger, LoggerFactory}
import play.api.libs.concurrent.Execution.Implicits.defaultContext
import play.api.libs.json._
import play.api.mvc._
import reactivemongo.extensions.json.dsl.JsonDsl
import services.Security

class LeaderBoards extends Controller with JsonDsl with Security with AuthenticatedAction {

  private final val logger: Logger = LoggerFactory.getLogger(classOf[LeaderBoards])

  def countMeetingOrganizer = Authenticated.async { implicit request =>
    MeetingDao.findMeetingsForOrganizer(request.user.email).map {
      case meetings => Ok(Json.toJson(meetings.size))
    }
  }

  def countMeetingAttendee = Authenticated.async { implicit request =>
    MeetingDao.findMeetingsForAttendee(request.user.email).map {
      case meetings => Ok(Json.toJson(meetings.size))
    }
  }

  def countActionPoints = Authenticated.async { implicit request =>
    MeetingDao.findActionPointsForOwner(request.user.email).map {
      case meetings => Ok(Json.toJson(meetings.size))
    }
  }

  def getLeadingList = Authenticated.async {
    LeaderBaordDao.leaderBoard.map(leaderBoardEntries => Ok(Json.toJson(leaderBoardEntries)))
  }
}
