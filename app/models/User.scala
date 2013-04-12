package models

//import play.api.mvc._
import play.api.libs.json._
import play.api.Play.current
import play.api.libs.functional.syntax._

import play.api.data._
import play.api.data.Forms._

//import com.mongodb.casbah.WriteConcern
//import se.radley.plugin.salat.Binders._

import java.util.Date
import com.novus.salat._
import com.novus.salat.annotations._
import com.novus.salat.dao._
import com.mongodb.casbah.Imports._
import se.radley.plugin.salat._

import mongoContext._

case class Address(
  street: String,
  zip: String,
  country: String
)

case class User(
  id: ObjectId = new ObjectId,
  username: String,
  password: String,
  addresses: List[Address] = Nil,
  added: Date = new Date(),
  updated: Option[Date] = None,
  deleted: Option[Date] = None,
  @Key("company_id")company: Option[ObjectId] = None
)

object User extends ModelCompanion[User, ObjectId] {
  val dao = new SalatDAO[User, ObjectId](collection = mongoCollection("users")) {}

  def findOneByUsername(username: String): Option[User] = dao.findOne(MongoDBObject("username" -> username))
  def findAllByUsername(username: String): SalatMongoCursor[User] = dao.find(MongoDBObject("username" -> username))

  def findByStreet(streetname: String): SalatMongoCursor[User] = dao.find(MongoDBObject("addresses.street" -> streetname))
}
