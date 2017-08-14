package networking

import java.net.{ServerSocket, Socket}
import java.io._
import java.util.concurrent.ConcurrentHashMap
import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global
import scala.collection.JavaConverters._
import collection.mutable

/**
  * ChatServer.
  */
object ChatServer extends App {
  case class User(socket: Socket, is: BufferedReader, ps: PrintStream, name: String)
  val users = new ConcurrentHashMap[String, User]().asScala

  override def main(args: Array[String]) : Unit = {
    val users = new mutable.ArrayBuffer[User]() with mutable.SynchronizedBuffer[User]
    val port = 4444
    val serverSocket = new ServerSocket(port)

    println("Listening...")

    Future {
      while (true) {
        val socket = serverSocket.accept()

        println("New connection " + socket)

        val is = new BufferedReader(new InputStreamReader(socket.getInputStream))
        val os = new PrintStream(socket.getOutputStream)

        Future {
          users += User(socket, is, os, is.readLine())
        }
      }
    }

    while (true) {
      for (user <- users) {
        if (user.is.ready) {
          val input = user.is.readLine
          for (user2 <- users) {
            user2.ps.println(user.name + ": " + input)
          }
        }
      }
    }
  }
}
