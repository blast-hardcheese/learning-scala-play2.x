package models

import play.api.Play.current
import play.api.libs.functional.syntax._

import play.api.data._
import play.api.data.Forms._

import com.novus.salat._
import com.novus.salat.annotations._
import com.novus.salat.dao._
import com.mongodb.casbah.Imports._
import se.radley.plugin.salat._

import mongoContext._

object RecipeForms {
  val ingredientMapping = mapping(
      "name" -> nonEmptyText,
      "quantity" -> optional[String](text),
      "unit" -> optional[String](text),
      "notes" -> optional[String](text)
    )(Ingredient.apply)(Ingredient.unapply)

  val recipeForm = Form(
    mapping(
      "id" -> ignored(new ObjectId),
      "name" -> nonEmptyText,
      "directions" -> list(text),
      "ingredients" -> list(ingredientMapping)
    )(Recipe.apply)(Recipe.unapply)
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
    directions: List[String],
    ingredients: List[Ingredient]
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
}
