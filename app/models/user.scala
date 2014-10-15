package models

import reactivemongo.bson.BSONObjectID

case class User(
                 _id: Option[BSONObjectID] = Some(BSONObjectID.generate),
                 age: Int,
                 firstName: String,
                 lastName: String,
                 active: Boolean) extends MongoEntity {}

object User {

  import play.api.libs.json.Json
  import play.modules.reactivemongo.json.BSONFormats._

  // Generates Writes and Reads for Feed and User thanks to Json Macros
  implicit val userFormat = Json.format[User]
}