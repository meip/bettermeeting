package models


import com.github.nscala_time.time.Imports.DateTime
import play.api.libs.functional.syntax._
import play.api.libs.json.{Reads, JsPath, Writes}
import reactivemongo.bson.BSONObjectID
import play.modules.reactivemongo.json.BSONFormats._

object MeetingFormats {
  implicit val datetimeReads = Reads.optionWithNull(Reads.jodaDateReads("dd.MM.yyyy HH:mm"))
  implicit val datetimeWrites = Writes.optionWithNull(Writes.jodaDateWrites("dd.MM.yyyy HH:mm"))

  implicit def meetingWrites: Writes[Meeting] = (
    (JsPath \ "id").writeNullable[BSONObjectID] and
      (JsPath \ "date").write[Option[DateTime]] and
      (JsPath \ "goal").write[Option[String]] and
      (JsPath \ "organizer").write[Option[String]] and
      (JsPath \ "attendees").write[List[String]]
    )(meeting => (meeting._id, meeting.date, meeting.goal, meeting.organizer, meeting.attendees))

  implicit def meetingListWrites: Writes[List[Meeting]] = Writes.list(meetingWrites)

  implicit def meetingReads: Reads[Meeting] = (
    (JsPath \ "_id").readNullable[BSONObjectID].map(_.getOrElse(BSONObjectID.generate)).map(Some(_)) and
      (JsPath \ "date").read[Option[DateTime]] and
      (JsPath \ "goal").read[Option[String]] and
      (JsPath \ "organizer").read[Option[String]] and
      (JsPath \ "attendees").read[List[String]]
    )((id, date, goal, organizer, attendees) => Meeting(_id = id, date = date, goal = goal, organizer = organizer, attendees = attendees))

}