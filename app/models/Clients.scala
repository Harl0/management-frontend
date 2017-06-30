package models

import play.api.libs.json.Json

/**
  * Created by jason on 30/06/17.
  */
case class Clients(clients: Seq[Client])

object Clients {
  implicit val formats = Json.format[Clients]

  def buildEmpty: Clients = {
    Clients(Seq(Client(
      "EMPTY _id",
      "EMPTY clientName",
      "EMPTY redirectURIs",
      "EMPTY clientId",
      "EMPTY clientSecret",
      Some("EMPTY imageURI"),
      Some("EMPTY contactName"),
      Some("EMPTY contactDetails")
    ))
    )
  }
}

