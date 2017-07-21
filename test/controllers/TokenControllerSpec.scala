package controllers

import akka.stream.Materializer
import org.mockito.Mockito._
import org.scalatest.concurrent.ScalaFutures
import org.scalatest.mockito.MockitoSugar
import org.scalatestplus.play.PlaySpec
import org.scalatestplus.play.guice.GuiceOneAppPerTest
import play.api.mvc.Results
import play.api.test.FakeRequest
import play.api.test.Helpers._

import scala.concurrent.Future

class TokenControllerSpec extends PlaySpec
  with MockitoSugar with ScalaFutures with GuiceOneAppPerTest with Results {

  implicit lazy val materializer: Materializer = app.materializer
  val mockConnector = mock[TokenConnector]

  "createTokenKeys" should {
    "create keys given a success is returned" in {

      when(mockConnector.createKeys)
        .thenReturn(Future.successful(Right(true)))

      val tokenController = new TokenController(stubControllerComponents(), mockConnector)

      val action = tokenController.createTokenKeys().apply(FakeRequest())

      status(action) mustEqual OK
    }

    "give an error given an error is returned" in {

      when(mockConnector.createKeys)
        .thenReturn(Future.successful(Left("bad error")))

      val tokenController = new TokenController(stubControllerComponents(), mockConnector)

      val action = tokenController.createTokenKeys().apply(FakeRequest())

      status(action) mustEqual OK
      assert(contentAsString(action).contains("bad error"))
    }

  }


}
