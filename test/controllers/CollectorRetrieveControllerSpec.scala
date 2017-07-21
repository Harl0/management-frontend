package controllers

import akka.stream.Materializer
import connectors.CollectorRetrieveConnector
import models.Collector
import org.mockito.Matchers
import org.mockito.Mockito._
import org.scalatest.concurrent.ScalaFutures
import org.scalatest.mockito.MockitoSugar
import org.scalatestplus.play.PlaySpec
import org.scalatestplus.play.guice.GuiceOneAppPerTest
import play.api.mvc.Results
import play.api.test.FakeRequest
import play.api.test.Helpers._
import play.api.test.CSRFTokenHelper._

import scala.concurrent.Future

class CollectorRetrieveControllerSpec extends PlaySpec
  with MockitoSugar with ScalaFutures with GuiceOneAppPerTest with Results {

  implicit lazy val materializer: Materializer = app.materializer
  val mockConnector = mock[CollectorRetrieveConnector]

  "retrieveClientList" should {
    "return the client list page" in {

      when(mockConnector.retrieveCollectorList)
        .thenReturn(Future.successful(Right(Collector.buildEmptyCollectorSeq)))

      val clientRetrieveController = new CollectorRetrieveController(stubControllerComponents(), mockConnector)

      val action = clientRetrieveController.retrieveCollectorList().apply(FakeRequest())

      status(action) mustEqual OK

    }

    "return the error page given an error is returned" in {

      when(mockConnector.retrieveCollectorList)
        .thenReturn(Future.successful(Left("bad error")))

      val clientRetrieveController = new CollectorRetrieveController(stubControllerComponents(), mockConnector)

      val action = clientRetrieveController.retrieveCollectorList().apply(FakeRequest())

      status(action) mustEqual OK
      assert(contentAsString(action).contains("bad error"))
    }
  }

  "retrieveClientDetail" should {
    "return the client detail page" in {

      when(mockConnector.retrieveCollectorDetail(Matchers.any[String]))
        .thenReturn(Future.successful(Right(Collector.buildEmptyCollector)))

      val clientRetrieveController = new CollectorRetrieveController(stubControllerComponents(), mockConnector)

      val action = clientRetrieveController.retrieveCollectorDetail("test",None).apply(FakeRequest().withCSRFToken)

      status(action) mustEqual OK

    }

    "return the error page given an error is returned" in {

      when(mockConnector.retrieveCollectorDetail(Matchers.any[String]))
        .thenReturn(Future.successful(Left("bad error")))

      val clientRetrieveController = new CollectorRetrieveController(stubControllerComponents(), mockConnector)

      val action = clientRetrieveController.retrieveCollectorDetail("test",None).apply(FakeRequest().withCSRFToken)

      status(action) mustEqual OK
      assert(contentAsString(action).contains("bad error"))
    }
  }

  "deleteClient" should {
    "return the client deleted page" in {

      when(mockConnector.deleteCollector(Matchers.any[String]))
        .thenReturn(Future.successful(Right(true)))

      val clientRetrieveController = new CollectorRetrieveController(stubControllerComponents(), mockConnector)

      val action = clientRetrieveController.deleteCollector("test").apply(FakeRequest().withCSRFToken)

      status(action) mustEqual OK

    }

    "return the error page given an error is returned" in {

      when(mockConnector.deleteCollector(Matchers.any[String]))
        .thenReturn(Future.successful(Left("bad error")))

      val clientRetrieveController = new CollectorRetrieveController(stubControllerComponents(), mockConnector)

      val action = clientRetrieveController.deleteCollector("test").apply(FakeRequest().withCSRFToken)

      status(action) mustEqual OK
      assert(contentAsString(action).contains("bad error"))
    }
  }


}

