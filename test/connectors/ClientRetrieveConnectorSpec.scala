package connectors

import akka.stream.Materializer
import config.AppConfig
import helpers.ErrorHelper
import mockws.{MockWS, Route}
import models.{Client, ClientRegistrationResponse}
import org.mockito.Matchers
import org.mockito.Mockito._
import org.scalatest.concurrent.ScalaFutures
import org.scalatest.mockito.MockitoSugar
import org.scalatestplus.play.PlaySpec
import org.scalatestplus.play.guice.GuiceOneAppPerTest
import play.api.libs.json.Json
import play.api.mvc.{Action, Results}

import scala.concurrent.Await
import scala.concurrent.duration._

class ClientRetrieveConnectorSpec extends PlaySpec
  with MockitoSugar with ScalaFutures with GuiceOneAppPerTest with Results {

  implicit lazy val materializer: Materializer = app.materializer
  val mockAppConfig = mock[AppConfig]
  val mockErrorHelper = mock[ErrorHelper]
  val defaultDuration = 5

  "retrieveClientDetail" should {
    "retrieve a client given a success is returned" in {

      val userRoute: Route = Route {
        case ("GET", u) => Action {
          Ok(Json.toJson(Client("chaz","dingle","berry","bob","cat")))
        }
      }

      def ageResponse(): Either[String, Client] = {
        // we initialize a mock WS with the defined route
        val ws = MockWS(userRoute)
        // we inject the MockWS into GatewayToTest
        val testedGateway = new ClientRetrieveConnector(ws, mockAppConfig, mockErrorHelper)
        try Await.result(testedGateway.retrieveClientDetail("chaz"),Duration.apply(defaultDuration, SECONDS))
        finally ws.close()
      }

      ageResponse mustBe Right(Client("chaz","dingle","berry","bob","cat"))
    }

    "return error given an error is returned" in {

      when(mockErrorHelper.non200Error(Matchers.any[Int],Matchers.any[String]))
        .thenReturn("fatal chaz dingle error")

      val userRoute: Route = Route {
        case ("GET", u) => Action {
          InternalServerError("dingle chaz")
        }
      }

      def ageResponse(): Either[String, Client] = {
        // we initialize a mock WS with the defined route
        val ws = MockWS(userRoute)
        // we inject the MockWS into GatewayToTest
        val testedGateway = new ClientRetrieveConnector(ws, mockAppConfig, mockErrorHelper)
        try Await.result(testedGateway.retrieveClientDetail("chaz"),Duration.apply(defaultDuration, SECONDS))
        finally ws.close()
      }

      ageResponse mustBe Left("fatal chaz dingle error")
    }

    //TODO test for exception

    }

  "retrieveClientList" should {
    "retrieve a client list given a success is returned" in {

      val userRoute: Route = Route {
        case ("GET", u) => Action {
          Ok(Json.toJson(Seq(Client("chaz","dingle","berry","bob","cat"))))
        }
      }

      def ageResponse(): Either[String, Seq[Client]] = {
        // we initialize a mock WS with the defined route
        val ws = MockWS(userRoute)
        // we inject the MockWS into GatewayToTest
        val testedGateway = new ClientRetrieveConnector(ws, mockAppConfig, mockErrorHelper)
        try Await.result(testedGateway.retrieveClientList,Duration.apply(defaultDuration, SECONDS))
        finally ws.close()
      }

      ageResponse mustBe  Right(List(Client("chaz","dingle","berry","bob","cat",None,None,None,None)))
    }

    "return error given an error is returned" in {

      when(mockErrorHelper.non200Error(Matchers.any[Int],Matchers.any[String]))
        .thenReturn("fatal chaz dingle error")

      val userRoute: Route = Route {
        case ("GET", u) => Action {
          InternalServerError("dingle chaz")
        }
      }

      def ageResponse(): Either[String, Seq[Client]] = {
        // we initialize a mock WS with the defined route
        val ws = MockWS(userRoute)
        // we inject the MockWS into GatewayToTest
        val testedGateway = new ClientRetrieveConnector(ws, mockAppConfig, mockErrorHelper)
        try Await.result(testedGateway.retrieveClientList,Duration.apply(defaultDuration, SECONDS))
        finally ws.close()
      }

      ageResponse mustBe Left("fatal chaz dingle error")
    }

    //TODO test for exception

  }

}

