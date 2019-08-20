name := "scala-misc"

version := "0.1"

scalaVersion := "2.12.8"

lazy val akkaVersion = "2.6.0-M4"

resolvers ++= Seq(
  "Sonatype Snapshots" at "http://oss.sonatype.org/content/repositories/snapshots",
  "Sonatype Releases" at "http://oss.sonatype.org/content/repositories/releases"
)

libraryDependencies += "commons-io" % "commons-io" % "2.6"

libraryDependencies ++= Seq(
  "com.typesafe.akka" %% "akka-actor" % akkaVersion,
  "com.typesafe.akka" %% "akka-testkit" % akkaVersion,
  "org.scalatest" %% "scalatest" % "3.0.5" % "test"
)
libraryDependencies ++= Seq("org.specs2" %% "specs2-core" % "4.6.0" % "test")
libraryDependencies ++= Seq("org.specs2" %% "specs2-scalacheck" % "4.6.0" % "test")
libraryDependencies ++= Seq("org.scalacheck" %% "scalacheck" % "1.14.0" % "test")
libraryDependencies ++= Seq(
  "org.scala-saddle" %% "saddle-core" % "1.3.+"
  // (OPTIONAL) "org.scala-saddle" %% "saddle-hdf5" % "1.3.+"
)
scalacOptions in Test ++= Seq("-Yrangepos")