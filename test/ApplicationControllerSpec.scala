import akka.stream.Materializer
import config.AppConfig
import connectors.{ClientConnector, TokenConnector}
import controllers.ClientController
import org.scalatest.concurrent.ScalaFutures
import org.scalatest.mockito.MockitoSugar
import org.scalatestplus.play.PlaySpec
import org.scalatestplus.play.guice.GuiceOneAppPerTest
import play.api.libs.ws.WSClient
import play.api.mvc.{ControllerComponents, Results}
import play.api.test.Helpers._
import play.api.test._

class ApplicationControllerSpec extends PlaySpec
  with MockitoSugar with ScalaFutures with GuiceOneAppPerTest with Results {

  implicit lazy val materializer: Materializer = app.materializer
  val mockWsClient = mock[WSClient]
  val mockAppConfig = mock[AppConfig]
  val mockComponents = mock[ControllerComponents]
  val mockClientConnector = mock[ClientConnector]
  val mockTokenConnector = mock[TokenConnector]

  "home" should {
    "return the home page" in {

      val applicationController = new ClientController(
        mockWsClient, mockAppConfig, stubControllerComponents(), mockClientConnector, mockTokenConnector)

      val action = applicationController.home().apply(FakeRequest())

      status(action) mustEqual OK

      whenReady(action) { res =>

      }
    }
  }


}

