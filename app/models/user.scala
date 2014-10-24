package models

import reactivemongo.bson.BSONObjectID

case class User(
                 _id: Option[BSONObjectID],
                 email: String,
                 firstName: String,
                 lastName: String,
                 active: Boolean = true)
