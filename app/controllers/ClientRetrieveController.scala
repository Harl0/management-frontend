package controllers
import com.google.inject.Inject
import com.typesafe.scalalogging.LazyLogging
import config.AppConfig
import connectors.ClientRetrieveConnector
import helpers.ClientHelper
import models.ClientForm._
import models.Log.HTTPRequestReceived
import play.api.i18n.I18nSupport
import play.api.libs.ws.WSClient
import play.api.mvc._
import utils.Constants._
import models.Log._
import play.api.http.Status._

import scala.concurrent.ExecutionContext.Implicits.global

/**
  * Created by jason on 24/05/17.
  */

class ClientRetrieveController @Inject()(cc: ControllerComponents,
                                         clientConnector: ClientRetrieveConnector)
  extends AbstractController(cc) with I18nSupport with LazyLogging {

  /**
    * GET   /client/create
    */
  def createClient(): Action[AnyContent] = Action { implicit request: RequestHeader =>
    Ok(views.html.createClient(clients, clientRegistrationForm))
  }

  /**
    * GET   /client/createConfirmation
    */
  def createClientConfirmation(clientId: String, clientSecret: String, createClientResponse: String):
  Action[AnyContent] = Action { implicit request: RequestHeader =>
    Ok(views.html.addClientConfirmation(clientId, clientSecret, createClientResponse))
  }

  /**
    * GET   /client/list
    */

  def retrieveClientList: Action[AnyContent] = Action.async {
    implicit request =>
      val headers = Seq(
        s"method=${request.method}",
        s"""uri="${request.uri}"""",
        s"""remote_address=${request.remoteAddress}"""
      ).mkString(", ")
      logger.info(HTTPRequestReceived(CLIENT_LIST_REQUEST, headers))
      clientConnector.retrieveClientList.map {
        case Right(clientList) => Ok(views.html.retrieveClients(clientList))
        case error@_ =>
          Ok(views.html.error(error.left.getOrElse(DEFAULT_ERROR),HOME_PAGE))
      }
  }

  /**
    * GET   /client/detail
    */

  def retrieveClientDetail(_id: String, message: Option[String]): Action[AnyContent] = Action.async {
    implicit request: RequestHeader =>
      val headers = Seq(
        s"method=${request.method}",
        s"""uri="${request.uri}"""",
        s"""remote_address=${request.remoteAddress}"""
      ).mkString(", ")
      logger.info(HTTPRequestReceived(CLIENT_DETAIL_REQUEST, headers))
      clientConnector.retrieveClientDetail(_id).map {
        case Right(clientDetails) => Ok(views.html.clientDetail(clientDetails, clientViewForm, message))
        case error@_ =>
          Ok(views.html.error(error.left.getOrElse(DEFAULT_ERROR),CLIENT_LIST_PAGE))
      }
  }

  /**
    * GET   /client/delete
    */
  def deleteClient(_id: String): Action[AnyContent] = Action.async {
    implicit request =>
      val headers = Seq(
        s"method=${request.method}",
        s"""uri="${request.uri}"""",
        s"""remote_address=${request.remoteAddress}"""
      ).mkString(", ")
      logger.info(HTTPRequestReceived(CLIENT_DELETE_REQUEST, headers))
      clientConnector.deleteClient(_id).map {
        case Right(success) => Ok
        case error@_ =>
          Ok(views.html.error(error.left.getOrElse(DEFAULT_ERROR),CLIENT_LIST_PAGE))
      }
  }

}
