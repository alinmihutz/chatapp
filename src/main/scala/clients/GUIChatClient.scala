package clients

import java.io.{BufferedReader, InputStreamReader, PrintStream}
import java.net.Socket
import javafx.event.EventHandler
import scala.concurrent.Future
import scala.io.StdIn
import scala.concurrent.ExecutionContext.Implicits.global
import scalafx.application.JFXApp
import scalafx.application.JFXApp.PrimaryStage
import scalafx.event.ActionEvent
import scalafx.scene.Scene
import scalafx.scene.control.{TextArea, TextField}
import scalafx.scene.input.KeyEvent
import scalafx.Includes._

/**
  * GUIChatClient.
  */
object GUIChatClient extends JFXApp {
  val host = "localhost"
  val port = 4000
  val socket = new Socket(host, port)
  var flag = true
  val is = new BufferedReader(new InputStreamReader(socket.getInputStream))
  val os = new PrintStream(socket.getOutputStream)

  stage = new PrimaryStage {
    title = "Chat"
    scene = new Scene(400,400) {
      val textField = new TextField() {
        layoutX = 20
        layoutY = 100
        focusTraversable = false
      }

      val textArea = new TextArea() {
        layoutX = 20
        layoutY = 20
      }

      content = List(textArea, textField)

      textField.onAction = (ev: ActionEvent) => {
        if (textField.text.value != "") {
          os.println(textField.text.value)
          textField.text.value = ""
        }
      }

      Future {
        while (true) {
          if (is.ready()) {
            val output = is.readLine()
            textArea.appendText(output + "\n")
          }
        }
        Thread.sleep(100)
      }
    }
  }
}
