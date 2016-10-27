name := """callliwei"""

version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayScala)
//lazy val root = (project in file(".")).enablePlugins(PlayScala,LauncherJarPlugin)


scalaVersion := "2.11.7"

pipelineStages := Seq(digest)

libraryDependencies ++= Seq(
  cache,
  jdbc,
  evolutions,
  ws,
  "org.scalatestplus.play" %% "scalatestplus-play" % "1.5.1" % Test,
  "com.typesafe.play" %% "anorm" % "2.5.0",
  "ai.x" %% "play-json-extensions" % "0.8.0",
//  "com.typesafe.play" %% "play-slick" % "1.1.1",
//  "com.typesafe.play" %% "play-slick-evolutions" % "1.1.1",
  cache
)


//fuck sbt/activator
//import com.typesafe.sbt.packager.Keys._
//scriptClasspath := Seq("*")
//com.typesafe.sbt.packager.Keys.scriptClasspath := Seq("*")
