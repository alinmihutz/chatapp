package gui

import java.io.{BufferedReader, InputStreamReader, PrintStream}
import java.net.Socket

import actors.{LoginActor, LogoutActor}
import akka.actor.{ActorRef, ActorSystem, Props}
import clients.CassandraChatappClient
import clients.GUIChatClient.is
import com.datastax.driver.core.{ResultSet, Row}

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import scalafx.Includes._
import scalafx.application.JFXApp
import scalafx.application.JFXApp.PrimaryStage
import scalafx.event.ActionEvent
import scalafx.geometry.Insets
import scalafx.scene.Scene
import scalafx.scene.control._
import scalafx.scene.layout.GridPane

class ChatGUI extends JFXApp {
  implicit val system = ActorSystem("Chatapp")
  val loginActor: ActorRef = system.actorOf(Props[LoginActor], name = "loginactor")
  val logoutActor: ActorRef = system.actorOf(Props[LogoutActor], name = "logoutactor")

  val host = "localhost"
  val port = 4444
  val socket = new Socket(host, port)
  var flag = true
  val is = new BufferedReader(new InputStreamReader(socket.getInputStream))
  val os = new PrintStream(socket.getOutputStream)

  stage = new PrimaryStage {
    title = "Chat"
    scene = new Scene(400, 600) {
      val username = new TextField() {
        promptText = "Username"
        focusTraversable = false
      }
      val password = new PasswordField() {
        promptText = "Password"
        focusTraversable = false
      }
      val loginBtn = new Button("Login")
      loginBtn.disable = true
      val logoutBtn = new Button("Log out")

      val textField = new TextField()
      val textArea = new TextArea()

      textArea.prefColumnCount = 15
      textArea.prefRowCount = 15
      //textArea.disable = false

      // Grid Panes
      val loginGridPane = new GridPane() {
        hgap = 10
        vgap = 10
        padding = Insets(20, 100, 10, 10)

        add(new Label("Username:"), 0, 0)
        add(username, 1, 0)
        add(new Label("Password:"), 0, 1)
        add(password, 1, 1)
        add(loginBtn, 1, 2)
      }

      val chatGridPane = new GridPane() {
        hgap = 10
        vgap = 10
        padding = Insets(20, 100, 10, 10)

        add(textArea, 0, 0)
        add(textField, 0, 1)
        add(logoutBtn, 0, 2)
      }

      // Threads
      Future {
        while (true) {
          if (!username.text.value.isEmpty && !password.text.value.isEmpty) loginBtn.disable = false
          else loginBtn.disable = true

          Thread.sleep(100)
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

      // Events
      textField.onAction = (ev: ActionEvent) => {
        if (textField.text.value != "") {
          os.println(textField.text.value)
          textField.text.value = ""
        }
      }
      loginBtn.onAction = (ev: ActionEvent) => {
        try {
          val resultSet: ResultSet = CassandraChatappClient.getUser(
            username.text.value,
            password.text.value
          )
          val row: Row = resultSet.one()

          //Logged
          CassandraChatappClient.updateUserIsOnline(isUserOnline = true, row.getInt("user_id"))

          textArea.appendText("Connected as " + row.getString("user_name") + "\n")
          os.println(row.getString("user_name"))

          content.clear()
          content.add(chatGridPane)
        } catch {
          case any: Any => {
            loginGridPane.add(new Label("Try again !"), 1, 3)
          }
        }
      }

      // Content
      content.add(loginGridPane)
    } // scene
  } // stage
}
