package connectors

import com.google.inject.Inject
import config.AppConfig
import models.ClientForm.ClientRegistrationForm
import models.{Client, ClientRegistrationResponse}
import play.api.Logger
import play.api.libs.json.Json
import play.api.libs.ws.WSClient

import scala.collection.mutable.{Map => MutableMap}
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

/**
  * Created by jason on 29/06/17.
  */
class ClientUpdateConnector @Inject()(ws: WSClient, config: AppConfig) {

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
      }.recover {
      case e =>
        Logger.error("Unable to retrieve data")
        Logger.error("Connector received: " + e.getCause)
        Left(e.getMessage)
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
      }.recover {
      case e =>
        Logger.info("Unable to retrieve data")
        Logger.info("Connector received: " + e.getCause)
        Left(e.getMessage)
    }
  }

}
