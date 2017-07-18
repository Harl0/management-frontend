package controllers

import com.google.inject.Inject
import com.typesafe.scalalogging.LazyLogging
import connectors.TokenConnector
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
    tokenConnector.createKeys.map {
      case Right(success) => Ok(views.html.tokenCreateKeys())
      case error@_ =>logger.error("There was an error in creating a token : " + error)
        Ok(views.html.error(error.left.getOrElse(DEFAULT_ERROR),HOME_PAGE))
    }
  }
}
