package controllers

import org.specs2.specification.BeforeExample

import scala.concurrent._
import duration._
import org.specs2.mutable._

import play.api.libs.json._
import play.api.test._
import play.api.test.Helpers._
import java.util.concurrent.TimeUnit

import utils.MongoDBTestUtils.withMongoDb
import utils.MongoDBTestUtils.withUserInDb


/**
 * You can mock out a whole application including requests, plugins etc.
 * For more information, consult the wiki.
 */
class MeetingIT extends ApiTest {

  val timeout: FiniteDuration = FiniteDuration(5, TimeUnit.SECONDS)

  val endPoint = "meetings"

  val meetingFull = Json.obj(
    "date" -> "16.12.2014 13:30",
    "goal" -> "Setup the Project test",
    "color" -> "color-1",
    "organizer" -> "peter@organizer.com",
    "attendees" -> List("attendee1@attendee.com", "attendee2@attendee.com")
  )

  val meetingNoGoal = Json.obj(
    "date" -> "16.12.2014 13:30",
    "color" -> "color-1",
    "organizer" -> "peter@organizer.com",
    "attendees" -> List("attendee1@attendee.com", "attendee2@attendee.com")
  )

  "Meetings" should {

    "insert a valid json" in withUserInDb { implicit app =>
      val request = FakeRequest.apply(POST, apiUrl).withSession("username" -> "p1meier@hsr.ch").withJsonBody(meetingFull)
      val response = route(request)
      response.isDefined mustEqual true
      val result = Await.result(response.get, timeout)
      result.header.status must equalTo(CREATED)
    }

    "fail inserting a non valid json, goal is missing" in withUserInDb { implicit app =>
      val request = FakeRequest.apply(POST, apiUrl).withSession("username" -> "p1meier@hsr.ch").withJsonBody(meetingNoGoal)
      val response = route(request)
      response.isDefined mustEqual true
      val result = Await.result(response.get, timeout)
      (contentAsJson(response.get) \ "status").as[String] mustEqual "NOT OK"
      result.header.status mustEqual BAD_REQUEST

    }
    "be find by valid id" in withUserInDb { implicit app =>
      val request = FakeRequest.apply(POST, apiUrl).withSession("username" -> "p1meier@hsr.ch").withJsonBody(meetingFull)
      val response = route(request)
      response.isDefined mustEqual true
      val resultCreated = Await.result(response.get, timeout)
      resultCreated.header.status must equalTo(CREATED)

      val resultList = route(FakeRequest.apply(GET, apiUrl).withSession("username" -> "p1meier@hsr.ch")).get
      status(resultList) must equalTo(OK)
      val theMeetingId = ((contentAsJson(resultList) \\ "_id").head \ "$oid").as[String]

      val theMeeting = route(FakeRequest.apply(GET, apiUrl + "/" + theMeetingId).withSession("username" -> "p1meier@hsr.ch")).get
      status(theMeeting) must equalTo(OK)
      contentAsJson(theMeeting) \ "goal" must beEqualTo(meetingFull \ "goal")
    }

    "returns 400 with invalid id" in withUserInDb { implicit app =>
      val request = FakeRequest.apply(POST, "/api/meetings").withSession("username" -> "p1meier@hsr.ch").withJsonBody(meetingFull)
      val response = route(request)
      response.isDefined mustEqual true
      val resultCreated = Await.result(response.get, timeout)
      resultCreated.header.status must equalTo(CREATED)

      val theBadId = route(FakeRequest.apply(GET, apiUrl + "/" + "NotAValidId").withSession("username" -> "p1meier@hsr.ch")).get
      status(theBadId) mustEqual BAD_REQUEST
    }

  }

}