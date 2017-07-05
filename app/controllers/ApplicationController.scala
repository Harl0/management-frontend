package controllers

import com.google.inject.Inject
import com.typesafe.scalalogging.LazyLogging
import config.AppConfig
import connectors.ClientConnector
import models.ClientForm._
import models.{Client, ClientRegister}
import play.api.i18n.I18nSupport
import play.api.libs.json._
import play.api.libs.ws.WSClient
import play.api.mvc._
import services.ClientService
import utils.Constants._
import utils.DateConversions

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

/**
  * Created by jason on 24/05/17.
  */

class ApplicationController @Inject()(ws: WSClient, config: AppConfig, cc: ControllerComponents,
                                      clientConnector: ClientConnector, clientOrchestrator: ClientService)
  extends AbstractController(cc) with I18nSupport with LazyLogging {

  /**
    * GET   /home
    */
  def home(): Action[AnyContent] = Action { implicit request: RequestHeader =>
    Ok(views.html.home())
  }

  /**
    * GET   /createClient
    */
  def createClient(): Action[AnyContent] = Action { implicit request: RequestHeader =>
    Ok(views.html.createClient(clients, clientRegistrationForm))
  }

  /**
    * POST  /postCreateClient
    */
  def postCreateClient: Action[AnyContent] = Action.async { implicit request =>

    clientRegistrationForm.bindFromRequest.fold(
      formWithErrors => {
        Future {
          BadRequest(views.html.createClient(clients, formWithErrors))
        }
      },
      data => {
        logger.info("Sending: " + Json.toJson(data))
        ws
          .url(s"${config.clientUrl}/create")
          .withHttpHeaders("Accept" -> "application/json")
          .post(Json.toJson(data))
          .map { x =>
            val cred = Json.fromJson[ClientRegister](x.json).asOpt
            logger.info("new client inserted")
            Ok(views.html.addClientConfirmation(cred.get))
          }.recover {
          case e =>
            logger.error("There was an error in adding client : " + e.getMessage)
            Ok(views.html.error())
        }
      }
    )
  }

  //  def renderRetrieveAllClients(): Action[AnyContent] = Action { implicit request: RequestHeader =>
  //    Ok(views.html.listClientsJS())
  //  }

  //  def retrieveAllClients: Action[AnyContent] = Action.async { implicit request =>
  //    clientOrchestrator.executeGetClients.map { x =>
  //      Ok(Json.toJson(x))
  //    }.recover {
  //      case ex: Exception => InternalServerError
  //    }
  //  }

  def retrieveClientList: Action[AnyContent] = Action.async { implicit request =>
    clientConnector.retrieveClientList.map { x =>
      Ok(views.html.retrieveClients(x))
    }.recover {
      case ex: Exception => InternalServerError
    }
  }

  def retrieveClientDetail(_id: String): Action[AnyContent] = Action.async { implicit request: RequestHeader =>
    clientConnector.retrieveClientDetail(_id).map {
      clientDetail =>
        Ok(views.html.viewClient(clientDetail, clientViewForm, None))
    }.recover {
      case ex: Exception => InternalServerError
    }
  }

  def postUpdateClient: Action[AnyContent] = Action.async { implicit request =>
    println("clientViewForm")
    clientViewForm.bindFromRequest.fold(
      formWithErrors => {
        println("FormErrors "+formWithErrors)
        val id = clientViewForm.data.get("_id").get
        clientConnector.retrieveClientDetail(id) map { clientData =>
          BadRequest(views.html.viewClient(clientData, formWithErrors, None))
        }
      },
      data => {
        logger.info("Sending: " + Json.toJson(data))
        ws
          .url(s"${config.clientUrl}/update")
          .withHttpHeaders("Accept" -> "application/json")
          .post(Json.toJson(data))
          .map { x =>
            val data = Json.fromJson[Client](x.json).asOpt
            logger.info(data.toString)
            logger.info("Client updated!")
            Ok(views.html.viewClient(data.get, clientViewForm, Some(updateConfirmationMessage)))
          }.recover {
          case e =>
            logger.error("There was an error updating client : " + e.getMessage)
            Ok(views.html.updateError())
        }
      }
    )
  }

  def deleteClient(_id: String): Action[AnyContent] = Action.async { implicit request =>
    clientConnector.deleteClient(_id).map { _ =>
      Ok(views.html.deleteClient(_id))
    }.recover {
      case ex: Exception => InternalServerError
    }
  }
}
