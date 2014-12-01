package models

import org.mindrot.jbcrypt.BCrypt
import reactivemongo.bson.BSONObjectID

case class User(
                 var _id: Option[BSONObjectID],
                 email: String,
                 password: String,
                 firstName: String,
                 lastName: String,
                 pushToken: Option[String],
                 showIntro: Boolean = true,
                 active: Boolean = true) {
  def checkPassword(password: String): Boolean = BCrypt.checkpw(password, this.password)

  def userWithoutPassword = copy (password = "")
}
