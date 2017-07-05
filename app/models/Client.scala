package models

import java.time.LocalDate

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
                   contactDetails: Option[String] = None,
                   serviceStartDate: Option[LocalDate] = None
                 )

object Client {
  implicit val formats = Json.format[Client]

  def buildEmptyClientSeq: Seq[Client] = {
    Seq(buildEmptyClient)
  }

  def buildEmptyClient: Client = {
    Client(
      "No Data",
      "No Data",
      "No Data",
      "No Data",
      "No Data",
      Some("No Data"),
      Some("No Data"),
      Some("No Data"),
      Some(LocalDate.now()))
  }
}
