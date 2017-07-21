package controllers

import com.google.inject.Inject
import com.typesafe.scalalogging.LazyLogging
import config.AppConfig
import connectors.{CollectorRetrieveConnector, CollectorUpdateConnector}
import helpers.CollectorHelper
import models.Collector
import models.CollectorForm._
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

class CollectorUpdateController @Inject()(cc: ControllerComponents,
                                          collectorUpdateConnector: CollectorUpdateConnector,
                                          collectorRetrieveConnector: CollectorRetrieveConnector,
                                          helper: CollectorHelper)
  extends AbstractController(cc) with I18nSupport with LazyLogging {

  /**
    * POST  /client/create
    */

  def postCreateCollector: Action[AnyContent] = Action.async { implicit request =>
    val headers = Seq(
      s"method=${request.method}",
      s"""uri="${request.uri}"""",
      s"""remote_address=${request.remoteAddress}"""
    ).mkString(", ")
    logger.info(HTTPRequestReceived(COLLECTOR_UPDATE_REQUEST, headers))
    collectorRegistrationForm.bindFromRequest.fold(
      formWithErrors => {
        Future(BadRequest(views.html.createCollector(collectors, formWithErrors)))
      },
      data => {
        logger.info(HTTPPOSTRequestReceived(COLLECTOR_CREATE_REQUEST, request.body.toString))
        collectorUpdateConnector.createCollector(data).map {
          case Right(collectorResponse) => Redirect(routes.CollectorRetrieveController.createCollectorConfirmation(
            collectorResponse.collectorId, collectorResponse.collectorSecret, helper.processCollectorCreateResponse(collectorResponse)), FOUND)
          case error@_ =>
            Ok(views.html.error(error.left.getOrElse(DEFAULT_ERROR), HOME_PAGE))
        }
      }
    )
  }

  /**
    * POST   /client/update
    */

  def postUpdateCollector: Action[AnyContent] = Action.async {
    implicit request =>
      val headers = Seq(
        s"method=${request.method}",
        s"""uri="${request.uri}"""",
        s"""remote_address=${request.remoteAddress}"""
      ).mkString(", ")
      logger.info(HTTPRequestReceived(COLLECTOR_UPDATE_REQUEST, headers))
      collectorViewForm.bindFromRequest.fold(
        formWithErrors => {
          val id = request.body.asFormUrlEncoded.collect {
            case s => s("_id").head
          }.getOrElse("")
          collectorRetrieveConnector.retrieveCollectorDetail(id) map {
            case Right(collectorData) => BadRequest(views.html.collectorDetail(collectorData, formWithErrors, None))
            case error@_ =>
              Ok(views.html.error(error.left.getOrElse(DEFAULT_ERROR), COLLECTOR_LIST_PAGE))
          }
        },
        data => {
          logger.info(HTTPPOSTRequestReceived(COLLECTOR_UPDATE_REQUEST, request.body.toString))
          collectorUpdateConnector.updateCollector(data).map {
            case Right(collector) => Redirect(routes.CollectorRetrieveController.retrieveCollectorDetail(collector._id,
              Some(helper.processCollectorUpdateResponse(collector))), FOUND)
            case error@_ =>
              Ok(views.html.error(error.left.getOrElse(DEFAULT_ERROR), COLLECTOR_LIST_PAGE))
          }
        }
      )
  }
}
