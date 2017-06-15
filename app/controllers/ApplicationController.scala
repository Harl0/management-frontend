package controllers

import com.google.inject.Inject
import config.AppConfig._
import models.Client
import models.Forms.{clientForm, clients}
import play.api.i18n.I18nSupport
import play.api.libs.ws.WSClient
import play.api.mvc.{Action, _}
import utils.Constants._

import scala.collection.mutable.{Map => MutableMap}
import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global

/**
  * Created by jason on 24/05/17.
  */

class ApplicationController @Inject()(ws: WSClient) extends InjectedController with I18nSupport {

  /**
    * GET   /createClient
    */
  def createClient() = Action { implicit request: RequestHeader => Ok(views.html.signIn(clients, clientForm))
  }

  /**
    * POST  /postCreateClient
    */
  def postCreateClient = Action { implicit request =>
    clientForm.bindFromRequest.fold(
      formWithErrors => {
        BadRequest(views.html.signIn(clients, formWithErrors))
      },
      data => {
        createClient(data)
      }
    )
  }

  def createClient(data: Client)(implicit req: RequestHeader): Result = {
    //TODO call client-private passing params to create API
    Ok(s"${data.contactname}, ${data.contactDetails}, ${data.department_name}, ${data.serviceStartDate}, ${data.redirectURIs}, ${data.imageURIs}")
  }


  /**
    *
    * @param user
    * @return Redirect to redirect_uri (test harness) including code as query param
    */
  def buildResponse(user: String)(implicit req: RequestHeader): Result = {
    checkAuthorisation(user) match {
      case true => {
        val queryStringMap: MutableMap[String, Seq[String]] = MutableMap("redirect_uri" -> Seq(req.session.get("redirect_uri").get))
        queryStringMap.put("code", Seq(generateAuthCode(user)))
        if (req.session.get("state").isDefined) {
          queryStringMap.put("state", Seq(req.session.get("state").get))
        }
        if (req.session.get("ui_locales").isDefined) {
          queryStringMap.put("ui_locales", Seq(req.session.get("ui_locales").get))
        }
        Redirect(s"$clientUrl/grant", queryStringMap.toMap, FOUND).clearingLang
      }
      case false => {
        val queryStringMap: MutableMap[String, Seq[String]] = MutableMap("error" -> Seq("access_denied"))
        queryStringMap.put("error_description", Seq("User failed to Authenticate"))
        if (req.session.get("state").isDefined) {
          queryStringMap.put("state", Seq(req.session.get("state").get))
        }
        Redirect(req.session.get("redirect_uri").get, queryStringMap.toMap, FOUND).clearingLang
      }
    }
  }

  def checkAuthorisation(user: String): Boolean = if (user != unauthorisedUser) true else false

  /**
    *
    * @param user
    * @return authorisation code (code) to be provided to token endpoint
    */
  def generateAuthCode(user: String): String = {
    user match {
      case `agentAdmin` => `agentAdminAuthCode`
      case `orgAdmin` => `orgAdminAuthCode`
      case `agentAssistant` => `agentAssistantAuthCode`
      case `orgAssistant` => `orgAssistantAuthCode`
      case `individual` => `individualAuthCode`
      case `nativeBAS` => `nativeBASAuthCode`
      case _ => `unauthorisedAuthCode`
    }
  }
}
