package models

import com.github.nscala_time.time.Imports.DateTime
import play.api.libs.json._

object DateTimeFormats {
  implicit val datetimeReads = Reads.jodaDateReads("dd.MM.yyyy HH:mm")
  implicit val dateTimeWrites: Writes[DateTime] = new Writes[DateTime] {
    override def writes(o: DateTime): JsValue = JsNumber(o.getMillis)
  }
  implicit val datetimeOptionReads = Reads.optionWithNull(Reads.jodaDateReads("dd.MM.yyyy HH:mm"))
  implicit val datetimeOptionWrites = Writes.optionWithNull(dateTimeWrites)
}
