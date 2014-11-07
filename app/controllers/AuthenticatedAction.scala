package controllers

import org.slf4j.{Logger, LoggerFactory}
import play.api.libs.concurrent.Execution.Implicits.defaultContext
import play.api.libs.json.{JsError, Json, Reads}
import play.api.mvc.{BodyParsers, Result, Results}
import reactivemongo.core.commands.LastError
import services.Security

import scala.concurrent.Future

trait AuthenticatedAction extends Security {
  this: Results =>
  private final val logger: Logger = LoggerFactory.getLogger(classOf[AuthenticatedAction])


  def authenticatedAction[A: Reads](action: (A) => Future[Result]) = Authenticated.async(BodyParsers.parse.json) { implicit request =>
    val validationResult = request.body.validate[A]
    validationResult.fold[Future[Result]](
      errors => {
        Future.successful(BadRequest(Json.obj("status" -> "NOT OK", "message" -> JsError.toFlatJson(errors))))
      },
      item => {
        action(item)
      }
    )
  }

  def authenticatedActionWithRecover[A: Reads](action: (A) => Future[LastError], successMessage: String = "Success", failureMessage: String = "Unknown error") = Authenticated.async(BodyParsers.parse.json) { implicit request =>
    val validationResult = request.body.validate[A]
    validationResult.fold[Future[Result]](
      errors => {
        Future.successful(BadRequest(Json.obj("status" -> "NOT OK", "message" -> JsError.toFlatJson(errors))))
      },
      item => {
        action(item).map(_ => Ok(Json.obj("status" -> "OK", "message" -> successMessage))).recover {
          case t: Throwable =>
            logger.error("Modification ERROR", t)
            InternalServerError(failureMessage)
        }
      }
    )
  }
}
