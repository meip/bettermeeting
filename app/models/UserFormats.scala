package models

import play.api.libs.functional.syntax._
import play.api.libs.json.{Reads, JsPath, Writes}
import reactivemongo.bson.BSONObjectID
import play.modules.reactivemongo.json.BSONFormats._

object UserFormats {
  implicit def userWrites: Writes[User] = (
    (JsPath \ "_id").writeNullable[BSONObjectID] and
      (JsPath \ "email").write[String] and
      (JsPath \ "firstName").write[String] and
      (JsPath \ "lastName").write[String] and
      (JsPath \ "active").write[Boolean]
    )(user => (user._id, user.email, user.firstName, user.lastName, user.active))

  implicit def userListWrites: Writes[List[User]] = Writes.list(userWrites)

  implicit def userReads: Reads[User] = (
    (JsPath \ "_id").readNullable[BSONObjectID].map(_.getOrElse(BSONObjectID.generate)).map(Some(_)) and
    (JsPath \ "email").read[String] and
      (JsPath \ "firstName").read[String] and
      (JsPath \ "lastName").read[String] and
      (JsPath \ "active").read[Boolean]
    )((id, email, firstName, lastName, active) => User(_id = id, email = email, firstName = firstName, lastName = lastName, active = active))

}