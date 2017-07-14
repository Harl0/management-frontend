package controllers

import com.google.inject.Inject
import com.typesafe.scalalogging.LazyLogging
import config.AppConfig
import connectors.ClientRetrieveConnector
import helpers.ClientHelper
import models.ClientForm._
import play.api.i18n.I18nSupport
import play.api.libs.ws.WSClient
import play.api.mvc._

import scala.concurrent.ExecutionContext.Implicits.global

/**
  * Created by jason on 24/05/17.
  */

class ClientRetrieveController @Inject()(ws: WSClient, config: AppConfig, cc: ControllerComponents,
                                         clientConnector: ClientRetrieveConnector, helper: ClientHelper)
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
      clientConnector.retrieveClientList.map {
        case Right(clientList) => Ok(views.html.retrieveClients(clientList))
        case error@_ => Ok(views.html.error())
      }
  }

  /**
    * GET   /client/detail
    */

  def retrieveClientDetail(_id: String, message: Option[String]): Action[AnyContent] = Action.async {
    implicit request: RequestHeader =>
      clientConnector.retrieveClientDetail(_id).map {
        case Right(clientDetails) => Ok(views.html.clientDetail(clientDetails, clientViewForm, message))
        case error@_ => Ok(views.html.error())
      }
  }

  /**
    * GET   /client/delete
    */
  def deleteClient(_id: String): Action[AnyContent] = Action.async {
    implicit request =>
      clientConnector.deleteClient(_id).map { _ =>
        Ok
      }.recover {
        case ex: Exception => Ok(views.html.error())
      }
  }

}
