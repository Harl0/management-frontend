package connectors

import com.google.inject.Inject
import config.AppConfig
import models.Client
import play.api.Logger
import play.api.libs.ws.WSClient

import scala.collection.mutable.{Map => MutableMap}
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

/**
  * Created by jason on 29/06/17.
  */
class ClientConnector @Inject()(ws: WSClient, config: AppConfig) {
  def retrieveClientList: Future[Seq[Client]]
  = {
    ws
      .url(s"${config.clientUrl}/retrieve")
      .get().map {
      case res if res.status == 200 => Logger.info("Received status : "+ res.status);Logger.info("Received data: "+res.json.as[Seq[Client]]); res.json.as[Seq[Client]]
    }.recover { case e => Logger.info("Unable to retrieve data"); Client.buildEmptyClientSeq }
  }

  def retrieveClientDetail(_id: String): Future[Client]
  = {
    Logger.info("Sending ID "+_id)
    val request = s"${config.clientUrl}/clientDetail?_id=${_id}"
    ws
      .url(request)
      .get().map {
      case res if res.status == 200 => res.json.as[Client]
    }.recover { case e => Logger.info("Unable to retrieve data"); Client.buildEmptyClient }
  }

  def deleteClient(_id: String): Future[String]
  = {
    val request = s"${config.clientUrl}/delete?_id=${_id}"
    ws
      .url(request)
      .get().map {
      case res if res.status == 200 => print("received" + res.body); "Ok"
      case res if res.status != 200 => print("received" + res.status + "" + res.body); "None 200"
    }.recover { case e => Logger.info("Unable to retrieve data"); "Not Ok" }
  }

}
