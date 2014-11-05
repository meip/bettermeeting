package controllers

import dao.UserDao
import models.User
import models.UserFormats._
import play.Logger
import play.api.libs.concurrent.Execution.Implicits.defaultContext
import play.api.libs.json._
import play.api.mvc._
import reactivemongo.extensions.json.dsl.JsonDsl
import services.Security

import scala.concurrent.duration.Duration
import scala.concurrent.{Await, Future}

object Users extends Controller with JsonDsl with Security {

  def list = Authenticated.async {
    implicit request => {
      UserDao.listUsers.map {
        case Nil => Ok(Json.toJson(""))
        case users => Ok(Json.toJson(users))
      }
    }
  }

  def login = Action {
    implicit request => {
      val query = request.queryString.map { case (k, v) => k -> v.mkString}
      val username = query get ("username") orElse (request.body.asJson.flatMap(json => (json \ "username").asOpt[String]))
      val password = query get ("password") orElse (request.body.asJson.flatMap(json => (json \ "password").asOpt[String]))
      (username, password) match {
        case (Some(u), Some(p)) => {
          Await.result(UserDao.findByEMail(u), Duration.fromNanos(5000000000l)).filter(user => user.checkPassword(p)).map(user => {
            // Login logic
            Ok("Login success").withSession("username" -> user.email)
          }).getOrElse(Unauthorized("Login failed"))
        }
        case _ => Unauthorized("Login parameter missing")
      }
    }
  }

  def logout = Action { implicit request =>
    Ok("Logged out").withNewSession
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
          _ => Created(Json.obj("status" -> "OK", "message" -> s"User ${user.firstName} created."))).recover {
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
  def findByEmail(email: String) = Authenticated.async {
    UserDao.findByEMail(email).map {
      case None => Ok(Json.toJson(""))
      case user => Ok(Json.toJson(user))
    }
  }

}
