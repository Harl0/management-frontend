package controllers

import com.google.inject.Inject
import com.typesafe.scalalogging.LazyLogging
import config.AppConfig
import connectors.{ClientRetrieveConnector, ClientUpdateConnector}
import helpers.ClientHelper
import models.Client
import models.ClientForm._
import play.api.i18n.{I18nSupport, MessagesApi}
import play.api.libs.json._
import play.api.libs.ws.WSClient
import play.api.mvc._

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

/**
  * Created by jason on 24/05/17.
  */

class ClientUpdateController @Inject()(ws: WSClient, config: AppConfig, cc: ControllerComponents,
                                       clientUpdateConnector: ClientUpdateConnector,
                                       clientRetrieveConnector: ClientRetrieveConnector,
                                       helper: ClientHelper)
  extends AbstractController(cc) with I18nSupport with LazyLogging {

  /**
    * POST  /client/create
    */

  def postCreateClient: Action[AnyContent] = Action.async { implicit request =>
    clientRegistrationForm.bindFromRequest.fold(
      formWithErrors => {
        Future(BadRequest(views.html.createClient(clients, formWithErrors)))
      },
      data => {
        logger.info("Sending: " + Json.toJson(data))
        clientUpdateConnector.createClient(data).map {
          case Right(clientResponse) => Redirect(routes.ClientRetrieveController.createClientConfirmation(
              clientResponse.clientId, clientResponse.clientSecret, helper.processClientCreateResponse(clientResponse)), FOUND)
          case error@_ => logger.error("There was an error in adding client : " + error.left.getOrElse("Unknown Error"))
            Ok(views.html.error())
        }
      }
    )
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
          clientRetrieveConnector.retrieveClientDetail(id) map {
            case Right(clientData) => BadRequest(views.html.clientDetail(clientData, formWithErrors, None))
            case error@_ => Ok(views.html.error())
          }
        },
        data => {
          logger.info("Sending: " + Json.toJson(data))
          clientUpdateConnector.updateClient(data).map {
            case Right(client) => Redirect(routes.ClientRetrieveController.retrieveClientDetail(client._id,
              Some(helper.processClientUpdateResponse(client))), FOUND)
            case error@_ => logger.error("There was an error in adding client : " + error.left.getOrElse("Unknown error"))
              Ok(views.html.error())
          }
        }
      )
  }

  def handleClient(error: Option[String],client: Option[Client]): Either[String,Client] ={
    (client,error) match {
      case (Some(model),None) => Right(model)
      case errorMessage@_ => logger.error("There was an error in adding client : " + errorMessage._2.getOrElse("Unknown error"))
        Left(errorMessage._2.getOrElse("Unknown error"))
    }
  }

}
