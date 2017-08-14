package actors

import akka.actor.Actor

class LogoutActor extends Actor {
  def receive: PartialFunction[Any, Unit] = {
    case "logout" => println("logout...")
    case _       => println("default")
  }
}
