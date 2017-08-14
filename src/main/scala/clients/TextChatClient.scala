package clients

import java.io.{BufferedReader, InputStreamReader, PrintStream}
import java.net.Socket
import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global
import scala.io.StdIn

/**
  * TextChatClient.
  */
object TextChatClient extends App {
  override def main(args: Array[String]): Unit = {
    val host = "localhost"
    val port = 4000
    val socket = new Socket(host, port)
    var flag = true
    val is = new BufferedReader(new InputStreamReader(socket.getInputStream))
    val os = new PrintStream(socket.getOutputStream)

    Future {
      while (true) {
        if (is.ready()) {
          val output = is.readLine()
          println(output)
        }
      }
      Thread.sleep(100)
    }

    while (flag) {
      val input = StdIn.readLine()
      if (input == "quit") flag = false
      else os.println(input)
    }
  }
}
