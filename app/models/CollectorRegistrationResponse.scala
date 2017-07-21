package models

import play.api.libs.json.{Json, OFormat}
import utils.Constants._

case class CollectorRegistrationResponse(collectorId: String,
                                         collectorSecret: String,
                                         redirectURI: String)

object CollectorRegistrationResponse {
  implicit val collectorRegisterSuccessformat: OFormat[CollectorRegistrationResponse] = Json.format[CollectorRegistrationResponse]

  def buildEmptyCollectorRegistrationResponse: CollectorRegistrationResponse = {
    CollectorRegistrationResponse(
      NO_DATA,
      NO_DATA,
      NO_DATA
    )
  }
}
