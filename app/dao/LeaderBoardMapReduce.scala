package dao

import play.api.Play.current
import play.modules.reactivemongo.ReactiveMongoPlugin
import reactivemongo.bson.{BSONDocument, BSONString}
import reactivemongo.core.commands.RawCommand
import reactivemongo.extensions.json.dao.JsonDao

import scala.concurrent.ExecutionContext.Implicits.global

trait MapReduce[Model, ID] {
  self: JsonDao[Model, ID] =>
  def mapReduce(mapFunction: String, reduceFunction: String, inCollectionName: String) = {
    val mapReduceCommand = BSONDocument(
      "mapreduce" -> BSONString(inCollectionName),
      "map" -> BSONString(mapFunction),
      "reduce" -> BSONString(reduceFunction),
      "out" -> BSONString(collection.name)
    )
    ReactiveMongoPlugin.db.command(RawCommand(mapReduceCommand))
  }
}