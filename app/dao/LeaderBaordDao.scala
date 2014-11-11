package dao

import models.LeaderBoard
import models.LeaderBoardFormats._
import play.api.Play.current
import play.api.libs.concurrent.Execution.Implicits.defaultContext
import play.api.libs.json.Json
import play.modules.reactivemongo.ReactiveMongoPlugin
import reactivemongo.extensions.json.dao.JsonDao

object LeaderBaordDao extends JsonDao[LeaderBoard, String](ReactiveMongoPlugin.db, "leaderBoard") with MapReduce[LeaderBoard, String] {
  def leaderBoard() = {
    val mapFunction =
      """
        |function() {
        |  var votesUp = this.votesUp.length;
        |  var votesDown = this.votesDown.length;
        |  var votesDiff = votesUp - votesDown;
        |  emit(this.organizer, votesDiff);
        |};
      """.stripMargin
    val reduceFunction =
      """
        |function(organizer, votes) {
        |    return Array.sum(votes);
        |};
      """.stripMargin
    mapReduce(mapFunction, reduceFunction, "meetings").map{
      case document => {
        findAll(sort = Json.obj("value" -> -1)).map{
          case list => list
        }
      }
    }.flatMap(f => f)
  }

  /**
   * Drops the collection!
   * @return
   */
  def clean = {
    dropSync()
  }

}
