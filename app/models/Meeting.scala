package models

import reactivemongo.bson.BSONObjectID
import com.github.nscala_time.time.Imports.DateTime

case class Meeting(
                    _id: Option[BSONObjectID] = Some(BSONObjectID.generate),
                    //date: DateTime,
                    goal: String,
                    organizer: String,
                    attendees: List[String]
                    ) extends MongoEntity {}

case class PointType(
                      _id: Option[BSONObjectID] = Some(BSONObjectID.generate),
                      typeName: String,
                      hasOwner: Boolean
                      ) extends MongoEntity {}

case class MeetingPoint(
                         _id: Option[BSONObjectID] = Some(BSONObjectID.generate),
                         lastEditDate: DateTime,
                         subject: String,
                         lastEditor: User,
                         owner: User,
                         dueDate: DateTime,
                         pointType: PointType
                         ) extends MongoEntity {}

object MeetingJsonFormat {

  import play.api.libs.json.Json
  import play.modules.reactivemongo.json.BSONFormats._

  implicit val meetingFormat = Json.format[Meeting]
  implicit val pointTypeFormat = Json.format[PointType]
  implicit val meetingPointFormat = Json.format[MeetingPoint]
}