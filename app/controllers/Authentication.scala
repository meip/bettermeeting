package controllers

import dao.UserDao
import models.User
import play.api.mvc.{Action, Controller, RequestHeader, Result}

import scala.concurrent.{Future, Await}
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration.Duration

trait Authentication {
  self:Controller =>

  def AuthenticateMe(f: User => Result) = Action { implicit request =>
    val user = AuthUtils.parseUserFromRequest
    if(user.isEmpty)
      Forbidden("Invalid username or password")
    else {
      f(user.get).withSession(request.session + ("username" -> user.get.email))
    }
  }
  def AuthenticateMeAsync(f: User => Future[Result]) = Action {implicit request =>
    val user = AuthUtils.parseUserFromRequest
    if(user.isEmpty)
      Forbidden("Invalid username or password")
    else {
      Await.result(f(user.get), Duration.fromNanos(5000000000d)).withSession(request.session + ("username" -> user.get.email))
    }
  }
}


object AuthUtils {
  def parseUserFromCookie(implicit request: RequestHeader) = {
    for {
      uuid <- request.session.get("username")
      user <- Await.result(UserDao.findByEMail(uuid), Duration.fromNanos(5000000000d))
    } yield user
  }

  def parseUserFromQueryString(implicit request:RequestHeader) = {
    val query = request.queryString.map { case (k, v) => k -> v.mkString }
    val username = query get ("username")
    val password = query get ("password")
    (username, password) match {
      case (Some(u), Some(p)) => UserDao.findByEMail(u).map{
        case None => None
        case user => user.filter(user => user.checkPassword(p)).map(user => {
          // Some more login logic
          user
        })
      }
    }
  }

  def parseUserFromRequest(implicit request:RequestHeader): Option[User] = {
    lazy val fromCookie = parseUserFromCookie
    lazy val queryString = Await.result(parseUserFromQueryString, Duration.fromNanos(5000000000d))
    parseUserFromCookie orElse queryString
  }

}