package controllers

import play.api._
import play.api.mvc._

import models._

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
}
