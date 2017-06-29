package connectors

import com.google.inject.Inject
import config.AppConfig
import play.api.libs.ws.{WSClient, WSResponse}

import scala.concurrent.Future

/**
  * Created by jason on 29/06/17.
  */
class ClientConnector @Inject()(ws: WSClient, config: AppConfig) {

  def getAllClients: Future[WSResponse]
  = {
    ws
      .url(s"${config.clientUrl}/retrieve")
      .get()
  }

}
