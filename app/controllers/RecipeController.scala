package controllers

import play.api._
import play.api.mvc._

import models._
import models.RecipeForms._

import play.api.data._
import play.api.data.Forms._

object MyHelpers {
    def urlDecode(string: String)(implicit codec: play.api.mvc.Codec): String =
      java.net.URLDecoder.decode(string, codec.charset)
}

import MyHelpers._

object RecipeController extends Controller {
  def listRecipes = Action {
    Ok(views.html.recipes_list(Recipe.findAll.toList))
  }

  def newRecipe = Action {
    Ok(views.html.recipes_new(recipeForm))
  }

  def showRecipe(encodedName: String) = Action {
    val name = urlDecode(encodedName)

    Recipe.findByName(name) match {
      case Some(r: Recipe) => Ok(views.html.recipes_show(r))
      case None => Redirect(routes.RecipeController.newRecipe)
    }
  }

  def editRecipe(encodedName: String) = Action { implicit request =>
    val name = urlDecode(encodedName)

    def qsInt(name: String, default: Int): Int = {
      request.queryString.get(name).getOrElse(Seq[String]()) match {
        case (x: String) :: Nil => x.toInt
        case xs :: (x: String) :: Nil => x.toInt
        case z => default
      }
    }

    val steps = qsInt("steps", 2)
    val ingredients = qsInt("ingredients", 2)

    Recipe.findByName(name) match {
      case Some(r: Recipe) => Ok(views.html.recipes_edit(recipeForm.fill(r), steps = steps, ingredients = ingredients))
      case None => Redirect(routes.RecipeController.newRecipe)
    }
  }

  def saveRecipe = Action { implicit request =>
    recipeForm.bindFromRequest.fold(
      errors => BadRequest(views.html.recipes_new(errors)),
      recipe => {
        Recipe.save(recipe)
        Redirect(routes.RecipeController.listRecipes)
      }
    )
  }

  def deleteRecipe = Action { implicit request =>
    singleRecipeIdForm.bindFromRequest.fold(
      errors => Redirect(routes.RecipeController.listRecipes),
      recipeId => {
        Recipe.removeId(recipeId)
        Redirect(routes.RecipeController.listRecipes)
      }
    )
  }

  def resetRecipes = Action {
    val count = Recipe.clearAll
    Ok("Removed %d entries\n" format count)
  }

  def createRecipes {
    println("sup")

    val cheesecrackers = Recipe(
      name="Cheese Crackers",
      description="Delicious cheese crackers, gluten free! Keto friendly!",
      directions=List(
        "Turn on stove",
        "Oil pan",
        "Pour some cheese in pan",
        "Mix in garlic",
        "Move oil around in pan to ensure cheese doesn't burn",
        "When cheese starts to bubble, drain oil",
        "Put in refrigerator to cool",
        "When cool, sandwich cheese between paper towels to draw away the oil"
      ),
      ingredients=List(
        Ingredient(
          "cheese",
          notes=Some("Just fill the bottom of whatever pan you're using")
        )
      )
    )
    Recipe.save(cheesecrackers)

    val tacos = Recipe(
      name="Tacos",
      description="Tacos, pretty tasty.",
      directions=List(
        "Cook meat",
        "Put in shell",
        "Put lettuce in the shell",
        "Put cheese in the shell",
        "Put salsa in the shell"
      ),
      ingredients=List(
        Ingredient("taco shell"),
        Ingredient("meat"),
        Ingredient("lettuce"),
        Ingredient("cheese"),
        Ingredient("salsa")
      )
    )
    Recipe.save(tacos)

    println("Recipes: " + Recipe.findAll.toList)

    println("Cheese recipes: " + Recipe.allForIngredient("cheese").count)
  }
}
