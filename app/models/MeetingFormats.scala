package models


import com.github.nscala_time.time.Imports.DateTime
import extensions.BSONFormatsBM._
import models.DateTimeFormats._
import play.api.libs.functional.syntax._
import play.api.libs.json.{JsPath, Reads, Writes}
import reactivemongo.bson.BSONObjectID

object MeetingFormats {

  implicit def meetingPointWrites: Writes[MeetingPoint] = (
    (JsPath \ "_id").writeNullable[BSONObjectID] and
      (JsPath \ "subject").writeNullable[String] and
      (JsPath \ "lastEditor").writeNullable[String] and
      (JsPath \ "owner").writeNullable[String] and
      (JsPath \ "dueDate").writeNullable[DateTime] and
      (JsPath \ "pointType").write[String] and
      (JsPath \ "dateCompleted").writeNullable[DateTime] and
      (JsPath \ "created").writeNullable[DateTime] and
      (JsPath \ "updated").writeNullable[DateTime]
    )(meetingPoint => (meetingPoint._id, meetingPoint.subject, meetingPoint.lastEditor, meetingPoint.owner, meetingPoint.dueDate, meetingPoint.pointType, meetingPoint.dateCompleted, meetingPoint.created, meetingPoint.updated))

  implicit def meetingPointReads: Reads[MeetingPoint] = (
    (JsPath \ "_id").readNullable[BSONObjectID].map(_.getOrElse(BSONObjectID.generate)).map(Some(_)) and
      (JsPath \ "subject").readNullable[String] and
      (JsPath \ "lastEditor").readNullable[String] and
      (JsPath \ "owner").readNullable[String] and
      (JsPath \ "dueDate").readNullable[DateTime] and
      (JsPath \ "pointType").read[String] and
      (JsPath \ "dateCompleted").readNullable[DateTime] and
      (JsPath \ "created").readNullable[DateTime] and
      (JsPath \ "updated").readNullable[DateTime]
    )((_id, subject, lastEditor, owner, dueDate, pointType, dateCompleted, created, updated) => MeetingPoint(_id = _id, subject = subject, lastEditor = lastEditor, owner = owner, dueDate = dueDate, pointType = pointType, dateCompleted = dateCompleted, created = created, updated = updated))

  implicit def meetingWrites: Writes[Meeting] = (
    (JsPath \ "_id").writeNullable[BSONObjectID] and
      (JsPath \ "date").writeNullable[DateTime] and
      (JsPath \ "goal").write[String] and
      (JsPath \ "organizer").write[String] and
      (JsPath \ "attendees").write[List[String]] and
      (JsPath \ "created").writeNullable[DateTime] and
      (JsPath \ "updated").writeNullable[DateTime] and
      (JsPath \ "meetingPoints").write[List[MeetingPoint]]
    )(meeting => (meeting._id, meeting.date, meeting.goal, meeting.organizer, meeting.attendees, meeting.created, meeting.updated, meeting.meetingPoints))

  implicit def meetingListWrites: Writes[List[Meeting]] = Writes.list(meetingWrites)

  implicit def meetingReads: Reads[Meeting] = (
    (JsPath \ "_id").readNullable[BSONObjectID].map(_.getOrElse(BSONObjectID.generate)).map(Some(_)) and
      (JsPath \ "date").readNullable[DateTime] and
      (JsPath \ "goal").read[String] and
      (JsPath \ "organizer").read[String] and
      (JsPath \ "attendees").read[List[String]] and
      (JsPath \ "created").readNullable[DateTime] and
      (JsPath \ "updated").readNullable[DateTime] and
      (JsPath \ "meetingPoints").readNullable[List[MeetingPoint]]
    )((_id, date, goal, organizer, attendees, created, updated, meetingPoints) => Meeting(_id = _id, date = date, goal = goal, organizer = organizer, attendees = attendees, created = created, updated = updated, meetingPoints = meetingPoints.getOrElse(Nil)))

  implicit object MeetingModelLifeCylce extends TemporalModelLifeCycle[Meeting]
  implicit object MeetingPointModelLifeCylce extends TemporalModelLifeCycle[MeetingPoint]
}