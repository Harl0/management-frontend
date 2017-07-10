package models

import java.time.LocalDate
import utils.Constants._
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
      NO_DATA,
      NO_DATA,
      NO_DATA,
      NO_DATA,
      NO_DATA,
      Some(NO_DATA),
      Some(NO_DATA),
      Some(NO_DATA),
      Some(LocalDate.now()))
  }
}
