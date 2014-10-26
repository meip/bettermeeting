package dao

package dao

import extensions.BSONFormatsBM._
import models.{MeetingPoint, Meeting}
import models.MeetingFormats._
import play.api.Play.current
import play.api.libs.concurrent.Execution.Implicits.defaultContext
import play.modules.reactivemongo.ReactiveMongoPlugin
import reactivemongo.bson.BSONObjectID
import reactivemongo.extensions.json.dao.JsonDao
import reactivemongo.extensions.json.dsl.JsonDsl._

object MeetingDao extends JsonDao[Meeting, BSONObjectID](ReactiveMongoPlugin.db, "meetings") {

  /**
   * Lists meetings.
   * Fetchs from database
   *
   * @return [[scala.concurrent.Future]] as a [[List]]
   */
  def listMeetings = MeetingDao.findAll()

  /**
   * Get meeting
   *
   * @return [[scala.concurrent.Future]] as a [[Meeting]]
   */
  def get(id: BSONObjectID) = MeetingDao.findById(id)

  /**
   * Create meeting.
   * Insert a [[Meeting]] object.
   *
   * @param meeting Inserted [[Meeting]] object.
   * @return [[scala.concurrent.Future]] as a [[reactivemongo.core.commands.LastError]]
   */
  def createMeeting(meeting: Meeting) = {
    MeetingDao.insert(meeting)
  }

  /**
   * Push a MeetingPoint to  meeting.
   * Insert a [[Meeting]] object.
   *
   * @param meetingId meetingId to Push MeetingPoint to
   * @param meetingPoint Inserted [[MeetingPoint]] object.
   * @return [[scala.concurrent.Future]] as a [[reactivemongo.core.commands.LastError]]
   */
  def pushMeetingPoint(meetingId: BSONObjectID, meetingPoint: MeetingPoint) = {
    MeetingDao.updateById(meetingId, $push("meetingPoints" -> meetingPoint))
  }

  /**
   * Find meetingPoints for user
   *
   * @return [[scala.concurrent.Future]] as a [[List]]
   */
  def findMeetingPointsForOwner(id: BSONObjectID) = {
    MeetingDao.findAll(selector = "meetingPoints.owner.oid" $eq id.stringify)
  }

  /**
   * Delete the [[Meeting]] object from the database according to id.
   * @param id [[reactivemongo.bson.BSONObjectID]] for the [[Meeting]] object.
   * @return [[scala.concurrent.Future]] as a [[reactivemongo.core.commands.LastError]]
   */
  def deleteMeeting(id: BSONObjectID) = {
    MeetingDao.removeById(id)
  }

}
