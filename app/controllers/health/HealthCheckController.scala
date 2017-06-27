package controllers.health

import play.api.mvc._

class HealthCheckController extends InjectedController {

  def healthCheck: Action[AnyContent] = Action { Ok }

}
