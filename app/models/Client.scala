package models

import java.time.OffsetDateTime

import play.api.libs.json.Json

/**
  * Created by jason on 29/06/17.
  */
case class Client(
                   _id: String,
                   clientName: String,
                   redirectURIs: String,
                   clientId: String,
                   clientSecret: String,
                   imageURI: Option[String] = None,
                   contactName: Option[String] = None,
                   contactDetails: Option[String] = None
                   //                   serviceStartDate: Option[OffsetDateTime] = None
                 )

object Client {
  implicit val formats = Json.format[Client]

  def buildEmptyClientSeq: Seq[Client] = {
    Seq(Client(
      "EMPTY _id",
      "EMPTY clientName",
      "EMPTY redirectURIs",
      "EMPTY clientId",
      "EMPTY clientSecret",
      Some("EMPTY imageURI"),
      Some("EMPTY contactName"),
      Some("EMPTY contactDetails")
    )
    )
  }

  def buildEmptyClient: Client = {
    Client(
      "EMPTY _id",
      "EMPTY clientName",
      "EMPTY redirectURIs",
      "EMPTY clientId",
      "EMPTY clientSecret",
      Some("EMPTY imageURI"),
      Some("EMPTY contactName"),
      Some("EMPTY contactDetails")
    )
  }
}
