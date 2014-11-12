package controllers

import dao.dao.MeetingDao
import dao.{LeaderBaordDao, UserDao}
import models.LeaderBoardFormattedFormats._
import models.{LeaderBoard, LeaderBoardFormatted}
import org.slf4j.{Logger, LoggerFactory}
import play.api.libs.concurrent.Execution.Implicits.defaultContext
import play.api.libs.json._
import play.api.mvc._
import reactivemongo.extensions.json.dsl.JsonDsl
import services.Security

import scala.concurrent.Future

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

  def getLeadingList = Authenticated.async { implicit request =>
    val leaderBoardsFormatted = LeaderBaordDao.leaderBoard.map(leaderBoardEntries => {
      leaderBoardEntries.zipWithIndex.map {
        case (leaderBoard: LeaderBoard, index: Int) => {
          UserDao.findByEMail(leaderBoard._id.get).map(_.map {
            case user => {
              LeaderBoardFormatted(user.firstName + " " + user.lastName, index + 1, leaderBoard.value.toInt, (leaderBoard._id.get.equals(request.user.email)))
            }
          })
        }
      }
    })
    leaderBoardsFormatted.flatMap(Future.sequence(_)).map(f => Ok(Json.toJson(f.flatMap(x => x))))
  }
}
