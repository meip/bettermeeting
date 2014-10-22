package dao

import play.api.Play.current
import play.api.libs.json.Json
import play.api.libs.concurrent.Execution.Implicits.defaultContext
import play.modules.reactivemongo.ReactiveMongoPlugin
import play.modules.reactivemongo.json.BSONFormats._
import reactivemongo.bson.BSONObjectID
import reactivemongo.extensions.json.dao.JsonDao
import models.User
import models.User._

import reactivemongo.extensions.json.dsl.JsonDsl._

object UserDao extends JsonDao[User, BSONObjectID](ReactiveMongoPlugin.db, "users") {

  /**
   * Lists users.
   * Fetchs from database
   *
   * @return [[scala.concurrent.Future]] as a [[List]]
   */
  def listUsers = UserDao.findAll()

  /**
   * Lists users for firstname.
   * Fetchs from database according to firstname.
   *
   * @param firstName Firstname attribute for for the [[User]] object.
   * @return [[scala.concurrent.Future]] as a [[List]]
   */
  def listUsersByFirstname(firstName: String) = {
    UserDao.findAll(selector = "firstName" $eq firstName, sort = "_id" $eq 1)
  }

  /**
   * Create user.
   * Insert a [[User]] object.
   *
   * @param user Inserted [[User]] object.
   * @return [[scala.concurrent.Future]] as a [[reactivemongo.core.commands.LastError]]
   */
  def createUser(user: User) = {
    UserDao.insert(user)
  }

  /**
   * Delete the [[User]] object from the database according to id.
   * @param id [[reactivemongo.bson.BSONObjectID]] for the [[User]] object.
   * @return [[scala.concurrent.Future]] as a [[reactivemongo.core.commands.LastError]]
   */
  def deleteUser(id: BSONObjectID) = {
    UserDao.removeById(id)
  }

}
