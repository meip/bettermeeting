package models

import play.api.libs.json.Json
import reactivemongo.bson.BSONObjectID

import com.github.nscala_time.time.Imports.DateTime

case class MeetingPoint(
                    _id: Option[BSONObjectID] = Some(BSONObjectID.generate),
                    lastEditDate: DateTime,
                    subject: String,
                    lastEditor: User,
                    owner: User,
                    dueDate: DateTime,
                    pointType: PointType
                    ) extends MongoEntity {}

object MeetingPoint {

  import play.modules.reactivemongo.json.BSONFormats._

  implicit val meetingPointFormat = Json.format[MeetingPoint]
}
