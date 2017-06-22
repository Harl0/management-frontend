package controllers

import com.google.inject.Inject
import config.AppConfig
import models.Client._
import models.Forms.{clientForm, clients}
import models.{Client, ClientRegister}
import play.api.i18n.I18nSupport
import play.api.libs.json.Json
import play.api.libs.ws.WSClient
import play.api.mvc._

import scala.collection.mutable.{Map => MutableMap}
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

/**
  * Created by jason on 24/05/17.
  */

class ApplicationController @Inject()(ws: WSClient, config: AppConfig) extends InjectedController with I18nSupport {

  /**
    * GET   /createClient
    */
  def createClient() = Action { implicit request: RequestHeader =>
    Ok(views.html.addClient(clients, clientForm))
  }

  /**
    * POST  /postCreateClient
    */
  def postCreateClient = Action.async { implicit request =>
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
            Ok(views.html.addClientConfirmation(cred.get))
          }.recover {
          case e => Ok(views.html.error())
        }
      }
    )
  }
}
