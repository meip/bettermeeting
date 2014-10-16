package utils

import play.api._
import play.api.libs.concurrent.Execution.Implicits._
import play.api.test.Helpers._
import play.api.test._
import play.modules.reactivemongo.ReactiveMongoPlugin
import play.modules.reactivemongo.json.collection.JSONCollection
import reactivemongo.api.DefaultDB

import scala.concurrent.duration._
import scala.concurrent.{Await, Future}
import scala.language.postfixOps

/**
 * Test utils for running tests with MongoDB
 */
object MongoDBTestUtils {

  /**
   * Run the given block with MongoDB
   */
  def withMongoDb[T](block: Application => T): T = {
    implicit val app = FakeApplication(
      additionalConfiguration = Map("mongodb.uri" -> "mongodb://localhost/bettermeeting-unittests")
    )
    running(app) {
      val db = ReactiveMongoPlugin.db
      try {
        block(app)
      } finally {
        dropAll(db)
        () // Explicitly return unit
      }
    }
  }

  def dropAll(db: DefaultDB) = {
    Await.ready(Future.sequence(Seq(
      db.collection[JSONCollection]("meetings").drop(),
      db.collection[JSONCollection]("users").drop()
    )), 2 seconds)
  }
}