package models

import reactivemongo.bson.BSONObjectID
import com.github.nscala_time.time.Imports.DateTime

case class Meeting(
                    _id: Option[BSONObjectID],
                    date: Option[DateTime],
                    goal: String,
                    organizer: String,
                    attendees: List[String],
                    published: Boolean,
                    override var created: Option[DateTime],
                    override var updated: Option[DateTime]
                    ) extends TemporalModel

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
