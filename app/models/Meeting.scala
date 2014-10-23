package models

import reactivemongo.bson.BSONObjectID
import com.github.nscala_time.time.Imports.DateTime

case class Meeting(
                    _id: BSONObjectID = BSONObjectID.generate,
                    date: Option[DateTime],
                    goal: Option[String],
                    organizer: Option[String],
                    attendees: List[String],
                    lastEdited: Int,
                    published: Boolean
                    )

case class PointType(
                      _id: BSONObjectID = BSONObjectID.generate,
                      typeName: String,
                      hasOwner: Boolean
                      )

case class MeetingPoint(
                         _id: BSONObjectID = BSONObjectID.generate,
                         lastEditDate: DateTime,
                         subject: Option[String],
                         lastEditor: Option[User],
                         owner: Option[User],
                         dueDate: Option[DateTime],
                         pointType: Option[PointType]
                         )
