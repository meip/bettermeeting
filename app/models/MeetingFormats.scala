package models


import com.github.nscala_time.time.Imports.DateTime
import play.api.libs.functional.syntax._
import play.api.libs.json.{Reads, JsPath, Writes}

object MeetingFormats {
  implicit val datetimeReads = Reads.optionWithNull(Reads.jodaDateReads("dd.MM.yyyy HH:mm"))
  implicit val datetimeWrites = Writes.optionWithNull(Writes.jodaDateWrites("dd.MM.yyyy HH:mm"))

  implicit def meetingWrites: Writes[Meeting] = (
    (JsPath \ "id").write[String] and
      (JsPath \ "date").write[Option[DateTime]] and
      (JsPath \ "goal").write[Option[String]] and
      (JsPath \ "organizer").write[Option[String]] and
      (JsPath \ "attendees").write[List[String]] and
      (JsPath \ "lastEdited").write[Int] and
      (JsPath \ "published").write[Boolean]
    )(meeting => (meeting._id.stringify, meeting.date, meeting.goal, meeting.organizer, meeting.attendees, meeting.lastEdited, meeting.published))

  implicit def meetingListWrites: Writes[List[Meeting]] = Writes.list(meetingWrites)

  implicit def meetingReads: Reads[Meeting] = (
    (JsPath \ "date").read[Option[DateTime]] and
      (JsPath \ "goal").read[Option[String]] and
      (JsPath \ "organizer").read[Option[String]] and
      (JsPath \ "attendees").read[List[String]] and
      (JsPath \ "lastEdited").read[Int] and
      (JsPath \ "published").read[Boolean]
    )((date, goal, organizer, attendees, lastEdited, published) => Meeting(date = date, goal = goal, organizer = organizer, attendees = attendees, lastEdited = lastEdited, published = published))

}