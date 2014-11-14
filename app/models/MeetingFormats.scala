package models

import com.github.nscala_time.time.Imports.DateTime
import extensions.BSONFormatsBM._
import models.DateTimeFormats._
import play.api.libs.functional.syntax._
import play.api.libs.json.{JsPath, Json, Reads, Writes}
import reactivemongo.bson.BSONObjectID

object MeetingFormats {
  implicit val actionPointWrites = Json.writes[ActionPoint]
  implicit val actionPointReads = Json.reads[ActionPoint]
  implicit val actionPointListFormats: Writes[List[ActionPoint]] = Writes.list(actionPointWrites)

  implicit val decisionWrites = Json.writes[Decision]
  implicit val decisionReads = Json.reads[Decision]
  implicit val decisionListFormats: Writes[List[Decision]] = Writes.list(decisionWrites)

  implicit val voteWrites = Json.writes[Vote]
  implicit val voteReads = Json.reads[Vote]
  implicit val voteListFormats: Writes[List[Vote]] = Writes.list(voteWrites)

  implicit def meetingWrites: Writes[Meeting] = (
    (JsPath \ "_id").writeNullable[BSONObjectID] and
      (JsPath \ "date").writeNullable[DateTime] and
      (JsPath \ "goal").write[String] and
      (JsPath \ "organizer").write[String] and
      (JsPath \ "color").writeNullable[String] and
      (JsPath \ "icsUuid").writeNullable[String] and
      (JsPath \ "attendees").write[List[String]] and
      (JsPath \ "actionPoints").write[List[ActionPoint]] and
      (JsPath \ "decisions").write[List[Decision]] and
      (JsPath \ "votesUp").write[List[Vote]] and
      (JsPath \ "votesDown").write[List[Vote]] and
      (JsPath \ "created").writeNullable[DateTime] and
      (JsPath \ "updated").writeNullable[DateTime]
    )(meeting => (meeting._id, meeting.date, meeting.goal, meeting.organizer, meeting.color, meeting.icsUuid, meeting.attendees, meeting.actionPoints, meeting.decisions, meeting.votesUp, meeting.votesDown, meeting.created, meeting.updated))

  implicit def meetingListWrites: Writes[List[Meeting]] = Writes.list(meetingWrites)

  implicit def meetingReads: Reads[Meeting] = (
    (JsPath \ "_id").readNullable[BSONObjectID] and
      (JsPath \ "date").readNullable[DateTime] and
      (JsPath \ "goal").read[String] and
      (JsPath \ "organizer").read[String] and
      (JsPath \ "color").readNullable[String] and
      (JsPath \ "icsUuid").readNullable[String] and
      (JsPath \ "attendees").read[List[String]] and
      (JsPath \ "actionPoints").readNullable[List[ActionPoint]] and
      (JsPath \ "decisions").readNullable[List[Decision]] and
      (JsPath \ "votesUp").readNullable[List[Vote]] and
      (JsPath \ "votesDown").readNullable[List[Vote]] and
      (JsPath \ "created").readNullable[DateTime] and
      (JsPath \ "updated").readNullable[DateTime]
    )((_id, date, goal, organizer, color, icsUuid, attendees, actionPoints, decisions, votesUp, votesDown, created, updated) => Meeting(_id = _id, date = date, goal = goal, organizer = organizer, color = color, icsUuid = icsUuid, attendees = attendees, actionPoints = actionPoints.getOrElse(Nil), decisions = decisions.getOrElse(Nil), votesUp = votesUp.getOrElse(Nil), votesDown = votesDown.getOrElse(Nil), created = created, updated = updated))

  implicit object MeetingModelLifeCylce extends TemporalModelLifeCycle[Meeting]

  implicit object DecisionModelLifeCylce extends TemporalModelLifeCycle[Decision]

  implicit object ActionPointModelLifeCylce extends TemporalModelLifeCycle[ActionPoint]

  implicit object MeetingVoteLifeCylce extends TemporalModelLifeCycle[Vote]

}