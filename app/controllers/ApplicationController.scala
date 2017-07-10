package controllers

import com.google.inject.Inject
import com.typesafe.scalalogging.LazyLogging
import config.AppConfig
import connectors.{ClientConnector, TokenConnector}
import models.ClientForm._
import models.{Client, ClientRegistrationResponse}
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
                                      clientConnector: ClientConnector, tokenConnector: TokenConnector)
  extends AbstractController(cc) with I18nSupport with LazyLogging {

  /**
    * GET   /
    */
  def home(): Action[AnyContent] = Action { implicit request: RequestHeader =>
    Ok(views.html.home())
  }

  /**
    * GET   /client/create
    */
  def createClient(): Action[AnyContent] = Action { implicit request: RequestHeader =>
    Ok(views.html.createClient(clients, clientRegistrationForm))
  }

  /**
    * POST  /client/create
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
          Redirect(routes.ApplicationController.createClientConfirmation(x.clientId, x.clientSecret, processClientCreateResponse(x)), FOUND)
        }.recover {
          case e =>
            logger.error("There was an error in adding client : " + e.getMessage)
            Ok(views.html.error())
        }
      }
    )
  }

  def processClientCreateResponse(data: ClientRegistrationResponse): String = {
    data.clientId match {
      case "No Data" => clientRegistrationFailure
      case _ => clientRegistrationSuccess
    }
  }

  def processClientUpdateResponse(data: Client): String = {
    data.clientId match {
      case "No Data" => clientUpdateFailure
      case _ => clientUpdateSuccess
    }
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
        x =>
          Ok(views.html.retrieveClients(x))
      }.recover {
        case ex: Exception => InternalServerError
      }
  }

  /**
    * GET   /client/detail
    */
  def retrieveClientDetail(_id: String, message: Option[String]): Action[AnyContent] = Action.async {
    implicit request: RequestHeader =>
      clientConnector.retrieveClientDetail(_id).map {
        clientDetail =>
          Ok(views.html.clientDetail(clientDetail, clientViewForm, message))
      }.recover {
        case ex: Exception => InternalServerError
      }
  }

  /**
    * POST   /client/update
    */
  def postUpdateClient: Action[AnyContent] = Action.async {
    implicit request =>
      clientViewForm.bindFromRequest.fold(
        formWithErrors => {
          val id = request.body.asFormUrlEncoded.collect {
            case s => s("_id").head
          }.getOrElse("")
          clientConnector.retrieveClientDetail(id) map {
            clientData =>
              BadRequest(views.html.clientDetail(clientData, formWithErrors, None))
          }
        },
        data => {
          logger.info("Sending: " + Json.toJson(data))
          clientConnector.updateClient(data).map { x =>
            Redirect(routes.ApplicationController.retrieveClientDetail(x._id, Some(processClientUpdateResponse(x))), FOUND)
          }.recover {
            case e =>
              logger.error("There was an error in adding client : " + e.getMessage)
              Ok(views.html.error())
          }
        }
      )
  }

  /**
    * GET   /client/delete
    */
  def deleteClient(_id: String): Action[AnyContent] = Action.async {
    implicit request =>
      clientConnector.deleteClient(_id).map { _ =>
        Ok
      }.recover {
        case ex: Exception => InternalServerError
      }
  }

  /**
    * GET   /token/createKeys
    */
  def createTokenKeys(): Action[AnyContent] = Action.async { implicit request: RequestHeader =>
    tokenConnector.createKeys.map { _ =>
      Ok(views.html.tokenCreateKeys())
    }.recover {
      case ex: Exception => InternalServerError
    }
  }
}
