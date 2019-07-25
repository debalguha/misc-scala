name := "scala-misc"

version := "0.1"

scalaVersion := "2.12.8"

lazy val akkaVersion = "2.6.0-M4"

libraryDependencies += "commons-io" % "commons-io" % "2.6"

libraryDependencies ++= Seq(
  "com.typesafe.akka" %% "akka-actor" % akkaVersion,
  "com.typesafe.akka" %% "akka-testkit" % akkaVersion,
  "org.scalatest" %% "scalatest" % "3.0.5" % "test"
)
libraryDependencies ++= Seq("org.specs2" %% "specs2-core" % "4.6.0" % "test")
libraryDependencies ++= Seq("org.specs2" %% "specs2-scalacheck" % "4.6.0" % "test")
libraryDependencies ++= Seq("org.scalacheck" %% "scalacheck" % "1.14.0" % "test")

scalacOptions in Test ++= Seq("-Yrangepos")