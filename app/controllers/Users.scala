package controllers

import dao.UserDao
import models.User
import models.UserFormats._
import play.Logger
import play.api.libs.concurrent.Execution.Implicits.defaultContext
import play.api.libs.json._
import play.api.mvc._
import reactivemongo.bson.BSONObjectID
import reactivemongo.extensions.json.dsl.JsonDsl
import services.Security

import scala.concurrent.duration.Duration
import scala.concurrent.{Await, Future}

object Users extends Controller with JsonDsl with Security with AuthenticatedAction {

  def list = Authenticated.async {
    implicit request => {
      UserDao.listUsers.map {
        case users => Ok(Json.toJson(users.map(_.userWithoutPassword)))
      }
    }
  }

  def profile = Authenticated { implicit request =>
    Ok(Json.toJson(request.user.copy(password = "")))
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
            Ok(Json.obj(("status" -> "Login erfolgreich"), ("user" -> user))).withSession("username" -> user.email)
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
   * Updates user
   *
   * @return A Ok [[play.api.mvc.Result]] or InternalServerError [[play.api.mvc.Results.Status]]
   */
  def update = authenticatedActionWithRecover[User](
    user => {
      UserDao.updateUser(user)
    }, "User updated", "Unknown error (UPDATE) user"
  )

  /**
   * Updates user's pushToken
   * @return A Ok [[play.api.mvc.Result]] or InternalServerError [[play.api.mvc.Results.Status]]
   */
  def intro = Authenticated.async { implicit request =>
    val showIntroOpt = for {
      json <- request.body.asJson
      showIntro <- (json \ "showIntro").asOpt[Boolean]
    } yield showIntro
    showIntroOpt.map {
      showIntro => UserDao.updateById(request.user._id.get, request.user.copy(showIntro = showIntro)).map(
        _ => Created(Json.obj("status" -> "OK", "message" -> s"ShowIntro pushed $showIntro"))).recover {
        case t: Throwable =>
          Logger.error("CREATE ERROR", t)
          InternalServerError("Unknown error (intro).")
      }
    }.getOrElse(Future(InternalServerError("Unknown error (intro).")))
  }

  /**
   * Updates user's pushToken
   * @return A Ok [[play.api.mvc.Result]] or InternalServerError [[play.api.mvc.Results.Status]]
   */
  def pushToken = Authenticated.async { implicit request =>
    val pushTokenOpt = for {
      json <- request.body.asJson
      pushToken <- (json \ "pushToken").asOpt[String]
    } yield pushToken
    pushTokenOpt.map {
      token => UserDao.updateById(request.user._id.get, request.user.copy(pushToken = Some(token))).map(
        _ => Created(Json.obj("status" -> "OK", "message" -> s"Token pushed $token"))).recover {
        case t: Throwable =>
          Logger.error("CREATE ERROR", t)
          InternalServerError("Unknown error (PushToken).")
      }
    }.getOrElse(Future(InternalServerError("Unknown error (PushToken).")))
  }


  /**
   * Lists the user for E-Mail.
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

  /**
   * Lists all user for regex.
   *
   * @param emailRegex E-Mail regex to lookup
   * @return A Ok [[play.api.mvc.Result]]
   */
  def findByEmailRegex(emailRegex: String) = Authenticated.async {
    UserDao.findByEMailRegex(emailRegex).map {
      case Nil => Ok(Json.toJson(""))
      case userList => Ok(Json.toJson(userList))
    }
  }

}
