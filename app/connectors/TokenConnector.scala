package connectors

import javax.inject.Singleton

import com.google.inject.Inject
import config.AppConfig
import helpers.ErrorHelper
import play.api.Logger
import play.api.libs.ws.WSClient
import utils.Constants._

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

@Singleton
class TokenConnector @Inject()(ws: WSClient, appConfig: AppConfig, errorHelper: ErrorHelper) {

  def createKeys: Future[Either[String,Boolean]] = {
    ws.url(s"${appConfig.tokenUrl}/createKeys")
      .get().map {
      case res if res.status == 200 => Right(true)
      case failure@_ => Logger.error(DEFAULT_TOKEN_ERROR)
        val errorMessage = errorHelper.non200Error(failure.status,TOKEN_SERVICE)
        Logger.error(errorMessage, new Throwable(DEFAULT_TOKEN_ERROR))
        Left(errorMessage)
    }.recover {
      case error => val errorMessage = errorHelper.internalError(error.getMessage)
        Logger.error(DEFAULT_TOKEN_ERROR)
        Logger.error(errorMessage,error)
        Left(errorMessage)
    }
  }

}
