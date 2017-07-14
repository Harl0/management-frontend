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
class ClientRetrieveConnector @Inject()(ws: WSClient, config: AppConfig) {

  def retrieveClientList: Future[Either[String,Seq[Client]]]
  = {
    ws
      .url(s"${config.clientUrl}/retrieve")
      .get().map {
      case res if res.status == 200 =>
        Logger.info("Received status : " + res.status)
        Logger.info("Received data: " + res.json.as[Seq[Client]])
        Right(res.json.as[Seq[Client]])
    }.recover { case e =>
      Logger.info("Unable to retrieve data")
      Logger.info("Connector received: " + e.getCause)
      Left(e.getMessage)
    }
  }

  def retrieveClientDetail(_id: String): Future[Either[String,Client]]
  = {
    Logger.info("Sending ID " + _id)
    val request = s"${config.clientUrl}/clientDetail?_id=${_id}"
    ws
      .url(request)
      .get().map {
      case res if res.status == 200 => Right(res.json.as[Client])
    }.recover {
      case e =>
        Logger.info("Unable to retrieve data")
        Logger.info("Connector received: " + e.getCause)
        Left(e.getMessage)
    }
  }

  def deleteClient(_id: String): Future[Either[String,Boolean]]
  = {
    val request = s"${config.clientUrl}/delete?_id=${_id}"
    ws
      .url(request)
      .get().map {
      case res if res.status == 200 => Right(true)
    }.recover { case e =>
      Logger.info("Unable to retrieve data")
      Logger.info("Connector received: " + e.getCause)
      Left(e.getMessage)
    }
  }
}
