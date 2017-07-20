package connectors

import javax.inject.Singleton

import com.google.inject.Inject
import com.typesafe.scalalogging.LazyLogging
import config.AppConfig
import helpers.ErrorHelper
import models.Log.processResponse
import play.api.http.Status._
import play.api.libs.ws.WSClient
import utils.Constants._

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

@Singleton
class TokenConnector @Inject()(ws: WSClient, appConfig: AppConfig, errorHelper: ErrorHelper) extends LazyLogging{

  def createKeys: Future[Either[String,Boolean]] = {
    ws.url(s"${appConfig.tokenUrl}/createKeys")
      .get().map {
      case res if res.status == OK => Right(true)
      case failure@_ =>
        val errorMessage = errorHelper.non200Error(failure.status,TOKEN_SERVICE)
        logger.error(processResponse(TOKEN_CREATE_RESPONSE, errorMessage.toString, OK))
        Left(errorMessage)
    }.recover {
      case error => val errorMessage = errorHelper.internalError(error.getMessage)
        logger.error(processResponse(TOKEN_CREATE_RESPONSE, errorMessage.toString, OK))
        Left(errorMessage)
    }
  }

}
