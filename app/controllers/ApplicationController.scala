package controllers

import com.google.inject.Inject
import com.typesafe.scalalogging.LazyLogging
import config.AppConfig
import connectors.ClientConnector
import models.ClientForm._
import models.ClientRegister
import play.api.i18n.I18nSupport
import play.api.libs.json.Json
import play.api.libs.ws.WSClient
import play.api.mvc._

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

/**
  * Created by jason on 24/05/17.
  */

class ApplicationController @Inject()(ws: WSClient, config: AppConfig, cc: ControllerComponents, clientConnector: ClientConnector)
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
    Ok(views.html.addClient(clients, clientForm))
  }

  /**
    * POST  /postCreateClient
    */
  def postCreateClient: Action[AnyContent] = Action.async { implicit request =>
    clientForm.bindFromRequest.fold(
      formWithErrors => {
        Future {
          BadRequest(views.html.addClient(clients, formWithErrors))
        }
      },
      data => {
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

  def retrieveAllClients: Action[AnyContent] = Action.async { implicit request =>
    clientConnector.getAllClients.map { x =>
      Ok(Json.toJson(x))
    }.recover {
      case ex: Exception => InternalServerError
    }
  }

  def retrieveAllClientsPage: Action[AnyContent] = Action.async { implicit request =>
    clientConnector.getAllClients.map { x =>
      Ok(views.html.listClients(x))
    }.recover {
      case ex: Exception => InternalServerError
    }
  }
}
