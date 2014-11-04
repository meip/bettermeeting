package models

import extensions.BSONFormatsBM._
import play.api.libs.functional.syntax._
import play.api.libs.json.{JsPath, Reads, Writes}
import reactivemongo.bson.BSONObjectID

object UserFormats {
  implicit def userWrites: Writes[User] = (
    (JsPath \ "_id").writeNullable[BSONObjectID] and
      (JsPath \ "email").write[String] and
      (JsPath \ "password").write[String] and
      (JsPath \ "firstName").write[String] and
      (JsPath \ "lastName").write[String] and
      (JsPath \ "active").write[Boolean]
    )(user => (user._id, user.email, user.password, user.firstName, user.lastName, user.active))

  implicit def userListWrites: Writes[List[User]] = Writes.list(userWrites)

  implicit def userReads: Reads[User] = (
    (JsPath \ "_id").readNullable[BSONObjectID].map(_.getOrElse(BSONObjectID.generate)).map(Some(_)) and
      (JsPath \ "email").read[String] and
      (JsPath \ "password").read[String] and
      (JsPath \ "firstName").read[String] and
      (JsPath \ "lastName").read[String] and
      (JsPath \ "active").read[Boolean]
    )((id, email, password, firstName, lastName, active) => User(_id = id, email = email, password = password, firstName = firstName, lastName = lastName, active = active))

}