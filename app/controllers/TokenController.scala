package controllers

import com.google.inject.Inject
import com.typesafe.scalalogging.LazyLogging
import config.AppConfig
import connectors.{ClientRetrieveConnector, TokenConnector}
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

class TokenController @Inject()(cc: ControllerComponents, tokenConnector: TokenConnector)
  extends AbstractController(cc) with I18nSupport with LazyLogging {
  /**
    * GET   /token/createKeys
    */
  def createTokenKeys(): Action[AnyContent] = Action.async { implicit request: RequestHeader =>
    tokenConnector.createKeys.map {
      case Right(success) => Ok(views.html.tokenCreateKeys())
      case Left(error) =>logger.error("There was an error in creating a token : " + error)
        Ok(views.html.error(error,HOME_PAGE))
    }
  }
}
