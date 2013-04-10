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
    println(recipeForm.bindFromRequest.get)
    Ok("Everything seems alright!")
  }

  def resetRecipes = Action {
    val count = Recipe.clearAll
    Ok("Removed %d entries\n" format count)
  }

  def listRecipes = Action { implicit request =>
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

    val filledForm = recipeForm.fill(tacos)
    Ok(views.html.recipes_new(filledForm))
  }
}
