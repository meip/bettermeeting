package models

import reactivemongo.bson.BSONObjectID
import com.github.nscala_time.time.Imports.DateTime

case class Meeting(
                    var _id: Option[BSONObjectID],
                    date: Option[DateTime],
                    goal: String,
                    organizer: String,
                    color: Option[String],
                    status: Option[String],
                    icsUuid: Option[String],
                    attendees: List[String],
                    decisions: List[Decision],
                    actionPoints: List[ActionPoint],
                    votesUp: List[Vote],
                    votesDown: List[Vote] ,
                    override var created: Option[DateTime],
                    override var updated: Option[DateTime]
                    ) extends TemporalModel

case class Decision(
                     var _id: Option[BSONObjectID],
                     subject: Option[String],
                     editor: Option[String],
                     override var created: Option[DateTime],
                     override var updated: Option[DateTime]
                     ) extends TemporalModel

case class ActionPoint(
                        var _id: Option[BSONObjectID],
                        subject: Option[String],
                        editor: Option[String],
                        owner: Option[String],
                        status: Option[String],
                        dueDate: Option[DateTime],
                        reminderDate: Option[DateTime],
                        reminderType: Option[String],
                        override var created: Option[DateTime],
                        override var updated: Option[DateTime]
                        ) extends TemporalModel

case class Vote(
                 email: String,
                 override var created: Option[DateTime],
                 override var updated: Option[DateTime]
                 ) extends TemporalModel