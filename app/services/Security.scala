package services

import controllers.routes
import dao.UserDao
import models.User
import play.api.mvc.Results._
import play.api.mvc.Security.AuthenticatedBuilder

import scala.concurrent.Await
import scala.concurrent.duration.Duration

trait Security {
  object Authenticated extends AuthenticatedBuilder[User](
    request => for {
      username <- request.session.get("username")
      user <- Await.result(UserDao.findByEMail(username), Duration.fromNanos(5000000000l))
    } yield user,
    onUnauthorized => Redirect(routes.Users.login())
  )
}