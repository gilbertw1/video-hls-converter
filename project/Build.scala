import sbt._
import Keys._
import com.github.retronym.SbtOneJar

object BuildSettings {
  val buildOrganization = "com.converter"
  val buildVersion      = "0.1"
  val buildScalaVersion = "2.10.4"

  val buildSettings = Defaults.defaultSettings ++ Seq (
    organization := buildOrganization,
    version      := buildVersion,
    scalaVersion := buildScalaVersion
  )
}

object Resolvers {
  val typesafeRepo = "Typesafe Repo" at "http://repo.typesafe.com/typesafe/releases/"
}

object Dependencies {
  val akkaVersion = "2.3.0"

  val akkaActor = "com.typesafe.akka" %% "akka-actor" % akkaVersion
  val akkaSlf4j = "com.typesafe.akka" %% "akka-slf4j" % akkaVersion
  val akkaTestkit = "com.typesafe.akka" %% "akka-testkit" % akkaVersion
  val slf4j = "org.slf4j" % "slf4j-nop" % "1.6.4"
  val scalatest = "org.scalatest" %% "scalatest" % "1.9.1" % "test"

  val akkaDependencies = Seq(akkaActor, akkaSlf4j, akkaTestkit)
  val miscDependencies = Seq(slf4j)
  val testDependencies = Seq(scalatest)
  val allDependencies = akkaDependencies ++ miscDependencies ++ testDependencies
}

object VideoHlsConverter extends Build {
  import Resolvers._
  import BuildSettings._
  import Defaults._

  lazy val northPoller = 
    Project ("video-hls-converter", file("."))
      .settings ( buildSettings : _* )
      .settings ( SbtOneJar.oneJarSettings : _* )
      .settings ( resolvers ++= Seq(typesafeRepo) )
      .settings ( libraryDependencies ++= Dependencies.allDependencies )
      .settings ( scalacOptions ++= Seq("-unchecked", "-deprecation", "-feature") )
}