package models


import com.github.nscala_time.time.Imports.DateTime
trait TemporalModel {
  var created: Option[Long]
  var updated: Option[Long]
}
