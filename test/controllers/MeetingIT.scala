package controllers

import java.util.concurrent.TimeUnit

import play.api.libs.json._
import play.api.test.Helpers._
import play.api.test._
import utils.MongoDBTestUtils.withUserInDb

import scala.concurrent._
import scala.concurrent.duration._


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

    "update an existing meeting with new actionPoints should generate IDs for the APs" in withUserInDb { implicit app =>
      val request = FakeRequest.apply(POST, apiUrl).withSession("username" -> "p1meier@hsr.ch").withJsonBody(meetingFull)
      val response = route(request)
      response.isDefined mustEqual true
      val result = Await.result(response.get, timeout)
      result.header.status must equalTo(CREATED)
      val meetingId = (contentAsJson(response.get) \ "message").as[String]

      val meetingWithAp = meetingFull ++ Json.obj("_id" -> Json.obj("$oid" -> meetingId)) ++ Json.obj("actionPoints" -> List(Json.obj(
        "subject" -> "Mach das zum laufen!",
        "editor" -> "r1bader@hsr.ch",
        "owner" -> "r1bader@hsr.ch",
        "dueDate" -> "16.10.2014 16:30",
        "reminderDate" -> "16.11.2014 16:30",
        "reminderType" -> "mail"
      )))
      val requestPut = FakeRequest.apply(PUT, apiUrl + "/" + meetingId).withSession("username" -> "p1meier@hsr.ch").withJsonBody(meetingWithAp)
      val responsePut = route(requestPut)
      val resultPut = Await.result(responsePut.get, timeout)
      val apObjectId = (route(FakeRequest.apply(GET, apiUrl + "/" + meetingId).withSession("username" -> "p1meier@hsr.ch"))).map(future => (contentAsJson(future) \ "actionPoints").as[List[JsObject]]).flatMap(list => list.headOption).map(json => (json \ "_id" \ "$oid").as[String])
      apObjectId.isDefined must equalTo(true)
      resultPut.header.status must equalTo(OK)
    }

  }

}