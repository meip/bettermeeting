package controllers

import dao.UserDao
import dao.dao.MeetingDao
import models.{ActionPoint, Decision, Meeting, User}
import org.joda.time.DateTime
import org.joda.time.format.DateTimeFormat
import play.Logger
import play.api.libs.json.Json
import play.api.mvc.{Action, Controller}
import reactivemongo.extensions.json.dsl.JsonDsl

import scala.concurrent.Await
import scala.concurrent.duration.Duration

object TestData extends Controller with JsonDsl {

  val testUserList = List(
    User(None, "p1meier@hsr.ch", "p1meier", "Philipp", "Meier", true),
    User(None, "r1bader@hsr.ch", "r1bader", "Robin", "Bader", true)
  )

  val testMeetingList = List(
    Meeting(
      _id = None,
      date = makeDate("22.12.2014 15:00"),
      goal = "Decision about Prototype #1",
      organizer = "p1meier@hsr.ch",
      color = Some("color-2"),
      attendees = List("r1bader@hsr.ch"),
      decisions = Nil,
      actionPoints = Nil,
      votesUp = Nil,
      votesDown = Nil,
      created = Some(DateTime.now),
      updated = Some(DateTime.now)
    ),
    Meeting(
      _id = None,
      date = makeDate("10.11.2014 13:00"),
      goal = "Build Summary for Project XY",
      organizer = "p1meier@hsr.ch",
      color = Some("color-2"),
      attendees = List("r1bader@hsr.ch"),
      decisions = List(
        Decision(
          _id = None,
          subject = Some("The Project was a great success"),
          editor = Some("p1meier@hsr.ch"),
          created = makeDate("10.11.2014 13:33"),
          updated = makeDate("10.11.2014 13:33")
        )
      ),
      actionPoints = List(
        ActionPoint(
          _id = None,
          subject = Some("Close all project reports"),
          editor = Some("p1meier@hsr.ch"),
          owner = Some("p1meier@hsr.ch"),
          status = Some("closed"),
          dueDate = makeDate("14.11.2014 16:00"),
          reminderDate = makeDate("14.11.2014 15:00"),
          reminderType = Some("push"),
          created = makeDate("10.11.2014 13:29"),
          updated = makeDate("14.11.2014 13:29")
        ),
        ActionPoint(
          _id = None,
          subject = Some("Inform the client about the summary."),
          editor = Some("p1meier@hsr.ch"),
          owner = Some("r1bader@hsr.ch"),
          status = Some("open"),
          dueDate = makeDate("12.11.2014 16:00"),
          reminderDate = makeDate("12.11.2014 15:00"),
          reminderType = Some("push"),
          created = makeDate("10.11.2014 13:29"),
          updated = makeDate("10.11.2014 13:29")
        )
      ),
      votesUp = List(
        "p1meier@hsr.ch",
        "r1bader@hsr.ch"
      ),
      votesDown = Nil,
      created = makeDate("08.11.2014 09:10"),
      updated = makeDate("08.11.2014 09:10")
    )
  )

  def createUsers() = Action {
    testUserList.map(user => UserDao.createUser(user)).map(f => Await.result(f, Duration.fromNanos(500000000000l)))
    Ok(Json.toJson("test users created"))
  }

  def createMeetings() = Action {
    testMeetingList.map(meeting => MeetingDao.createMeeting(meeting)).map(f => Await.result(f, Duration.fromNanos(500000000000l)))
    Ok(Json.toJson("test meetings created"))
  }

  def init = Action {
    testUserList.map(user => UserDao.createUser(user)).map(f => Await.result(f, Duration.fromNanos(500000000000l)))
    testMeetingList.map(meeting => MeetingDao.createMeeting(meeting)).map(f => Await.result(f, Duration.fromNanos(500000000000l)))
    Ok(Json.toJson("test users meetings created"))
  }

  def clean = Action {
    MeetingDao.clean
    UserDao.clean
    Ok(Json.toJson("database cleaned up"))
  }

  def cleanInit = Action {
    Await.result(UserDao.clean, Duration.fromNanos(500000000000l))
    Await.result(MeetingDao.clean, Duration.fromNanos(500000000000l))

    testUserList.map(user => UserDao.createUser(user)).map(f => Await.result(f, Duration.fromNanos(500000000000l)))
    testMeetingList.map(meeting => MeetingDao.createMeeting(meeting)).map(f => Await.result(f, Duration.fromNanos(500000000000l)))

    Ok(Json.toJson("database cleaned up and initialized again"))
  }

  private def makeDate(s: String): Option[DateTime] = scala.util.control.Exception.allCatch[DateTime] opt (DateTime.parse(s, DateTimeFormat.forPattern("dd.MM.yyyy HH:mm")))

}