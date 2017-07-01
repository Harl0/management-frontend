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
  def getAllClients: Future[Seq[Client]]
  = {
    ws
      .url(s"${config.clientUrl}/retrieve")
      .get().map {
      case res if res.status == 200 => res.json.as[Seq[Client]]
    }.recover { case e => Logger.info("Unable to retrieve data"); Client.buildEmpty }
  }

  def deleteClient(_id: String): Future[String]
  = {

    val params = getParamMap(_id)
    ws
      .url(s"${config.clientUrl}/delete/${params}")
      .get().map {
      case res if res.status == 200 => print("received" + res.body); "Ok"
      case res if res.status != 200 => print("received" + res.status + "" + res.body); "None 200"
    }.recover { case e => Logger.info("Unable to retrieve data"); "Not Ok" }
  }

  private def getParamMap(_id: String): Map[String, Seq[String]] = {
    val mmSeq: MutableMap[String, Seq[String]] = MutableMap()

    addParamSeq(mmSeq, Some(_id), "_id")
    mmSeq.toMap

  }


  private def addParamSeq(mm: MutableMap[String, Seq[String]], param: Option[String], key: String) = {
    if (param.isDefined && !param.get.toUpperCase.startsWith("X")) {
      mm.put(key, Seq(param.getOrElse("")))
    }
  }

}
