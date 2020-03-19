name := "akka-persistence"

version := "0.1"

scalaVersion := "2.12.7"
lazy val akkaVersion = "2.5.13" // must be 2.5.13 so that it's compatible with the stores plugins (JDBC and Cassandra)
lazy val leveldbVersion = "0.7"
lazy val leveldbjniVersion = "1.8"
lazy val postgresVersion = "42.2.2"
lazy val cassandraVersion = "0.91"
lazy val json4sVersion = "3.2.11"
lazy val protobufVersion = "3.6.1"

// some libs are available in Bintray's JCenter
resolvers += Resolver.jcenterRepo

libraryDependencies ++= Seq(
  "com.typesafe.akka" %% "akka-persistence" % akkaVersion,

  // local levelDB stores
  "org.iq80.leveldb" % "leveldb" % leveldbVersion,
  "org.fusesource.leveldbjni" % "leveldbjni-all" % leveldbjniVersion,

  // JDBC with PostgreSQL 
  "org.postgresql" % "postgresql" % postgresVersion,
  "com.github.dnvriend" %% "akka-persistence-jdbc" % "3.4.0",

  // Cassandra
  "com.typesafe.akka" %% "akka-persistence-cassandra" % cassandraVersion,
  "com.typesafe.akka" %% "akka-persistence-cassandra-launcher" % cassandraVersion % Test,

  // Google Protocol Buffers
  "com.google.protobuf" % "protobuf-java"  % protobufVersion,
)