package models

import play.api.libs.json.Json

/**
  * Created by abhishek on 21/06/17.
  */
case class ClientRegistrationResponse(clientId: String,
                                      clientSecret: String,
                                      redirectURI: String)

object ClientRegistrationResponse {
  implicit val clientRegisterSuccessformat = Json.format[ClientRegistrationResponse]

  def buildEmptyClientRegistrationResponse: ClientRegistrationResponse = {
    ClientRegistrationResponse(
      "No Data",
      "No Data",
      "No Data"
    )
  }

}