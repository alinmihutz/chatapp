import gui.ChatGUI

object Chatapp extends App {
  val chatGUI = new ChatGUI()

  new Thread(new Runnable() {
    override def run(): Unit = {
      chatGUI.main(args)
    }
  }).start()
}
