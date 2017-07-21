package connectors

import com.google.inject.Inject
import com.typesafe.scalalogging.LazyLogging
import config.AppConfig
import helpers.ErrorHelper
import models.CollectorForm.CollectorRegistrationForm
import models.Log.processResponse
import models.{Collector, CollectorRegistrationResponse}
import play.api.Logger
import play.api.libs.json.Json
import play.api.libs.ws.WSClient
import utils.Constants._
import play.api.http.Status._

import scala.collection.mutable.{Map => MutableMap}
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

/**
  * Created by jason on 29/06/17.
  */
class CollectorUpdateConnector @Inject()(ws: WSClient, config: AppConfig, errorHelper: ErrorHelper) extends LazyLogging {

  def createCollector(client: CollectorRegistrationForm): Future[Either[String,CollectorRegistrationResponse]]
  = {
    ws
      .url(s"${config.collectorUrl}/create")
      .withHttpHeaders("Accept" -> "application/json")
      .post(Json.toJson(client))
      .map {
        case res if res.status == OK =>
          Right(res.json.as[CollectorRegistrationResponse])
        case failure@_ =>
          val errorMessage = errorHelper.non200Error(failure.status,COLLECTOR_SERVICE)
          logger.error(processResponse(COLLECTOR_UPDATE_RESPONSE, errorMessage.toString, OK))
          Left(errorMessage)
      }.recover {
      case e =>
        val errorMessage = errorHelper.internalError(e.getMessage)
        logger.error(processResponse(COLLECTOR_UPDATE_RESPONSE, errorMessage.toString, OK))
        Left(errorMessage)
    }
  }

  def updateCollector(client: Collector): Future[Either[String,Collector]]
  = {
    ws
      .url(s"${config.collectorUrl}/update")
      .withHttpHeaders("Accept" -> "application/json")
      .post(Json.toJson(client))
      .map {
        case res if res.status == OK =>
          Right(res.json.as[Collector])
        case failure@_ =>
          val errorMessage = errorHelper.non200Error(failure.status,COLLECTOR_SERVICE)
          logger.error(processResponse(COLLECTOR_UPDATE_RESPONSE, errorMessage.toString, OK))
          Left(errorMessage)
      }.recover {
      case e =>
        val errorMessage = errorHelper.internalError(e.getMessage)
        logger.error(processResponse(COLLECTOR_UPDATE_RESPONSE, errorMessage.toString, OK))
        Left(errorMessage)
    }
  }
}
