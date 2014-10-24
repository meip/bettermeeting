package controllers

import scala.concurrent._
import duration._
import org.specs2.mutable._

import play.api.libs.json._
import play.api.test._
import play.api.test.Helpers._
import java.util.concurrent.TimeUnit

import utils.MongoDBTestUtils.withMongoDb


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
    "organizer" -> "peter@organizer.com",
    "attendees" -> List("attendee1@attendee.com","attendee1@attendee.com"),
    "published" -> true)

  val meetingNoGoal = Json.obj(
    "date" -> "16.12.2014 13:30",
    "organizer" -> "peter@organizer.com",
    "attendees" -> List("attendee1@attendee.com","attendee1@attendee.com"),
    "published" -> true)


  "Meetings" should {

    "insert a valid json" in withMongoDb { implicit app =>
      val request = FakeRequest.apply(POST, apiUrl).withJsonBody(meetingFull)
      val response = route(request)
      response.isDefined mustEqual true
      val result = Await.result(response.get, timeout)
      result.header.status must equalTo(CREATED)
    }

    "fail inserting a non valid json, goal is missing" in withMongoDb { implicit app =>
      val request = FakeRequest.apply(POST, apiUrl).withJsonBody(meetingNoGoal)
      val response = route(request)
      response.isDefined mustEqual true
      val result = Await.result(response.get, timeout)
      (contentAsJson(response.get) \ "status").as[String] mustEqual "NOT OK"
      result.header.status mustEqual BAD_REQUEST

    }
    "be find by valid id" in withMongoDb { implicit app =>
      val request = FakeRequest.apply(POST, apiUrl).withJsonBody(meetingFull)
      val response = route(request)
      response.isDefined mustEqual true
      val resultCreated = Await.result(response.get, timeout)
      resultCreated.header.status must equalTo(CREATED)

      val resultList = route(FakeRequest.apply(GET, apiUrl)).get
      status(resultList) must equalTo(OK)
      val theMeetingId = ((contentAsJson(resultList) \\ "_id").head \ "$oid").as[String]

      val theMeeting = route(FakeRequest.apply(GET, apiUrl + "/" + theMeetingId)).get
      status(theMeeting) must equalTo(OK)
      contentAsJson(theMeeting) \ "goal" must beEqualTo(meetingFull \ "goal")
    }

    "returns 400 with invalid id" in withMongoDb { implicit app =>
      val request = FakeRequest.apply(POST, "/api/meetings").withJsonBody(meetingFull)
      val response = route(request)
      response.isDefined mustEqual true
      val resultCreated = Await.result(response.get, timeout)
      resultCreated.header.status must equalTo(CREATED)

      val theBadId = route(FakeRequest.apply(GET, apiUrl + "/" + "NotAValidId")).get
      status(theBadId) mustEqual BAD_REQUEST
    }

  }

}