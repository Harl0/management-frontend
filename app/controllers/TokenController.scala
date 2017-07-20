package controllers

import com.google.inject.Inject
import com.typesafe.scalalogging.LazyLogging
import connectors.TokenConnector
import models.Log.{HTTPRequestReceived, processResponse}
import play.api.i18n.I18nSupport
import play.api.mvc._
import utils.Constants._

import scala.concurrent.ExecutionContext.Implicits.global

/**
  * Created by jason on 24/05/17.
  */

class TokenController @Inject()(cc: ControllerComponents, tokenConnector: TokenConnector)
  extends AbstractController(cc) with I18nSupport with LazyLogging {
  /**
    * GET   /token/createKeys
    */
  def createTokenKeys(): Action[AnyContent] = Action.async { implicit request: RequestHeader =>
    val headers = Seq(
      s"method=${request.method}",
      s"""uri="${request.uri}"""",
      s"""remote_address=${request.remoteAddress}"""
    ).mkString(", ")
    logger.info(HTTPRequestReceived(TOKEN_CREATE_REQUEST, headers))
    tokenConnector.createKeys.map {
      case Right(success) => Ok(views.html.tokenCreateKeys())
      case error@_ =>
        Ok(views.html.error(error.left.getOrElse(DEFAULT_ERROR),HOME_PAGE))
    }
  }
}
