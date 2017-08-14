package actors

import akka.actor.Actor

class LoginActor extends Actor {
  def receive: PartialFunction[Any, Unit] = {
    case "login" => println("login...")
    case _       => println("default")
  }
}
