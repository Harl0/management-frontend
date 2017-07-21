package controllers

import akka.stream.Materializer
import connectors.{CollectorRetrieveConnector, CollectorUpdateConnector}
import helpers.CollectorHelper
import models.{Collector, CollectorRegistrationResponse}
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

class CollectorUpdateControllerSpec extends PlaySpec
  with MockitoSugar with ScalaFutures with GuiceOneAppPerTest with Results {

  implicit lazy val materializer: Materializer = app.materializer
  val mockUpdateConnector = mock[CollectorUpdateConnector]
  val mockRetrieveConnector = mock[CollectorRetrieveConnector]
  val mockHelper = mock[CollectorHelper]

  "postCreateClient" should {
    "return the create client page" in {

      when(mockUpdateConnector.createCollector(Matchers.any()))
        .thenReturn(Future.successful(Right(CollectorRegistrationResponse.buildEmptyCollectorRegistrationResponse)))

      val clientRetrieveController = new CollectorUpdateController(
        stubControllerComponents(),mockUpdateConnector,mockRetrieveConnector,mockHelper)

      val action = clientRetrieveController.postCreateCollector().apply(
        FakeRequest().withFormUrlEncodedBody(
          "clientName" -> "chaz dingle",
          "redirect_uri" -> "/test"
        ).withCSRFToken
      )

      status(action) mustEqual 302

    }

    "return the error page given an error" in {

      when(mockUpdateConnector.createCollector(Matchers.any()))
        .thenReturn(Future.successful(Left("bad error")))

      val clientRetrieveController = new CollectorUpdateController(
        stubControllerComponents(),mockUpdateConnector,mockRetrieveConnector,mockHelper)

      val action = clientRetrieveController.postCreateCollector().apply(
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

      when(mockUpdateConnector.updateCollector(Matchers.any[Collector]))
        .thenReturn(Future.successful(Right(Collector.buildEmptyCollector)))

      val clientRetrieveController = new CollectorUpdateController(
        stubControllerComponents(),mockUpdateConnector,mockRetrieveConnector,mockHelper)

      val action = clientRetrieveController.postCreateCollector().apply(
        FakeRequest().withFormUrlEncodedBody(
          "clientName" -> "chaz dingle",
          "redirect_uri" -> "test"
        ).withCSRFToken
      )
      status(action) mustEqual 200
    }

    "return the error page given an error" in {

      when(mockUpdateConnector.updateCollector(Matchers.any[Collector]))
        .thenReturn(Future.successful(Left("bad error")))

      val clientRetrieveController = new CollectorUpdateController(
        stubControllerComponents(),mockUpdateConnector,mockRetrieveConnector,mockHelper)

      val action = clientRetrieveController.postCreateCollector().apply(
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

