package models

import reactivemongo.bson.BSONObjectID
import com.github.nscala_time.time.Imports.DateTime

case class Meeting(
                    _id: Option[BSONObjectID],
                    date: Option[DateTime],
                    goal: String,
                    organizer: String,
                    color: Option[String],
                    attendees: List[String],
                    override var created: Option[DateTime],
                    override var updated: Option[DateTime],
                    meetingPoints: List[MeetingPoint]
                    ) extends TemporalModel


case class MeetingPoint(
                         _id: Option[BSONObjectID],
                         subject: Option[String],
                         lastEditor: Option[String],
                         owner: Option[String],
                         dueDate: Option[DateTime],
                         pointType: String,
                         dateCompleted: Option[DateTime],
                         override var created: Option[DateTime],
                         override var updated: Option[DateTime]
                         ) extends TemporalModel

case class PointType(
                      _id: BSONObjectID = BSONObjectID.generate,
                      typeName: String
                      )