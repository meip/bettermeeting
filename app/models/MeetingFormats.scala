package models


import com.github.nscala_time.time.Imports.DateTime
import play.api.libs.functional.syntax._
import play.api.libs.json.{Reads, JsPath, Writes}
import reactivemongo.bson.BSONObjectID
import play.modules.reactivemongo.json.BSONFormats._

object MeetingFormats {
  implicit val datetimeOptionReads = Reads.optionWithNull(Reads.jodaDateReads("dd.MM.yyyy HH:mm"))
  implicit val datetimeOptionWrites = Writes.optionWithNull(Writes.jodaDateWrites("dd.MM.yyyy HH:mm"))
  implicit val datetimeReads = Reads.jodaDateReads("dd.MM.yyyy HH:mm")
  implicit val datetimeWrites = Writes.jodaDateWrites("dd.MM.yyyy HH:mm")

  implicit def meetingWrites: Writes[Meeting] = (
    (JsPath \ "_id").writeNullable[BSONObjectID] and
      (JsPath \ "date").writeNullable[DateTime] and
      (JsPath \ "goal").write[String] and
      (JsPath \ "organizer").write[String] and
      (JsPath \ "attendees").write[List[String]] and
      (JsPath \ "published").write[Boolean] and
      (JsPath \ "created").writeNullable[DateTime] and
      (JsPath \ "updated").writeNullable[DateTime]
    )(meeting => (meeting._id, meeting.date, meeting.goal, meeting.organizer, meeting.attendees, meeting.published, meeting.created, meeting.updated))

  implicit def meetingListWrites: Writes[List[Meeting]] = Writes.list(meetingWrites)

  implicit def meetingReads: Reads[Meeting] = (
    (JsPath \ "_id").readNullable[BSONObjectID].map(_.getOrElse(BSONObjectID.generate)).map(Some(_)) and
      (JsPath \ "date").readNullable[DateTime] and
      (JsPath \ "goal").read[String] and
      (JsPath \ "organizer").read[String] and
      (JsPath \ "attendees").read[List[String]] and
      (JsPath \ "published").read[Boolean] and
      (JsPath \ "created").readNullable[DateTime] and
      (JsPath \ "updated").readNullable[DateTime]
    )((_id, date, goal, organizer, attendees, published, created, updated) => Meeting(_id = _id, date = date, goal = goal, organizer = organizer, attendees = attendees, published = published, created = created, updated = updated))

}