package connectors

import com.google.inject.Inject
import config.AppConfig
import helpers.ErrorHelper
import models.Client
import play.api.Logger
import play.api.libs.ws.WSClient
import utils.Constants._

import scala.collection.mutable.{Map => MutableMap}
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

/**
  * Created by jason on 29/06/17.
  */
class ClientRetrieveConnector @Inject()(ws: WSClient, config: AppConfig, errorHelper: ErrorHelper) {

  def retrieveClientList: Future[Either[String,Seq[Client]]]
  = {
    ws
      .url(s"${config.clientUrl}/retrieve")
      .get().map {
      case res if res.status == 200 =>
        Logger.info("Received status : " + res.status)
        Logger.info("Received data: " + res.json.as[Seq[Client]])
        Right(res.json.as[Seq[Client]])
      case failure@_ => val errorMessage = errorHelper.non200Error(failure.status,CLIENT_SERVICE)
        Logger.error("Unable to retrieve data")
        Logger.error(errorMessage)
        Left(errorMessage)
    }.recover { case e => val errorMessage = errorHelper.internalError(e.getMessage)
      Logger.error("Unable to retrieve data")
      Logger.error(errorMessage)
      Left(errorMessage)
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

  def deleteClient(_id: String): Future[Either[String,Boolean]]
  = {
    val request = s"${config.clientUrl}/delete?_id=${_id}"
    ws
      .url(request)
      .get().map {
      case res if res.status == 200 => Right(true)
      case failure@_ => val errorMessage = errorHelper.non200Error(failure.status,CLIENT_SERVICE)
        Logger.error("Unable to retrieve data")
        Logger.error(errorMessage)
        Left(errorMessage)
    }.recover { case e => val errorMessage = errorHelper.internalError(e.getMessage)
      Logger.error("Unable to retrieve data")
      Logger.error(errorMessage)
      Left(errorMessage)
    }
  }
}
