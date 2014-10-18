package controllers

import models.Meeting
import models.MeetingJsonFormat._
import org.slf4j.{Logger, LoggerFactory}
import play.api.libs.concurrent.Execution.Implicits.defaultContext
import play.api.libs.json._
import play.api.mvc._
import play.modules.reactivemongo.MongoController
import play.modules.reactivemongo.json.BSONFormats._
import play.modules.reactivemongo.json.collection.JSONCollection
import reactivemongo.api.Cursor
import reactivemongo.bson.BSONObjectID

import scala.concurrent.Future

trait OIDValidator extends Controller{
  def validateOID(id: String)(f: BSONObjectID => Future[Result]) = {
    BSONObjectID.parse(id).map(f) getOrElse(Future(BadRequest(Json.obj("error" -> "invalidBsonId"))))
  }

}
