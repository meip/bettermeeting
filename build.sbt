import WebKeys._

name := "ibettermeeting"

organization in ThisBuild := "ch.hsr"

version := "0.1-SNAPSHOT"

scalaVersion := "2.11.2"

lazy val root = (project in file(".")).enablePlugins(PlayScala)

resolvers += "Sonatype Snapshots" at "https://oss.sonatype.org/content/repositories/snapshots/"

libraryDependencies ++= Seq(
  "com.google.inject" % "guice" % "3.0",
  "javax.inject" % "javax.inject" % "1",
  "org.reactivemongo" %% "play2-reactivemongo" % "0.10.5.akka23-SNAPSHOT",
  "org.webjars" % "bootstrap" % "3.2.0" exclude("org.webjars", "jquery"),
  "org.webjars" % "angularjs" % "1.3.0" exclude("org.webjars", "jquery"),
  "org.webjars" % "angular-ui-bootstrap" % "0.11.0-3",
  "org.mockito" % "mockito-core" % "1.9.5" % "test",
  "com.github.nscala-time" %% "nscala-time" % "1.4.0")

// Scala Compiler Options
scalacOptions in ThisBuild ++= Seq(
  "-encoding", "UTF-8",
  "-deprecation", // warning and location for usages of deprecated APIs
  "-feature", // warning and location for usages of features that should be imported explicitly
  "-unchecked", // additional warnings where generated code depends on assumptions
  "-Xlint", // recommended additional warnings
  "-Ywarn-adapted-args", // Warn if an argument list is modified to match the receiver
  "-Ywarn-value-discard", // Warn when non-Unit expression results are unused
  "-Ywarn-inaccessible",
  "-Ywarn-dead-code"
)

//
// sbt-web configuration
// https://github.com/sbt/sbt-web
//

