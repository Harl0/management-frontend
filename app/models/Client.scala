package models

import java.time.{LocalDate, OffsetDateTime, ZoneOffset}

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
                   imageURI: String = "",
                   contactName: String = "",
                   contactDetails: String = "",
                   serviceStartDate: Option[LocalDate] = None
                 )

object Client {
  implicit val formats = Json.format[Client]

  implicit def toOffsetDateTime(epochMillis: Option[String]): Option[OffsetDateTime] = {
    epochMillis match {
      case Some(date) => Some(OffsetDateTime.of(LocalDate.parse(date).atStartOfDay(), ZoneOffset.UTC))
      case None => None
    }
  }

  //  def buildClient(client: ): Seq[Client] = {
  //    Seq(Client())
  //  }
  def buildEmptyClientSeq: Seq[Client] = {
    Seq(buildEmptyClient)
  }

  def buildEmptyClient: Client = {
    Client(
      "EMPTY _id",
      "EMPTY clientName",
      "EMPTY redirectURIs",
      "EMPTY clientId",
      "EMPTY clientSecret",
      "EMPTY imageURI",
      "EMPTY contactName",
      "EMPTY contactDetails",
      Some(LocalDate.now())
    )
  }
}
