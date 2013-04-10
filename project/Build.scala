import sbt._
import Keys._
import play.Project._

object ApplicationBuild extends Build {

    val appName         = "play2mini_learning"
    val appVersion      = "1.0-SNAPSHOT"

    val appDependencies = Seq(
      // Add your project dependencies here,
      "play" %% "play-jdbc" % "2.1-SNAPSHOT",
      "se.radley" %% "play-plugins-salat" % "1.2",
      "com.typesafe.slick" % "slick_2.10" % "1.0.0",
      "org.xerial" % "sqlite-jdbc" % "3.7.2"
    )

    val main = play.Project(appName, appVersion, appDependencies).settings(
      // Add your own project settings here
      routesImport += "se.radley.plugin.salat.Binders._",
      templatesImport += "org.bson.types.ObjectId",
      resolvers += Resolver.sonatypeRepo("snapshots")
    )

}
