package models

import play.api.libs.json.Json
import reactivemongo.bson.BSONObjectID

case class PointType(
                         _id: Option[BSONObjectID] = Some(BSONObjectID.generate),
                         typeName: String,
                         hasOwner: Boolean
                         ) extends MongoEntity {}

object PointType {

  import play.modules.reactivemongo.json.BSONFormats._

  implicit val pointTypeFormat = Json.format[PointType]
}
