import akka.stream.Materializer
import config.AppConfig
import connectors.{ClientRetrieveConnector, TokenConnector}
import controllers.{ClientUpdateController, HomeController}
import org.scalatest.concurrent.ScalaFutures
import org.scalatest.mockito.MockitoSugar
import org.scalatestplus.play.PlaySpec
import org.scalatestplus.play.guice.GuiceOneAppPerTest
import play.api.libs.ws.WSClient
import play.api.mvc.{ControllerComponents, Results}
import play.api.test.Helpers._
import play.api.test._

class HomeControllerSpec extends PlaySpec
  with MockitoSugar with ScalaFutures with GuiceOneAppPerTest with Results {

  implicit lazy val materializer: Materializer = app.materializer

  "home" should {
    "return the home page" in {

      val applicationController = new HomeController(stubControllerComponents())

      val action = applicationController.home().apply(FakeRequest())

      status(action) mustEqual OK

      whenReady(action) { res =>

      }
    }
  }


}

