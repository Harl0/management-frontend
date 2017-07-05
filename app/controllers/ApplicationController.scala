package controllers

import com.google.inject.Inject
import com.typesafe.scalalogging.LazyLogging
import config.AppConfig
import connectors.ClientConnector
import models.ClientForm._
import play.api.i18n.I18nSupport
import play.api.libs.json._
import play.api.libs.ws.WSClient
import play.api.mvc._
import utils.Constants._

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

/**
  * Created by jason on 24/05/17.
  */

class ApplicationController @Inject()(ws: WSClient, config: AppConfig, cc: ControllerComponents,
                                      clientConnector: ClientConnector)
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
        clientConnector.createClient(data).map { x =>
          Ok(views.html.addClientConfirmation(x))
        }.recover {
          case e =>
            logger.error("There was an error in adding client : " + e.getMessage)
            Ok(views.html.error())
        }
      }
    )
  }

  def retrieveClientList: Action[AnyContent] = Action.async {
    implicit request =>
      clientConnector.retrieveClientList.map {
        x =>
          Ok(views.html.retrieveClients(x))
      }.recover {
        case ex: Exception => InternalServerError
      }
  }

  def retrieveClientDetail(_id: String): Action[AnyContent] = Action.async {
    implicit request: RequestHeader =>
      clientConnector.retrieveClientDetail(_id).map {
        clientDetail =>
          Ok(views.html.viewClient(clientDetail, clientViewForm, None))
      }.recover {
        case ex: Exception => InternalServerError
      }
  }

  def postUpdateClient: Action[AnyContent] = Action.async {
    implicit request =>
      clientViewForm.bindFromRequest.fold(
        formWithErrors => {
          val id = request.body.asFormUrlEncoded.collect {
            case s => s("_id").head
          }.getOrElse("")
          clientConnector.retrieveClientDetail(id) map {
            clientData =>
              BadRequest(views.html.viewClient(clientData, formWithErrors, None))
          }
        },
        data => {
          logger.info("Sending: " + Json.toJson(data))
          clientConnector.updateClient(data).map { _ =>
            Ok(views.html.viewClient(data, clientViewForm, Some(updateConfirmationMessage)))
          }.recover {
            case e =>
              logger.error("There was an error in adding client : " + e.getMessage)
              Ok(views.html.error())
          }
        }
      )
  }

  def deleteClient(_id: String, clientName: String, clientId: String): Action[AnyContent] = Action.async {
    implicit request =>
      clientConnector.deleteClient(_id).map { _ =>
        Ok
      }.recover {
        case ex: Exception => InternalServerError
      }
  }
}
