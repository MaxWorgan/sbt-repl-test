package org.example

import sbt._
import complete.DefaultParsers._
import java.io.File


final class Main extends xsbti.AppMain {

  def run(configuration: xsbti.AppConfiguration) =
    MainLoop.runLogged(initialState(configuration))

  def initialState(conf: xsbti.AppConfiguration) =
    State(
      conf,
      Seq(hello, helloAll, changeColor, BasicCommands.shell),
      Set.empty,
      None,
      Seq("shell"),
      State.newHistory,
      AttributeMap.empty,
      initialGlobalLogging,
      State.Continue
    )

  val Hello = "hello"
  val hello = Command.command(Hello) { s ⇒
    s.log.info("Hello! 123")
    s
  }

  def helloAll = Command.args("helloAll", "<name>") { (state, args) =>
    println("Hi " + args.mkString(" "))
    state
  }

  lazy val change = Space ~> (reset | setColor)
  lazy val reset = token("reset" ^^^ "\033[0m")
  lazy val color = token( Space ~> ("blue" ^^^ "4" | "green" ^^^ "2") )
  lazy val select = token( "fg" ^^^ "3" | "bg" ^^^ "4" )
  lazy val setColor = (select ~ color) map { case (g, c) => "\033[" + g + c + "m" }

  def changeColor = Command("color")(_ ⇒ change) { (state, ansicode) =>
      print(ansicode)
      state
  }

  def initialGlobalLogging =
    GlobalLogging.initial(MainLogging.globalDefault(ConsoleOut.systemOut), File.createTempFile("hello", "log"), ConsoleOut.systemOut)
}
