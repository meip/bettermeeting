package models

import play.api.libs.json.{Json, Writes}

case class LeaderBoard(
                        var _id: Option[String],
                        value: Double)


object LeaderBoardFormats {
  implicit val leaderBoardWrites = Json.writes[LeaderBoard]
  implicit val leaderBoardReads = Json.reads[LeaderBoard]

  implicit def leaderBoardListWrites: Writes[List[LeaderBoard]] = Writes.list(leaderBoardWrites)
}


case class LeaderBoardFormatted(
                                 name: String,
                                 rank: Int,
                                 points: Int,
                                 selft: Boolean
                                 )
object LeaderBoardFormattedFormats {
  implicit val leaderBoardFormattedWrites = Json.writes[LeaderBoardFormatted]

  implicit def leaderBoardFormattedListWrites: Writes[List[LeaderBoardFormatted]] = Writes.list(leaderBoardFormattedWrites)
}