package models

import reactivemongo.bson.BSONObjectID

case class User(
                 _id: Option[BSONObjectID],
                 email: String,
                 password: String,
                 firstName: String,
                 lastName: String,
                 active: Boolean = true) {

  def checkPassword(password: String): Boolean = this.password == password
}
