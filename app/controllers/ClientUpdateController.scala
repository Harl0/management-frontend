package controllers

import com.google.inject.Inject
import com.typesafe.scalalogging.LazyLogging
import config.AppConfig
import connectors.{ClientRetrieveConnector, ClientUpdateConnector}
import helpers.ClientHelper
import models.Client
import models.ClientForm._
import models.Log._
import play.api.i18n.{I18nSupport, MessagesApi}
import play.api.libs.json._
import play.api.libs.ws.WSClient
import play.api.mvc._
import utils.Constants._

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

/**
  * Created by jason on 24/05/17.
  */

class ClientUpdateController @Inject()(cc: ControllerComponents,
                                       clientUpdateConnector: ClientUpdateConnector,
                                       clientRetrieveConnector: ClientRetrieveConnector,
                                       helper: ClientHelper)
  extends AbstractController(cc) with I18nSupport with LazyLogging {

  /**
    * POST  /client/create
    */

  def postCreateClient: Action[AnyContent] = Action.async { implicit request =>
    val headers = Seq(
      s"method=${request.method}",
      s"""uri="${request.uri}"""",
      s"""remote_address=${request.remoteAddress}"""
    ).mkString(", ")
    logger.info(HTTPRequestReceived(CLIENT_UPDATE_REQUEST, headers))
    clientRegistrationForm.bindFromRequest.fold(
      formWithErrors => {
        Future(BadRequest(views.html.createClient(clients, formWithErrors)))
      },
      data => {
        logger.info(HTTPPOSTRequestReceived(CLIENT_CREATE_REQUEST, request.body.toString))
        clientUpdateConnector.createClient(data).map {
          case Right(clientResponse) => Redirect(routes.ClientRetrieveController.createClientConfirmation(
              clientResponse.clientId, clientResponse.clientSecret, helper.processClientCreateResponse(clientResponse)), FOUND)
          case error@_ =>
            Ok(views.html.error(error.left.getOrElse(DEFAULT_ERROR),HOME_PAGE))
        }
      }
    )
  }

  /**
    * POST   /client/update
    */

  def postUpdateClient: Action[AnyContent] = Action.async {
    implicit request =>
      val headers = Seq(
        s"method=${request.method}",
        s"""uri="${request.uri}"""",
        s"""remote_address=${request.remoteAddress}"""
      ).mkString(", ")
      logger.info(HTTPRequestReceived(CLIENT_UPDATE_REQUEST, headers))
      clientViewForm.bindFromRequest.fold(
        formWithErrors => {
          val id = request.body.asFormUrlEncoded.collect {
            case s => s("_id").head
          }.getOrElse("")
          clientRetrieveConnector.retrieveClientDetail(id) map {
            case Right(clientData) => BadRequest(views.html.clientDetail(clientData, formWithErrors, None))
            case error@_ =>
              Ok(views.html.error(error.left.getOrElse(DEFAULT_ERROR),CLIENT_LIST_PAGE))
          }
        },
        data => {
          logger.info(HTTPPOSTRequestReceived(CLIENT_UPDATE_REQUEST, request.body.toString))
          clientUpdateConnector.updateClient(data).map {
            case Right(client) => Redirect(routes.ClientRetrieveController.retrieveClientDetail(client._id,
              Some(helper.processClientUpdateResponse(client))), FOUND)
            case error@_ =>
              Ok(views.html.error(error.left.getOrElse(DEFAULT_ERROR),CLIENT_LIST_PAGE))
          }
        }
      )
  }

  //TODO refine case matching code for all controller methods
//  def handleClient(error: Option[String],client: Option[Client]): Either[String,Client] ={
//    (client,error) match {
//      case (Some(model),None) => Right(model)
//      case errorMessage@_ => logger.error("There was an error in adding client : " + errorMessage._2.getOrElse("Unknown error"))
//        Left(errorMessage._2.getOrElse("Unknown error"))
//    }
//  }

}
