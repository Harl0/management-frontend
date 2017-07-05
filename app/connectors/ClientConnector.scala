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
class ClientConnector @Inject()(ws: WSClient, config: AppConfig) {

  def createClient(client: ClientRegistrationForm): Future[ClientRegistrationResponse]
  = {
    ws
      .url(s"${config.clientUrl}/create")
      .withHttpHeaders("Accept" -> "application/json")
      .post(Json.toJson(client))
      .map {
        case res if res.status == 200 =>
          Logger.info("Received status : " + res.status)
          Logger.info("Received data: " + res.json.as[ClientRegistrationResponse])
          res.json.as[ClientRegistrationResponse]
      }.recover {
      case e =>
        Logger.info("Unable to retrieve data")
        Logger.info("Connector received: " + e.getCause)
        ClientRegistrationResponse.buildEmptyClientRegistrationResponse
    }
  }

  def updateClient(client: Client): Future[Client]
  = {
    ws
      .url(s"${config.clientUrl}/update")
      .withHttpHeaders("Accept" -> "application/json")
      .post(Json.toJson(client))
      .map {
        case res if res.status == 200 =>
          Logger.info("Received status : " + res.status)
          Logger.info("Received data: " + res.json.as[Client])
          res.json.as[Client]
      }.recover {
      case e =>
        Logger.info("Unable to retrieve data")
        Logger.info("Connector received: " + e.getCause)
        Client.buildEmptyClient
    }
  }

  def retrieveClientList: Future[Seq[Client]]
  = {
    ws
      .url(s"${config.clientUrl}/retrieve")
      .get().map {
      case res if res.status == 200 =>
        Logger.info("Received status : " + res.status)
        Logger.info("Received data: " + res.json.as[Seq[Client]])
        res.json.as[Seq[Client]]
    }.recover { case e => Logger.info("Unable to retrieve data")
      Logger.info("Connector received: " + e.getCause)
      Client.buildEmptyClientSeq
    }
  }

  def retrieveClientDetail(_id: String): Future[Client]
  = {
    Logger.info("Sending ID " + _id)
    val request = s"${config.clientUrl}/clientDetail?_id=${_id}"
    ws
      .url(request)
      .get().map {
      case res if res.status == 200 => res.json.as[Client]
    }.recover {
      case e =>
        Logger.info("Unable to retrieve data")
        Logger.info("Connector received: " + e.getCause)
        Client.buildEmptyClient
    }
  }

  def deleteClient(_id: String): Future[Boolean]
  = {
    val request = s"${config.clientUrl}/delete?_id=${_id}"
    ws
      .url(request)
      .get().map {
      case res if res.status == 200 => true
    }.recover { case e =>
      Logger.info("Unable to retrieve data")
      Logger.info("Connector received: " + e.getCause)
      false
    }
  }
}
