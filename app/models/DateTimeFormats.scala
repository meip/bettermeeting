package models

import play.api.libs.json.{Reads, Writes}

object DateTimeFormats {
  implicit val datetimeOptionReads = Reads.optionWithNull(Reads.jodaDateReads("dd.MM.yyyy HH:mm"))
  implicit val datetimeOptionWrites = Writes.optionWithNull(Writes.jodaDateWrites("dd.MM.yyyy HH:mm"))
  implicit val datetimeReads = Reads.jodaDateReads("dd.MM.yyyy HH:mm")
  implicit val datetimeWrites = Writes.jodaDateWrites("dd.MM.yyyy HH:mm")
}
