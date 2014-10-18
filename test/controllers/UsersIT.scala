package controllers

import utils.MongoDBTestUtils._

import scala.concurrent._
import duration._
import org.specs2.mutable._

import play.api.libs.json._
import play.api.test._
import play.api.test.Helpers._
import java.util.concurrent.TimeUnit


/**
 * You can mock out a whole application including requests, plugins etc.
 * For more information, consult the wiki.
 */
class UsersIT extends ApiTest {

  val timeout: FiniteDuration = FiniteDuration(5, TimeUnit.SECONDS)

  def endPoint(): String = {
    "user"
  }

  "Users" should {

    "insert a valid json" in withMongoDb { implicit app =>
      val request = FakeRequest.apply(POST, apiUrl).withJsonBody(Json.obj(
        "firstName" -> "Jack",
        "lastName" -> "London",
        "age" -> 27,
        "active" -> true))
      val response = route(request)
      response.isDefined mustEqual true
      val result = Await.result(response.get, timeout)
      result.header.status must equalTo(CREATED)
    }

    "fail inserting a non valid json" in withMongoDb { implicit app =>
      val request = FakeRequest.apply(POST, apiUrl).withJsonBody(Json.obj(
        "firstName" -> 98,
        "lastName" -> "London",
        "age" -> 27))
      val response = route(request)
      response.isDefined mustEqual true
      val result = Await.result(response.get, timeout)
      contentAsString(response.get) mustEqual "invalid json"
      result.header.status mustEqual BAD_REQUEST
    }

  }

}