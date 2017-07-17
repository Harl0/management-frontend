package connectors

import com.google.inject.Inject
import config.AppConfig
import helpers.ErrorHelper
import models.ClientForm.ClientRegistrationForm
import models.{Client, ClientRegistrationResponse}
import play.api.Logger
import play.api.libs.json.Json
import play.api.libs.ws.WSClient
import utils.Constants._

import scala.collection.mutable.{Map => MutableMap}
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

/**
  * Created by jason on 29/06/17.
  */
class ClientUpdateConnector @Inject()(ws: WSClient, config: AppConfig, errorHelper: ErrorHelper) {

  def createClient(client: ClientRegistrationForm): Future[Either[String,ClientRegistrationResponse]]
  = {
    ws
      .url(s"${config.clientUrl}/create")
      .withHttpHeaders("Accept" -> "application/json")
      .post(Json.toJson(client))
      .map {
        case res if res.status == 200 =>
          Logger.info("Received status : " + res.status)
          Logger.info("Received data: " + res.json.as[ClientRegistrationResponse])
          Right(res.json.as[ClientRegistrationResponse])
        case failure@_ => val errorMessage = errorHelper.non200Error(failure.status,CLIENT_SERVICE)
          Logger.error("Unable to retrieve data")
          Logger.error(errorMessage)
          Left(errorMessage)
      }.recover {
      case e => val errorMessage = errorHelper.internalError(e.getMessage)
        Logger.error("Unable to retrieve data")
        Logger.error(errorMessage)
        Left(errorMessage)
    }
  }

  def updateClient(client: Client): Future[Either[String,Client]]
  = {
    ws
      .url(s"${config.clientUrl}/update")
      .withHttpHeaders("Accept" -> "application/json")
      .post(Json.toJson(client))
      .map {
        case res if res.status == 200 =>
          Logger.info("Received status : " + res.status)
          Logger.info("Received data: " + res.json.as[Client])
          Right(res.json.as[Client])
        case failure@_ => val errorMessage = errorHelper.non200Error(failure.status,CLIENT_SERVICE)
          Logger.error("Unable to retrieve data")
          Logger.error(errorMessage)
          Left(errorMessage)
      }.recover {
      case e => val errorMessage = errorHelper.internalError(e.getMessage)
        Logger.error("Unable to retrieve data")
        Logger.error(errorMessage)
        Left(errorMessage)
    }
  }

}
