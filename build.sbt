name := "chatapp"

version := "1.0"

scalaVersion := "2.12.2"

lazy val akkaVersion = "2.5.3"

libraryDependencies ++= Seq(
  "com.typesafe.akka" %% "akka-actor" % akkaVersion,
  "com.typesafe.akka" %% "akka-testkit" % akkaVersion,
  "org.scalatest" %% "scalatest" % "3.0.1" % "test",
  "com.lightbend.akka" %% "akka-stream-alpakka-cassandra" % "0.10",
  "com.datastax.cassandra" % "cassandra-driver-core" % "3.0.0",
  "org.scalafx" %% "scalafx" % "8.0.102-R11"
)
