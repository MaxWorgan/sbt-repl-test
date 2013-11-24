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
      Seq(hello, helloAll, changeColor, BasicCommands.help, BasicCommands.shell, BasicCommands.nop),
      Set.empty,
      None,
      Seq(if (conf.arguments.nonEmpty) conf.arguments.mkString(" ") else Hello, "shell"),
      State.newHistory,
      AttributeMap.empty,
      initialGlobalLogging,
      State.Continue
    )

  val Hello = "hello"
  val hello = Command.command(Hello, "test command", "detail help info") { s ⇒
    s.log.info("Hello!")
    s
  }

  def helloAll = Command.args("helloAll", ("helloAll", "короткое описание"), "длинное описание", "<name>") { (state, args) =>
    println("Hi " + args.mkString(" "))
    state
  }

  lazy val change = Space ~> (reset | setColor)
  lazy val reset = token("reset" ^^^ "\033[0m")
  lazy val color = token(Space ~> ("blue" ^^^ "4" | "green" ^^^ "2"))
  lazy val select = token("fg" ^^^ "3" | "bg" ^^^ "4")
  lazy val setColor = (select ~ color) map { case (g, c) => "\033[" + g + c + "m"}

  def changeColor = Command("color", ("color", "коротко"), "длинно")(_ ⇒ change) { (state, ansicode) =>
    print(ansicode)
    state
  }

  def initialGlobalLogging =
    GlobalLogging.initial(MainLogging.globalDefault(ConsoleOut.systemOut), File.createTempFile("history", "log"), ConsoleOut.systemOut)
}
