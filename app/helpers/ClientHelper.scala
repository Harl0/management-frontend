package helpers

import models.{Client, ClientRegistrationResponse}
import utils.Constants._

/**
  * Created by yang on 14/07/17.
  */
class ClientHelper {

  def processClientCreateResponse(data: ClientRegistrationResponse): String = {
    data.clientId match {
      case NO_DATA => CLIENT_REGISTRATION_FAILURE
      case _ => CLIENT_REGISTRATION_SUCCESS
    }
  }

  def processClientUpdateResponse(data: Client): String = {
    data.clientId match {
      case NO_DATA => CLIENT_UPDATE_FAILURE
      case _ => CLIENT_UPDATE_SUCCESS
    }
  }

}
