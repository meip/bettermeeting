package dao

import extensions.BSONFormatsBM._
import models.MeetingFormats._
import models.{ActionPoint, Meeting, Vote}
import play.api.Play.current
import play.api.libs.concurrent.Execution.Implicits.defaultContext
import play.api.libs.json.Json
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
    meeting.actionPoints.foreach(actionPoint => if (actionPoint._id.isEmpty) actionPoint._id = Some(BSONObjectID.generate))
    meeting.decisions.foreach(decision => if (decision._id.isEmpty) decision._id = Some(BSONObjectID.generate))
    meeting.status = Some("new")
    MeetingDao.insert(meeting)
  }

  /**
   * Push a ActionPoint to  meeting.
   * Insert a [[ActionPoint]] object.
   *
   * @param meetingId meetingId to Push MeetingPoint to
   * @param actionPoint Inserted [[ActionPoint]] object.
   * @return [[scala.concurrent.Future]] as a [[reactivemongo.core.commands.LastError]]
   */
  def pushActionPoint(meetingId: BSONObjectID, actionPoint: ActionPoint) = {
    MeetingDao.updateById(meetingId, $push("actionPoints" -> actionPoint))
  }

  /**
   * Vote a meeting goal.
   *
   * @param meetingId meetingId to Push Vote to
   * @param vote [[Vote]] object.
   * @return [[scala.concurrent.Future]] as a [[reactivemongo.core.commands.LastError]]
   */
  def voteOnGoal(meetingId: BSONObjectID, vote: Vote) = for {
    remove <- MeetingDao.updateById(meetingId, $pull("votesOnGoal" -> Json.obj("email" -> vote.email)))
    setVote <- MeetingDao.updateById(meetingId, $push("votesOnGoal" -> vote))
  } yield setVote
  
  /**
   * Vote a meeting engagement.
   *
   * @param meetingId meetingId to Push Vote to
   * @param vote [[Vote]] object.
   * @return [[scala.concurrent.Future]] as a [[reactivemongo.core.commands.LastError]]
   */
  def voteOnEfficiency(meetingId: BSONObjectID, vote: Vote) = for {
    remove <- MeetingDao.updateById(meetingId, $pull("votesOnEfficiency" -> Json.obj("email" -> vote.email)))
    setVote <- MeetingDao.updateById(meetingId, $push("votesOnEfficiency" -> vote))
  } yield setVote

  /**
   * Update an ActionPoint
   * Insert a [[ActionPoint]] object.
   *
   * @param actionPointId actionPointId to update
   * @param actionPoint Updated [[ActionPoint]] object.
   * @return [[scala.concurrent.Future]] as a [[reactivemongo.core.commands.LastError]]
   */
  def updateActionPointById(actionPointId: BSONObjectID, actionPoint: ActionPoint) = {
    MeetingDao.update(Json.obj("actionPoints._id" -> actionPointId), $set("actionPoints.$" -> actionPoint))
  }

  /**
   * Find open actionPoints for user
   *
   * @return [[scala.concurrent.Future]] as a [[List]]
   */
  def findDoneActionPointsForOwner(id: String) = {
    MeetingDao.findAll(selector = "actionPoints.owner" $eq id).map {
      case meeting => meeting.flatMap(_.actionPoints).filter(ap => (ap.owner.equals(Some(id)) && ap.status.equals(Some("done"))))
    }
  }

  /**
   * Find open actionPoints for user
   *
   * @return [[scala.concurrent.Future]] as a [[List]]
   */
  def findOpenActionPointsForOwner(id: String) = {
    MeetingDao.findAll(selector = "actionPoints.owner" $eq id).map {
      case meeting => meeting.flatMap(_.actionPoints).filter(ap => (ap.owner.equals(Some(id)) && (ap.status.equals(Some("open")) || ap.status.isEmpty)))
    }
  }

  /**
   * Find actionPoints for user
   *
   * @return [[scala.concurrent.Future]] as a [[List]]
   */
  def findMeetingsForUser(id: String) = {
    MeetingDao.findAll(selector = $or(("organizer" $eq id), ("attendees" $in id)))
  }

  /**
   * Find actionPoints for user where he is an organizer
   *
   * @return [[scala.concurrent.Future]] as a [[List]]
   */
  def findMeetingsForOrganizer(id: String) = {
    MeetingDao.findAll(selector = ("organizer" $eq id))
  }

  /**
   * Find meetings for for user where he is an attendee
   *
   * @return [[scala.concurrent.Future]] as a [[List]]
   */
  def findMeetingsForAttendee(id: String) = {
    MeetingDao.findAll(selector = Json.obj("attendees" -> id))
  }

  /**
   * Find meeting for IcsUuid
   *
   * @return [[scala.concurrent.Future]] as a [[List]]
   */
  def findMeetingsForIcsUuid(icsUuid: String) = {
    MeetingDao.findAll(selector = ("icsUuid" $eq icsUuid))
  }

  /**
   * Delete the [[Meeting]] object from the database according to id.
   * @param id [[reactivemongo.bson.BSONObjectID]] for the [[Meeting]] object.
   * @return [[scala.concurrent.Future]] as a [[reactivemongo.core.commands.LastError]]
   */
  def deleteMeeting(id: BSONObjectID) = {
    MeetingDao.removeById(id)
  }

  /**
   * Delete the [[ActionPoint]] object from the database according to id.
   * @param id [[reactivemongo.bson.BSONObjectID]] for the [[ActionPoint]] object.
   * @return [[scala.concurrent.Future]] as a [[reactivemongo.core.commands.LastError]]
   */
  def deleteActionPoint(id: BSONObjectID) = {
    MeetingDao.update(Json.obj(), $pull("actionPoints" -> Json.obj("_id" -> id)), multi = true)
  }

  /**
   * Drops the collection!
   * @return
   */
  def clean = {
    dropSync()
  }

}
