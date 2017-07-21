package models

import java.time.LocalDate
import utils.Constants._
import play.api.libs.json.Json

/**
  * Created by jason on 29/06/17.
  */
case class Collector(
                   _id: String,
                   collectorName: String,
                   redirectURIs: String,
                   collectorId: String,
                   collectorSecret: String,
                   imageURI: Option[String] = None,
                   contactName: Option[String] = None,
                   contactDetails: Option[String] = None,
                   serviceStartDate: Option[LocalDate] = None
                 )

object Collector {
  implicit val formats = Json.format[Collector]

  def buildEmptyCollectorSeq: Seq[Collector] = {
    Seq(buildEmptyCollector)
  }

  def buildEmptyCollector: Collector = {
    Collector(
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
