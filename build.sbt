organization := "ru.org.codingteam"

name := "FreqParser"

version := "1.0-SNAPSHOT"

scalaVersion := "2.11.1"

scalacOptions ++= Seq(
  "-deprecation",
  "-feature",
  "-unchecked"
)

mainClass in (Compile, run) := Some("ru.org.codingteam.freqparser.Main")

libraryDependencies ++= Seq(
  "com.typesafe.slick" %% "slick" % "2.1.0",
  "com.h2database" % "h2" % "1.3.171",
  "org.apache.commons" % "commons-lang3" % "3.3.2",
  "org.scalatest" % "scalatest_2.11" % "2.2.1" % "test"
)
