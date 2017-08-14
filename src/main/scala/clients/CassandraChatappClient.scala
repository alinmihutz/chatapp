package clients

import com.datastax.driver.core.{Cluster, ResultSet, Session}

object CassandraChatappClient {
  private val cluster = Cluster.builder.addContactPoint("localhost").withPort(9042).build()
  implicit val session: Session = cluster.connect("chatapp")

  def getUser(username: String, password: String): ResultSet = {
    session.execute("SELECT * FROM users WHERE user_name='" + username + "' AND user_password='" + password + "' ALLOW FILTERING;")
  }

  def updateUserIsOnline(isUserOnline: Boolean, userId: Int): ResultSet = {
    def toInt = if (isUserOnline) 1 else 0
    session.execute("UPDATE users SET user_online =" + toInt + " WHERE user_id=" + userId + ";")
  }
}
