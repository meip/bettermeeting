package controllers

import dao.UserDao
import models.User
import models.UserFormats._
import play.Logger
import play.api.libs.concurrent.Execution.Implicits.defaultContext
import play.api.libs.json._
import play.api.mvc._
import reactivemongo.extensions.json.dsl.JsonDsl

import scala.concurrent.Future

object Users extends Controller with JsonDsl {

  def list = Action.async {
    UserDao.listUsers.map {
      case Nil => Ok(Json.toJson(""))
      case users => Ok(Json.toJson(users))
    }
  }


  /**
   * Creates and persists meetings with coming HTTP request data.
   *
   * @return A Ok [[play.api.mvc.Result]] or InternalServerError [[play.api.mvc.Results.Status]]
   */
  def create = Action.async(BodyParsers.parse.json) { implicit request =>
    val userResult = request.body.validate[User]
    userResult.fold(
      errors => {
        Future.successful(BadRequest(Json.obj("status" -> "NOT OK", "message" -> JsError.toFlatJson(errors))))
      },
      user => {
        UserDao.createUser(user).map(
          _ => Ok(Json.obj("status" -> "OK", "message" -> s"User ${user.firstName} created."))).recover {
          case t: Throwable =>
            Logger.error("CREATE ERROR", t)
            InternalServerError("Unknown error (CREATE).")
        }
      }
    )
  }

  /**
   * Lists all user for E-Mail.
   *
   * @param email E-Mail attribute of users.
   * @return A Ok [[play.api.mvc.Result]]
   */
  def findByEmail(email: String) = Action.async {
    UserDao.findByEMail(email).map {
      case None => Ok(Json.toJson(""))
      case user => Ok(Json.toJson(user))
    }
  }

}
