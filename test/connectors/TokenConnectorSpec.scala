package connectors

import akka.stream.Materializer
import config.AppConfig
import helpers.ErrorHelper
import mockws.{MockWS, Route}
import org.mockito.Matchers
import org.mockito.Mockito._
import org.scalatest.concurrent.ScalaFutures
import org.scalatest.mockito.MockitoSugar
import org.scalatestplus.play.PlaySpec
import org.scalatestplus.play.guice.GuiceOneAppPerTest
import play.api.libs.ws.WSClient
import play.api.mvc.{Action, Results}

import scala.concurrent.Await
import scala.concurrent.duration._

class TokenConnectorSpec extends PlaySpec
  with MockitoSugar with ScalaFutures with GuiceOneAppPerTest with Results {

  implicit lazy val materializer: Materializer = app.materializer
  val mockAppConfig = mock[AppConfig]
  val mockErrorHelper = mock[ErrorHelper]
  val defaultDuration = 5

  "createTokenKeys" should {
    "create keys given a success is returned" in {

      val userRoute: Route = Route {
        case ("GET", u) => Action {
          Ok("result")
        }
      }
      def ageResponse(): Either[String, Boolean] = {
        // we initialize a mock WS with the defined route
        val ws = MockWS(userRoute)
        // we inject the MockWS into GatewayToTest
        val testedGateway = new TokenConnector(ws, mockAppConfig, mockErrorHelper)
        try Await.result(testedGateway.createKeys,Duration.apply(defaultDuration, SECONDS))
        finally ws.close()
      }

      ageResponse mustBe Right(true)
    }

    "return an error given a non 200 response" in {

      when(mockErrorHelper.non200Error(Matchers.any[Int],Matchers.any[String]))
        .thenReturn("fatal chaz dingle error")

      val userRoute: Route = Route {
        case ("GET", u) => Action {
          BadRequest("fatal chaz dingle error")
        }
      }
      def ageResponse(): Either[String, Boolean] = {
        // we initialize a mock WS with the defined route
        val ws = MockWS(userRoute)
        // we inject the MockWS into GatewayToTest
        val testedGateway = new TokenConnector(ws, mockAppConfig, mockErrorHelper)
        try Await.result(testedGateway.createKeys,Duration.apply(defaultDuration, SECONDS))
        finally ws.close()
      }

      ageResponse mustBe Left("fatal chaz dingle error")
    }

    //TODO test for exception

    }

}

