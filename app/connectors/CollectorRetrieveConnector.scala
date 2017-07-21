package connectors

import com.google.inject.Inject
import com.typesafe.scalalogging.LazyLogging
import config.AppConfig
import helpers.ErrorHelper
import models.Collector
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
class CollectorRetrieveConnector @Inject()(ws: WSClient, config: AppConfig, errorHelper: ErrorHelper) extends LazyLogging {

  def retrieveCollectorList: Future[Either[String,Seq[Collector]]]
  = {
    ws
      .url(s"${config.collectorUrl}/retrieve")
      .get().map {
      case res if res.status == OK =>
        Right(res.json.as[Seq[Collector]])
      case failure@_ =>
        val errorMessage = errorHelper.non200Error(failure.status,COLLECTOR_SERVICE)
        logger.error(processResponse(COLLECTOR_LIST_RESPONSE, errorMessage.toString, OK))
        Left(errorMessage)
    }.recover { case e =>
      val errorMessage = errorHelper.internalError(e.getMessage)
      logger.error(processResponse(COLLECTOR_LIST_RESPONSE, errorMessage.toString, OK))
      Left(errorMessage)
    }
  }

  def retrieveCollectorDetail(_id: String): Future[Either[String,Collector]]
  = {
    Logger.info("Sending ID " + _id)
    val request = s"${config.collectorUrl}/collectorDetail?_id=${_id}"
    ws
      .url(request)
      .get().map {
      case res if res.status == OK => Right(res.json.as[Collector])
      case failure@_ =>
        val errorMessage = errorHelper.non200Error(failure.status,COLLECTOR_SERVICE)
        logger.error(processResponse(COLLECTOR_DETAIL_RESPONSE, errorMessage.toString, OK))
        Left(errorMessage)
    }.recover {
      case e =>
        val errorMessage = errorHelper.internalError(e.getMessage)
        logger.error(processResponse(COLLECTOR_DETAIL_RESPONSE, errorMessage.toString, OK))
        Left(errorMessage)
    }
  }

  def deleteCollector(_id: String): Future[Either[String,Boolean]]
  = {
    val request = s"${config.collectorUrl}/delete?_id=${_id}"
    ws
      .url(request)
      .get().map {
      case res if res.status == OK => Right(true)
      case failure@_ =>
        val errorMessage = errorHelper.non200Error(failure.status,COLLECTOR_SERVICE)
        logger.error(processResponse(COLLECTOR_DELETE_RESPONSE, errorMessage.toString, OK))
        Left(errorMessage)
    }.recover { case e =>
      val errorMessage = errorHelper.internalError(e.getMessage)
      logger.error(processResponse(COLLECTOR_DELETE_RESPONSE, errorMessage.toString, OK))
      Left(errorMessage)
    }
  }
}
