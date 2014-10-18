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

import scala.concurrent.Future

class Meetings extends Controller with MongoController with OIDValidator {

  private final val logger: Logger = LoggerFactory.getLogger(classOf[Meetings])

  def collection: JSONCollection = db.collection[JSONCollection]("meetings")

  def create = Action.async(parse.json) {
    request =>
      request.body.validate[Meeting].map {
        meeting =>
          collection.insert(meeting).map {
            lastError =>
              logger.debug(s"Successfully inserted with LastError: $lastError")
              Created(s"Meeting Created")
          }
      }.getOrElse(Future.successful(BadRequest("invalid json")))
  }

  def update(id: String) = Action.async(parse.json) {
    request =>
      request.body.validate[Meeting].map {
        meeting =>
          validateOID(id) { meetingId =>
            val newData = meeting.copy(_id = Some(meetingId))
            collection.save(newData).map {
              lastError =>
                logger.debug(s"Successfully updated with LastError: $lastError")
                Created(s"Meeting updated")
            }
          }
      }.getOrElse(Future.successful(BadRequest("invalid json")))
  }

  def get(id: String) = Action.async {
    validateOID(id) { meetingId =>
      val cursor: Cursor[Meeting] = collection
        .find(Json.obj("_id" -> meetingId))
        .sort(Json.obj("created" -> -1))
        .cursor[Meeting]

      cursor.collect[List]().map {
        meetings =>
          if (meetings.isEmpty)
            NotFound
          else {
            Ok(Json.toJson(meetings.head))
          }
      }
    }
  }

  def list = Action.async {
    val cursor: Cursor[Meeting] = collection.find(Json.obj()).sort(Json.obj("created" -> -1)).cursor[Meeting]
    val futureMeetingsList: Future[List[Meeting]] = cursor.collect[List]()
    val futureMeetingsJsonArray: Future[JsArray] = futureMeetingsList.map {
      meetings =>
        Json.arr(meetings)
    }

    futureMeetingsJsonArray.map {
      meetings =>
        Ok(meetings(0))
    }
  }

  def delete(id: String) = Action.async {
    validateOID(id) { meetingId =>
      val cursor: Cursor[Meeting] = collection.find(Json.obj("_id" -> meetingId)).sort(Json.obj("created" -> -1)).cursor[Meeting]

      cursor.collect[List]().map {
        meetings =>
          if (meetings.isEmpty)
            NoContent
          else {
            logger.debug(s"The meeting to be removed: ${id}")
            collection.remove(meetings.head)
            NoContent
          }
      }
    }
  }

}
