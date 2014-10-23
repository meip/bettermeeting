package models

import play.api.libs.functional.syntax._
import play.api.libs.json.{Reads, JsPath, Writes}

object UserFormats {
  implicit def userWrites: Writes[User] = (
    (JsPath \ "id").write[String] and
      (JsPath \ "email").write[String] and
      (JsPath \ "firstName").write[String] and
      (JsPath \ "lastName").write[String] and
      (JsPath \ "active").write[Boolean]
    )(user => (user._id.stringify, user.email, user.firstName, user.lastName, user.active))

  implicit def userListWrites: Writes[List[User]] = Writes.list(userWrites)

  implicit def userReads: Reads[User] = (
    (JsPath \ "email").read[String] and
      (JsPath \ "firstName").read[String] and
      (JsPath \ "lastName").read[String] and
      (JsPath \ "active").read[Boolean]
    )((email, firstName, lastName, active) => User(email = email, firstName = firstName, lastName = lastName, active = active))

}