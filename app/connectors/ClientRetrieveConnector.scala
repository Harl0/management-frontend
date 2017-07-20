package connectors

import com.google.inject.Inject
import com.typesafe.scalalogging.LazyLogging
import config.AppConfig
import helpers.ErrorHelper
import models.Client
import models.Log.processResponse
import play.api.Logger
import play.api.http.Status._
import play.api.libs.ws.WSClient
import utils.Constants._

import scala.collection.mutable.{Map => MutableMap}
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

/**
  * Created by jason on 29/06/17.
  */
class ClientRetrieveConnector @Inject()(ws: WSClient, config: AppConfig, errorHelper: ErrorHelper) extends LazyLogging {

  def retrieveClientList: Future[Either[String,Seq[Client]]]
  = {
    ws
      .url(s"${config.clientUrl}/retrieve")
      .get().map {
      case res if res.status == OK =>
        Right(res.json.as[Seq[Client]])
      case failure@_ =>
        val errorMessage = errorHelper.non200Error(failure.status,CLIENT_SERVICE)
        logger.error(processResponse(CLIENT_LIST_RESPONSE, errorMessage.toString, OK))
        Left(errorMessage)
    }.recover { case e =>
      val errorMessage = errorHelper.internalError(e.getMessage)
      logger.error(processResponse(CLIENT_LIST_RESPONSE, errorMessage.toString, OK))
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
      case res if res.status == OK => Right(res.json.as[Client])
      case failure@_ =>
        val errorMessage = errorHelper.non200Error(failure.status,CLIENT_SERVICE)
        logger.error(processResponse(CLIENT_DETAIL_RESPONSE, errorMessage.toString, OK))
        Left(errorMessage)
    }.recover {
      case e =>
        val errorMessage = errorHelper.internalError(e.getMessage)
        logger.error(processResponse(CLIENT_DETAIL_RESPONSE, errorMessage.toString, OK))
        Left(errorMessage)
    }
  }

  def deleteClient(_id: String): Future[Either[String,Boolean]]
  = {
    val request = s"${config.clientUrl}/delete?_id=${_id}"
    ws
      .url(request)
      .get().map {
      case res if res.status == OK => Right(true)
      case failure@_ =>
        val errorMessage = errorHelper.non200Error(failure.status,CLIENT_SERVICE)
        logger.error(processResponse(CLIENT_DELETE_RESPONSE, errorMessage.toString, OK))
        Left(errorMessage)
    }.recover { case e =>
      val errorMessage = errorHelper.internalError(e.getMessage)
      logger.error(processResponse(CLIENT_DELETE_RESPONSE, errorMessage.toString, OK))
      Left(errorMessage)
    }
  }
}
