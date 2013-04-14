learning-scala-play2.x
======================

How to use this project:
 - Install the correct version of Play
    (`git grep sbt-plugin project/plugins.sbt` will tell you what the repo is currently at)

 - Install mongodb, making sure the definition in application.conf matches your configuration

 - Type `play` to launch the sbt-based play console

 - Type `run` in the play console to start the server

 - Visit http://localhost:9000/ and have fun!

History of this repository:

 * Started with the Play2.x sample "task list" application:
   http://www.playframework.com/documentation/2.1.1/ScalaTodoList

 * Transitioned from anorm to SLICK
   http://slick.typesafe.com/doc/1.0.0-RC1/gettingstarted.html
   https://github.com/slick/slick-examples/blob/master/src/main/scala/com/typesafe/slick/examples/lifted/FirstExample.scala

 * Started a new set of features, "Recipes", a simple recipe tracker with the following goals:
   - Store recipes for things I've cooked
   - MongoDB backend
   - Provide interface for querying ingredients
     - Based on what recipes I'd like to make in a week, generate a shopping list
     - Filter recipes that include (or don't include) a particular recipe

 * Integrated MongoDB by way of Salat and Casbah
   https://github.com/leon/play-salat
   http://repo.novus.com/salat-presentation/

 * Built out more complex forms
   http://www.playframework.com/documentation/2.1.1/ScalaFormHelpers
   http://www.playframework.com/documentation/2.1.1/ScalaForms
