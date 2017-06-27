package healthcontrollers

import com.google.inject.Inject
import play.api.mvc._

class HealthCheckController @Inject()(cc: ControllerComponents) extends AbstractController(cc) {

  def healthCheck: Action[AnyContent] = Action {
    Ok
  }

}
