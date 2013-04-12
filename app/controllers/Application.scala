package controllers

import play.api._
import play.api.mvc._

import models._
import models.RecipeForms._

import play.api.data._
import play.api.data.Forms._

object Application extends Controller {

  val taskForm = Form(
    "label" -> nonEmptyText
  )

  def index = Action {
    Ok(views.html.index("Your new application is ready."))
  }

  def hello(name: String) = Action {
    Ok(views.html.hello(name))
  }

// Tasks
  def tasks = Action {
    Ok(views.html.task_list(Tasks.all(), taskForm))
  }

  def newTask = Action { implicit request =>
    taskForm.bindFromRequest.fold(
      errors => BadRequest(views.html.task_list(Tasks.all(), errors)),
      label => {
        Tasks.create(label)
        Redirect(routes.Application.tasks)
      }
    )
  }

  def deleteTask(id: Long) = Action {
    Tasks.delete(id)
    Redirect(routes.Application.tasks)
  }

// Recipe

  def newRecipe = Action { implicit request =>
    recipeForm.bindFromRequest.fold(
      errors => BadRequest(views.html.recipes_new(errors)),
      recipe => {
        Recipe.save(recipe)
        Redirect(routes.Application.listRecipes)
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

  def editRecipe(name: String) = Action { implicit request =>
    val recipe = Recipe.findByName(name) match {
      case Some(r: Recipe) => r
      case None => Recipe(name=name, directions=List(), ingredients=List())
    }

    def qsInt(name: String, default: Int): Int = {
      request.queryString.get(name).getOrElse(Seq[String]()) match {
        case (x: String) :: Nil => x.toInt
        case xs :: (x: String) :: Nil => x.toInt
        case z => default
      }
    }

    val steps = qsInt("steps", 2)
    val ingredients = qsInt("ingredients", 2)

    val filledForm = recipeForm.fill(recipe)
    Ok(views.html.recipes_new(filledForm, steps = steps, ingredients = ingredients))
  }

  def listRecipes = Action { implicit request =>
    Ok("Recipes: " + Recipe.findAll.toList)
  }
}
