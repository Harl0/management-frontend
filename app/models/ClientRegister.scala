package models

import play.api.libs.json.Json

/**
  * Created by abhishek on 21/06/17.
  */
case class ClientRegister(clientId: String,
                          clientSecret: String,
                          redirectURI: String)

object ClientRegister {
  implicit val clientRegisterSuccessformat = Json.format[ClientRegister]
}