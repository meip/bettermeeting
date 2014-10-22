package models

import reactivemongo.bson.BSONObjectID
import com.github.nscala_time.time.Imports.DateTime

case class Meeting(
                    _id: Option[BSONObjectID] = Some(BSONObjectID.generate)
                    , date: String
                    , goal: String
                    , organizer: String
                    , attendees: List[String]
                    , meetingPoints: List[MeetingPoint]
                    ) extends MongoEntity {}

/*case class PointType(
                      _id: Option[BSONObjectID] = Some(BSONObjectID.generate),
                      typeName: String,
                      hasOwner: Boolean
                      ) extends MongoEntity {}*/

case class MeetingPoint(
                         _id: Option[BSONObjectID] = Some(BSONObjectID.generate),
                         pointType: String,
                         note: String,
                         owner: String,
                         date: String
                         ) extends MongoEntity {}

object MeetingJsonFormat {

  import play.api.libs.json.Json
  import play.modules.reactivemongo.json.BSONFormats._

  implicit val meetingPointFormat = Json.format[MeetingPoint]
  implicit val meetingFormat = Json.format[Meeting]
  //implicit val pointTypeFormat = Json.format[PointType]
}