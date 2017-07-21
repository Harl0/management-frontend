package controllers
import com.google.inject.Inject
import com.typesafe.scalalogging.LazyLogging
import config.AppConfig
import connectors.CollectorRetrieveConnector
import helpers.CollectorHelper
import models.CollectorForm._
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

class CollectorRetrieveController @Inject()(cc: ControllerComponents,
                                            collectorConnector: CollectorRetrieveConnector)
  extends AbstractController(cc) with I18nSupport with LazyLogging {

  /**
    * GET   /client/create
    */
  def createCollector(): Action[AnyContent] = Action { implicit request: RequestHeader =>
    Ok(views.html.createCollector(collectors, collectorRegistrationForm))
  }

  /**
    * GET   /client/createConfirmation
    */
  def createCollectorConfirmation(collectorId: String, collectorSecret: String, createCollectorResponse: String):
  Action[AnyContent] = Action { implicit request: RequestHeader =>
    Ok(views.html.addCollectorConfirmation(collectorId, collectorSecret, createCollectorResponse))
  }

  /**
    * GET   /client/list
    */

  def retrieveCollectorList: Action[AnyContent] = Action.async {
    implicit request =>
      val headers = Seq(
        s"method=${request.method}",
        s"""uri="${request.uri}"""",
        s"""remote_address=${request.remoteAddress}"""
      ).mkString(", ")
      logger.info(HTTPRequestReceived(COLLECTOR_LIST_REQUEST, headers))
      collectorConnector.retrieveCollectorList.map {
        case Right(collectorList) => Ok(views.html.retrieveCollectors(collectorList))
        case error@_ =>
          Ok(views.html.error(error.left.getOrElse(DEFAULT_ERROR),HOME_PAGE))
      }
  }

  /**
    * GET   /client/detail
    */

  def retrieveCollectorDetail(_id: String, message: Option[String]): Action[AnyContent] = Action.async {
    implicit request: RequestHeader =>
      val headers = Seq(
        s"method=${request.method}",
        s"""uri="${request.uri}"""",
        s"""remote_address=${request.remoteAddress}"""
      ).mkString(", ")
      logger.info(HTTPRequestReceived(COLLECTOR_DETAIL_REQUEST, headers))
      collectorConnector.retrieveCollectorDetail(_id).map {
        case Right(collectorDetails) => Ok(views.html.collectorDetail(collectorDetails, collectorViewForm, message))
        case error@_ =>
          Ok(views.html.error(error.left.getOrElse(DEFAULT_ERROR),COLLECTOR_LIST_PAGE))
      }
  }

  /**
    * GET   /client/delete
    */
  def deleteCollector(_id: String): Action[AnyContent] = Action.async {
    implicit request =>
      val headers = Seq(
        s"method=${request.method}",
        s"""uri="${request.uri}"""",
        s"""remote_address=${request.remoteAddress}"""
      ).mkString(", ")
      logger.info(HTTPRequestReceived(COLLECTOR_DELETE_REQUEST, headers))
      collectorConnector.deleteCollector(_id).map {
        case Right(success) => Ok
        case error@_ =>
          Ok(views.html.error(error.left.getOrElse(DEFAULT_ERROR),COLLECTOR_LIST_PAGE))
      }
  }

}
