package models

import play.api.libs.json._

/**
  * Created by jason on 24/05/17.
  */
case class Client(clientName: String,
                  redirectURIs: String,
                  imageURI: Option[String] = None,
                  contactName: Option[String] = None,
                  contactDetails: Option[String] = None,
                  serviceStartDate: Option[String] = None)

object Client {
  implicit val clientRegisterFormat = Json.format[Client]
}