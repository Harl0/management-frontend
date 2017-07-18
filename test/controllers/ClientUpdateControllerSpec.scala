package controllers

import akka.stream.Materializer
import connectors.{ClientRetrieveConnector, ClientUpdateConnector}
import helpers.ClientHelper
import models.{Client, ClientRegistrationResponse}
import org.apache.http.HttpStatus
import org.mockito.Matchers
import org.mockito.Mockito._
import org.scalatest.concurrent.ScalaFutures
import org.scalatest.mockito.MockitoSugar
import org.scalatestplus.play.PlaySpec
import org.scalatestplus.play.guice.GuiceOneAppPerTest
import play.api.mvc.Results
import play.api.test.CSRFTokenHelper._
import play.api.test.FakeRequest
import play.api.test.Helpers._

import scala.concurrent.Future

class ClientUpdateControllerSpec extends PlaySpec
  with MockitoSugar with ScalaFutures with GuiceOneAppPerTest with Results {

  implicit lazy val materializer: Materializer = app.materializer
  val mockUpdateConnector = mock[ClientUpdateConnector]
  val mockRetrieveConnector = mock[ClientRetrieveConnector]
  val mockHelper = mock[ClientHelper]

  "postCreateClient" should {
    "return the create client page" in {

      when(mockUpdateConnector.createClient(Matchers.any()))
        .thenReturn(Future.successful(Right(ClientRegistrationResponse.buildEmptyClientRegistrationResponse)))

      val clientRetrieveController = new ClientUpdateController(
        stubControllerComponents(),mockUpdateConnector,mockRetrieveConnector,mockHelper)

      val action = clientRetrieveController.postCreateClient().apply(
        FakeRequest().withFormUrlEncodedBody(
          "clientName" -> "chaz dingle",
          "redirect_uri" -> "/test"
        ).withCSRFToken
      )

      status(action) mustEqual 302

    }

    "return the error page given an error" in {

      when(mockUpdateConnector.createClient(Matchers.any()))
        .thenReturn(Future.successful(Left("bad error")))

      val clientRetrieveController = new ClientUpdateController(
        stubControllerComponents(),mockUpdateConnector,mockRetrieveConnector,mockHelper)

      val action = clientRetrieveController.postCreateClient().apply(
        FakeRequest().withFormUrlEncodedBody(
          "clientName" -> "chaz dingle",
          "redirect_uri" -> "/test"
        ).withCSRFToken
      )

      status(action) mustEqual 200
      assert(contentAsString(action).contains("bad error"))

    }
  }

  "postUpdateClient" should {
    "return the update client page" in {

      when(mockUpdateConnector.updateClient(Matchers.any[Client]))
        .thenReturn(Future.successful(Right(Client.buildEmptyClient)))

      val clientRetrieveController = new ClientUpdateController(
        stubControllerComponents(),mockUpdateConnector,mockRetrieveConnector,mockHelper)

      val action = clientRetrieveController.postCreateClient().apply(
        FakeRequest().withFormUrlEncodedBody(
          "clientName" -> "chaz dingle",
          "redirect_uri" -> "test"
        ).withCSRFToken
      )
      status(action) mustEqual 200
    }

    "return the error page given an error" in {

      when(mockUpdateConnector.updateClient(Matchers.any[Client]))
        .thenReturn(Future.successful(Left("bad error")))

      val clientRetrieveController = new ClientUpdateController(
        stubControllerComponents(),mockUpdateConnector,mockRetrieveConnector,mockHelper)

      val action = clientRetrieveController.postCreateClient().apply(
        FakeRequest().withFormUrlEncodedBody(
          "clientName" -> "chaz dingle",
          "redirect_uri" -> "test"
        ).withCSRFToken
      )

      status(action) mustEqual 200
      assert(contentAsString(action).contains("bad error"))
    }
  }

}

