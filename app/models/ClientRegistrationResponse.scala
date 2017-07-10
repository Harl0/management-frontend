package models

import play.api.libs.json.{Json, OFormat}
import utils.Constants._
/**
  * Created by abhishek on 21/06/17.
  */
case class ClientRegistrationResponse(clientId: String,
                                      clientSecret: String,
                                      redirectURI: String)

object ClientRegistrationResponse {
  implicit val clientRegisterSuccessformat: OFormat[ClientRegistrationResponse] = Json.format[ClientRegistrationResponse]

  def buildEmptyClientRegistrationResponse: ClientRegistrationResponse = {
    ClientRegistrationResponse(
      NO_DATA,
      NO_DATA,
      NO_DATA
    )
  }
}
