package dao

import play.api.Play.current
import play.api.libs.json.Json
import play.api.libs.concurrent.Execution.Implicits.defaultContext
import play.modules.reactivemongo.ReactiveMongoPlugin
import play.modules.reactivemongo.json.BSONFormats._
import reactivemongo.api.indexes.{Index, IndexType}
import reactivemongo.api.indexes.IndexType.Ascending
import reactivemongo.bson.BSONObjectID
import reactivemongo.extensions.json.dao.JsonDao
import models.User
import models.UserFormats._

object UserDao extends JsonDao[User, BSONObjectID](ReactiveMongoPlugin.db, "users") {

  /**
   * Lists users.
   * Fetchs from database
   *
   * @return [[scala.concurrent.Future]] as a [[List]]
   */
  def listUsers = UserDao.findAll()

  /**
   * Finds user for E-Mail.
   *
   * @param email E-Mail attribute for for the [[User]] object.
   * @return [[scala.concurrent.Future]] as a [[Option]]
   */
  def findByEMail(email: String) = {
    UserDao.findOne(Json.obj("email" -> email))
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

  override def autoIndexes = Seq(
    Index(Seq("_id"-> Ascending, "email" -> Ascending)),
    Index(Seq("email" -> IndexType.Ascending), unique = true))

}
