package models

import play.api.Play.current
import play.api.libs.functional.syntax._

import play.api.data._
import play.api.data.Forms._
import play.api.data.format.Formatter

import com.novus.salat._
import com.novus.salat.annotations._
import com.novus.salat.dao._
import com.mongodb.casbah.Imports._
import se.radley.plugin.salat._

import mongoContext._

object RecipeForms {
  implicit def objectIdFormat: Formatter[ObjectId] = new Formatter[ObjectId] {
    def bind(key: String, data: Map[String, String]) = data
      .get(key)
      .map({ value: String => new ObjectId(value) })
      .toRight(Seq(FormError(key, "error.required", Nil)))
    def unbind(key: String, value: ObjectId) = Map(key -> value.toString)
  }

  val ingredientMapping = mapping(
      "name" -> nonEmptyText,
      "quantity" -> optional[String](text),
      "unit" -> optional[String](text),
      "notes" -> optional[String](text)
    )(Ingredient.apply)(Ingredient.unapply)

  val recipeForm = Form(
    mapping(
      "id" -> default(of[ObjectId], new ObjectId),
      "name" -> nonEmptyText,
      "description" -> nonEmptyText,
      "directions" -> list(text),
      "ingredients" -> list(ingredientMapping)
    )(Recipe.apply)(Recipe.unapply)
  )

  val singleRecipeIdForm = Form(
      "id" -> of[ObjectId]
  )
}

case class Ingredient(
    name: String,
    quantity: Option[String] = None,
    unit: Option[String] = None,
    notes: Option[String] = None
  )

case class Recipe(
    id: ObjectId = new ObjectId,
    name: String,
    description: String,
    directions: List[String] = List(),
    ingredients: List[Ingredient] = List()
  )


object Recipe extends ModelCompanion[Recipe, ObjectId] {
  val dao = new SalatDAO[Recipe, ObjectId](collection = mongoCollection("recipes")) {}

  def clearAll = {
    val recipes = Recipe.findAll
    val count = recipes.count
    Recipe.removeByIds(recipes.map({ r: Recipe => r.id }).toList)
    count
  }

  def findByName(name: String): Option[Recipe] = dao.findOne(MongoDBObject("name" -> name))
  def allForIngredient(name: String): SalatMongoCursor[Recipe] = dao.find(MongoDBObject("ingredients.name" -> name))
  def removeId(id: ObjectId) { dao.removeById(id) }
}
