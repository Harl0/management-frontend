package connectors

import com.google.inject.Inject
import config.AppConfig
import models.Client
import play.api.Logger
import play.api.libs.ws.WSClient

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

/**
  * Created by jason on 29/06/17.
  */
class ClientConnector @Inject()(ws: WSClient, config: AppConfig) {
  def getAllClients: Future[Seq[Client]]
  = {
    ws
      .url(s"${config.clientUrl}/retrieve")
      .get().map{
      case res if res.status == 200 => res.json.as[Seq[Client]]
    }.recover { case e => Logger.info("Unable to retrieve data"); Client.buildEmpty}
  }
}
