package models

import play.api.libs.functional.syntax._
import play.api.libs.json.{Reads, JsPath, Writes}

object UserFormats {
  implicit def userWrites: Writes[User] = (
    (JsPath \ "id").write[String] and
      (JsPath \ "age").write[Int] and
      (JsPath \ "firstname").write[String] and
      (JsPath \ "lastname").write[String] and
      (JsPath \ "active").write[Boolean]
    )(user => (user._id.stringify, user.age, user.firstName, user.lastName, user.active))

  implicit def userListWrites: Writes[List[User]] = Writes.list(userWrites)

  implicit def userReads: Reads[User] = (
    (JsPath \ "age").read[Int] and
      (JsPath \ "firstName").read[String] and
      (JsPath \ "lastName").read[String]
    )((age, firstName, lastName) => User(age = age, firstName = firstName, lastName = lastName))

}