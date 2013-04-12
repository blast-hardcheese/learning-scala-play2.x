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

  def testUser {
    println(User.findAllByUsername("leon").next())
    User.removeByIds(
        (User.findAllByUsername("leon") map({ u:User => u.id })).toList
      )

    val testUser1 = User(
      username = "leon",
      password = "1234",
      addresses = List(Address("Örebro", "123 45", "Sweden"), Address("Elmer", "91601", "USA"))
    )

    val testUser2 = User(
      username = "devon",
      password = "1234",
      addresses = List(Address("Elmer", "91601", "USA"))
    )

    println(testUser1)
    println(testUser2)

    User.save(testUser1)
    User.save(testUser2)

    println("Users by username: " + User.findAllByUsername("leon").map({ u: User => u.username }).toList)
    println("Users by street: " + User.findByStreet("Elmer").map({ u: User => u.username }).toList)

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
