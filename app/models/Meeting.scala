package models

import play.api.libs.json.Json
import reactivemongo.bson.BSONObjectID

import com.github.nscala_time.time.Imports.DateTime

case class Meeting(
                    _id: Option[BSONObjectID] = Some(BSONObjectID.generate),
                    date: DateTime,
                    goal: String,
                    organizer: User,
                    attendees: List[User] = List[User]()
                    ) extends MongoEntity {}

object Meeting {

  import play.modules.reactivemongo.json.BSONFormats._

  implicit val meetingFormat = Json.format[Meeting]
}
