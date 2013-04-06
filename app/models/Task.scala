package models


import scala.slick.driver.BasicDriver.Table
import scala.slick.driver.BasicDriver.Implicit._

import scala.slick.session.Database
import scala.slick.session.Database.threadLocalSession

import play.api.db._
import play.api.Play.current

case class Task(id: Long, label: String)
object TaskT extends Table[Task]("task") {
  def id = column[Long]("id")
  def label = column[String]("label")

  def * = id ~ label <> (Task, Task.unapply _)
}

object Tasks {
  lazy val database = Database.forDataSource(DB.getDataSource())

  def all(): List[Task] = database withSession {
    (for(t <- TaskT) yield t).list
  }

  def create(label: String) { database withSession {
      TaskT.label insert (label)
    }
  }

  def delete(id: Long) { database withSession {
      (for(t <- TaskT if(t.id is id)) yield t).delete
    }
  }
}
