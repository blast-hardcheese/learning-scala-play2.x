package models

import org.scalaquery.ql.basic.BasicTable
import org.scalaquery.ql.basic.BasicDriver.Implicit._

import org.scalaquery.session.Database
import org.scalaquery.session.Database.threadLocalSession

import play.api.db._
import play.api.Play.current

object Tasks {
  def all() = TaskSQ.all
  def create(label: String) = TaskSQ.create(label)
  def delete(id: Long) = TaskSQ.delete(id)
}


case class Task(id: Long, label: String)
object TaskT extends BasicTable[Task]("task") {
  def id = column[Long]("id")
  def label = column[String]("label")

  def * = id ~ label <> (Task, Task.unapply _)
}


object TaskAN {
  import anorm._
  import anorm.SqlParser._

  val task = {
    get[Long]("id") ~
    get[String]("label") map {
      case id ~ label => Task(id, label)
    }
  }

  def all(): List[Task] = DB.withConnection { implicit c =>
    SQL("select * from task").as(task *)
  }

  def create(label: String) {
    DB.withConnection { implicit c =>
      SQL("insert into task (label) values ({label})").on(
        "label" -> label
      ).executeUpdate()
    }
  }

  def delete(id: Long) {
    DB.withConnection { implicit c =>
      SQL("delete from task where id = {id}").on(
        'id -> id
      ).executeUpdate()
    }
  }
}

object TaskSQ {
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
