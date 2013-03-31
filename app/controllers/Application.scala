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
    Ok(views.html.task_list(Task.all(), taskForm))
  }
  def newTask = TODO
  def deleteTask(id: Long) = TODO
}
