package models


import com.github.nscala_time.time.Imports.DateTime
import reactivemongo.bson.BSONObjectID
import reactivemongo.extensions.dao.LifeCycle

trait TemporalModel {
  var created: Option[DateTime]
  var updated: Option[Long]
}

class TemporalModelLifeCycle[T <: TemporalModel] extends LifeCycle[T, BSONObjectID] {
  override def prePersist(model: T): T = {
    if (!model.created.isDefined) model.created = Some(DateTime.now)
    model
  }

  override def postPersist(model: T): Unit = {
  }

  def preRemove(id: BSONObjectID): Unit = {
  }

  def postRemove(id: BSONObjectID): Unit = {
  }

  def ensuredIndexes(): Unit = {
  }
}
