package controllers

import com.google.inject.Inject
import com.typesafe.scalalogging.LazyLogging
import config.AppConfig
import connectors.ClientConnector
import models.ClientForm._
import models.ClientRegister
import services.ClientService
import play.api.i18n.I18nSupport
import play.api.libs.json.Json
import play.api.libs.ws.WSClient
import play.api.mvc._

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
    Ok(views.html.createClient(clients, clientForm))
  }

  /**
    * POST  /postCreateClient
    */
  def postCreateClient: Action[AnyContent] = Action.async { implicit request =>
    clientForm.bindFromRequest.fold(
      formWithErrors => {
        Future {
          BadRequest(views.html.createClient(clients, formWithErrors))
        }
      },
      data => {
        logger.info("Sending: "+Json.toJson(data))
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

  def retrieveClientDetail(_id: String, clientName: String, redirectURIs: String, clientId: String,
                           clientSecret: String, imageURI: String, contactName: String, contactDetails: String):
  Action[AnyContent] = Action { implicit request: RequestHeader =>
    import models.Client
    val clientDetail = Client(_id, clientName, redirectURIs, clientId, clientSecret, Some(imageURI), Some(contactName), Some(contactDetails))

    Ok(views.html.viewClient(clientDetail, clientsView, clientForm))
  }

  def deleteClient(_id: String): Action[AnyContent] = Action.async { implicit request =>
    clientConnector.deleteClient(_id).map { _ =>
      Ok(views.html.deleteClient(_id))
    }.recover {
      case ex: Exception => InternalServerError
    }
  }
}
