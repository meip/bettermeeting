package models

import reactivemongo.bson.BSONObjectID

case class User(
                 _id: BSONObjectID = BSONObjectID.generate,
                 email: String,
                 firstName: String,
                 lastName: String,
                 active: Boolean = true)
