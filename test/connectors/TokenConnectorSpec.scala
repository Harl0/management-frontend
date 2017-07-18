//package connectors
//
//import akka.stream.Materializer
//import config.AppConfig
//import helpers.ErrorHelper
//import org.mockito.Mockito._
//import org.scalatest.concurrent.ScalaFutures
//import org.scalatest.mockito.MockitoSugar
//import org.scalatestplus.play.PlaySpec
//import org.scalatestplus.play.guice.GuiceOneAppPerTest
//import play.api.libs.json._
//import play.api.libs.ws.WSClient
//import play.api.mvc.{Results, _}
//import play.api.routing.sird._
//import play.api.test._
//import play.core.server.Server
//
//import scala.concurrent.Await
//import scala.concurrent.duration._
//
//class TokenConnectorSpec extends PlaySpec
//  with MockitoSugar with ScalaFutures with GuiceOneAppPerTest with Results {
//
//  implicit lazy val materializer: Materializer = app.materializer
//  val mockConnector = mock[TokenConnector]
//  val mockWSClient = mock[WSClient]
//  val mockAppConfig = mock[AppConfig]
//  val mockErrorHelper = mock[ErrorHelper]
//
//  "createTokenKeys" should {
//    "create keys given a success is returned" in {
//      Server.withRouter() {
//        case GET(p"/http://token.private/createKeys") => Action {
//          Results.Ok(Json.arr(Json.obj("full_name" -> "octocat/Hello-World")))
//        }
//      } { implicit port =>
//        WsTestClient.withClient { client =>
//          val result = Await.result(
//            new TokenConnector(mockWSClient, mockAppConfig, mockErrorHelper).createKeys, 10.seconds)
//          result mustBe "a"
//        }
//      }
//    }
//  }
//
//}
//
