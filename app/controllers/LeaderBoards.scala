package controllers

import dao.{LeaderBaordDao, MeetingDao, UserDao}
import models.LeaderBoardFormattedFormats._
import models.{User, LeaderBoard, LeaderBoardFormatted}
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
    MeetingDao.findDoneActionPointsForOwner(request.user.email).map {
      case actionPoints => Ok(Json.toJson(actionPoints.size))
    }
  }

  def getLeadingListOnGoal = Authenticated.async { implicit request =>
    val leaderBoard = new LeaderBaordDao("votesOnGoal")
    getLeadingList(leaderBoard, request.user)
  }

  def getLeadingListOnEfficiency = Authenticated.async { implicit request =>
    request
    val leaderBoard = new LeaderBaordDao("votesOnEfficiency")
    getLeadingList(leaderBoard, request.user)
  }

  private def getLeadingList(leaderBoard: LeaderBaordDao, user: User): Future[Result] = {
    val leaderBoardsFormatted = leaderBoard.mapReduceBoard.map(leaderBoardEntries => {
      leaderBoardEntries.zipWithIndex.map {
        case (leaderBoard: LeaderBoard, index: Int) => {
          UserDao.findByEMail(leaderBoard._id.get).map(_.map {
            case user => {
              LeaderBoardFormatted(user.firstName + " " + user.lastName, index + 1, leaderBoard.value.toInt, (leaderBoard._id.get.equals(user.email)))
            }
          })
        }
      }
    })
    leaderBoardsFormatted.flatMap(Future.sequence(_)).map(f => Ok(Json.toJson(f.flatMap(x => x))))
  }
}
