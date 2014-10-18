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

  def endPoint(): String = {
    "meetings"
  }

  val userOrganizer = Json.obj(
    "firstName" -> "Jack",
    "lastName" -> "London",
    "age" -> 27,
    "active" -> true)
  val attendees = List(
    Json.obj(
      "firstName" -> "Michael",
      "lastName" -> "SirAttendee",
      "age" -> 29,
      "active" -> true),
    Json.obj(
      "firstName" -> "Thomas",
      "lastName" -> "AnotherAttendee",
      "age" -> 23,
      "active" -> true))

  val meetingFull = Json.obj(
    "date" -> "2014-12-16",
    "goal" -> "Setup the Project test",
    "organizer" -> userOrganizer,
    "attendees" -> attendees)

  val meetingNoGoal = Json.obj(
    "date" -> "2014-12-16",
    "organizer" -> userOrganizer,
    "attendees" -> attendees)


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
      contentAsString(response.get) mustEqual "invalid json"
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