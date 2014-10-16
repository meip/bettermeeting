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
class MeetingIT extends Specification {

  val timeout: FiniteDuration = FiniteDuration(5, TimeUnit.SECONDS)

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

  "Meetings" should {

    "insert a valid json" in withMongoDb { implicit app =>
      val request = FakeRequest.apply(POST, "/meetings").withJsonBody(Json.obj(
        "date" -> "2014-12-16",
        "goal" -> "Setup the Project test",
        "organizer" -> userOrganizer,
        "attendees" -> attendees)
      )
      val response = route(request)
      response.isDefined mustEqual true
      val result = Await.result(response.get, timeout)
      result.header.status must equalTo(CREATED)
    }

    "fail inserting a non valid json, goal is missing" in withMongoDb { implicit app =>
      val request = FakeRequest.apply(POST, "/meetings").withJsonBody(Json.obj(
        "date" -> "2014-12-16",
        "organizer" -> userOrganizer,
        "attendees" -> attendees)
      )
      val response = route(request)
      response.isDefined mustEqual true
      val result = Await.result(response.get, timeout)
      contentAsString(response.get) mustEqual "invalid json"
      result.header.status mustEqual BAD_REQUEST

    }

  }

}