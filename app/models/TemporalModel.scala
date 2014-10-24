package models


import com.github.nscala_time.time.Imports.DateTime
trait TemporalModel {
  var created: Option[DateTime]
  var updated: Option[DateTime]
}
