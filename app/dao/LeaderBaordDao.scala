package dao

import models.LeaderBoard
import models.LeaderBoardFormats._
import play.api.Play.current
import play.api.libs.concurrent.Execution.Implicits.defaultContext
import play.api.libs.json.Json
import play.modules.reactivemongo.ReactiveMongoPlugin
import reactivemongo.extensions.json.dao.JsonDao

class LeaderBaordDao(leaderBoardType: String) extends JsonDao[LeaderBoard, String](ReactiveMongoPlugin.db, "leaderBoard_" + leaderBoardType) with MapReduce[LeaderBoard, String] {
  def mapReduceBoard() = {
    val mapFunction =
      """
        |function() {
        |  var sum = 0;
        |  this.""".stripMargin + leaderBoardType + """.forEach(function(vote) {
        |    if (vote.voteValue > 0) {
        |      sum += vote.voteValue;
        |    }
        |  });
        |  emit(this.organizer, sum);
        |};
      """.stripMargin
    val reduceFunction =
      """
        |function(organizer, vote) {
        |    return Array.sum(vote);
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
