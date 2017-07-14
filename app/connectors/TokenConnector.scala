package connectors

import javax.inject.Singleton
import com.google.inject.Inject
import config.AppConfig
import play.api.Logger
import play.api.libs.ws.WSClient

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

@Singleton
class TokenConnector @Inject()(ws: WSClient, config: AppConfig) {

  def createKeys: Future[Boolean] = {
    ws.url(s"${config.tokenUrl}/createKeys")
      .get().map {
      case res if res.status == 200 =>
        true
      case _ => false
    }.recover {
      case e => Logger.error("Unable to create Keys")
        Logger.error(e.getMessage, e)
        false
    }
  }

}
