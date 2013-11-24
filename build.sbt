organization := "org.example"

name := "hello"

version := "0.1-SNAPSHOT"

scalaVersion := "2.10.3"

resolvers += Resolver.url("Typesafe Ivy Releases", url("http://repo.typesafe.com/typesafe/repo"))(Resolver.ivyStylePatterns)

libraryDependencies += "org.scala-sbt" % "command" % "0.12.4"

seq(conscriptSettings :_*)
