package controllers

import com.google.inject.Inject
import com.typesafe.scalalogging.LazyLogging
import play.api.i18n.I18nSupport
import play.api.mvc.{Action, AnyContent, RequestHeader, _}
/**
  * Created by yang on 14/07/17.
  */
class HomeController @Inject()(cc: ControllerComponents)
  extends AbstractController(cc) with I18nSupport with LazyLogging {

  def home(): Action[AnyContent] = Action { implicit request: RequestHeader =>
    Ok(views.html.home())
  }

}
