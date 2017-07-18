package connectors

import akka.stream.Materializer
import config.AppConfig
import helpers.ErrorHelper
import mockws.{MockWS, Route}
import models.ClientForm._
import models.ClientRegistrationResponse
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

  "createClient" should {
    "create client given a success is returned" in {

      val userRoute: Route = Route {
        case ("POST", u) => Action {
          Ok(Json.toJson(ClientRegistrationResponse("chaz","dingle","bob")))
        }
      }

      def ageResponse(): Either[String, ClientRegistrationResponse] = {
        // we initialize a mock WS with the defined route
        val ws = MockWS(userRoute)
        // we inject the MockWS into GatewayToTest
        val testedGateway = new ClientUpdateConnector(ws, mockAppConfig, mockErrorHelper)
        try Await.result(testedGateway.createClient(ClientRegistrationForm("chaz","dingle")),Duration.apply(defaultDuration, SECONDS))
        finally ws.close()
      }

      ageResponse mustBe Right(ClientRegistrationResponse("chaz","dingle","bob"))
    }

    "return error given an error is returned" in {

      when(mockErrorHelper.non200Error(Matchers.any[Int],Matchers.any[String]))
        .thenReturn("fatal chaz dingle error")

      val userRoute: Route = Route {
        case ("POST", u) => Action {
          InternalServerError("dingle chaz")
        }
      }

      def ageResponse(): Either[String, ClientRegistrationResponse] = {
        // we initialize a mock WS with the defined route
        val ws = MockWS(userRoute)
        // we inject the MockWS into GatewayToTest
        val testedGateway = new ClientUpdateConnector(ws, mockAppConfig, mockErrorHelper)
        try Await.result(testedGateway.createClient(ClientRegistrationForm("chaz","dingle")),Duration.apply(defaultDuration, SECONDS))
        finally ws.close()
      }

      ageResponse mustBe Left("fatal chaz dingle error")
    }

    //TODO test for exception

    }
//
//  "updateClient" should {
//    "create client given a success is returned" in {
//
//      val userRoute: Route = Route {
//        case ("POST", u) => Action {
//          Ok(Json.toJson(ClientRegistrationResponse("chaz","dingle","bob")))
//        }
//      }
//
//      def ageResponse(): Either[String, ClientRegistrationResponse] = {
//        // we initialize a mock WS with the defined route
//        val ws = MockWS(userRoute)
//        // we inject the MockWS into GatewayToTest
//        val testedGateway = new ClientUpdateConnector(ws, mockAppConfig, mockErrorHelper)
//        try Await.result(testedGateway.createClient(ClientRegistrationForm("chaz","dingle")),Duration.apply(defaultDuration, SECONDS))
//        finally ws.close()
//      }
//
//      ageResponse mustBe Right(ClientRegistrationResponse("chaz","dingle","bob"))
//    }
//
//    "return error given an error is returned" in {
//
//      when(mockErrorHelper.non200Error(Matchers.any[Int],Matchers.any[String]))
//        .thenReturn("fatal chaz dingle error")
//
//      val userRoute: Route = Route {
//        case ("POST", u) => Action {
//          InternalServerError("dingle chaz")
//        }
//      }
//
//      def ageResponse(): Either[String, ClientRegistrationResponse] = {
//        // we initialize a mock WS with the defined route
//        val ws = MockWS(userRoute)
//        // we inject the MockWS into GatewayToTest
//        val testedGateway = new ClientUpdateConnector(ws, mockAppConfig, mockErrorHelper)
//        try Await.result(testedGateway.createClient(ClientRegistrationForm("chaz","dingle")),Duration.apply(defaultDuration, SECONDS))
//        finally ws.close()
//      }
//
//      ageResponse mustBe Left("fatal chaz dingle error")
//    }
//
//    //TODO test for exception
//
//  }

}

