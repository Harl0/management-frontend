package helpers

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.model.{HttpMethods, HttpRequest, HttpResponse, RequestEntity}
import akka.stream.ActorMaterializer
import com.github.tomakehurst.wiremock.WireMockServer
import com.github.tomakehurst.wiremock.client.WireMock
import com.github.tomakehurst.wiremock.client.WireMock._
import com.github.tomakehurst.wiremock.core.WireMockConfiguration.wireMockConfig
import com.github.tomakehurst.wiremock.stubbing.StubMapping
import org.scalatest.Suite
import org.scalatest.concurrent.{Eventually, IntegrationPatience}
import org.scalatestplus.play.guice.GuiceOneServerPerSuite

import scala.concurrent.Future

trait WiremockHelper extends Eventually with IntegrationPatience {
  self: GuiceOneServerPerSuite =>
  implicit val system = ActorSystem("my-system")
  implicit val materializer = ActorMaterializer()

  val wiremockPort = 1111
  val wiremockHost = "localhost"
  val wiremockURL = s"http://$wiremockHost:$wiremockPort"

  def stubGet(url: String, status: Integer, body: String): StubMapping = {
    stubFor(get(urlMatching(url))
      .willReturn(
        aResponse().
          withStatus(status).
          withBody(body)
      )
    )
  }

  def stubPost(url: String, status: Integer, responseBody: String): StubMapping =
    stubFor(post(urlMatching(url))
      .willReturn(
        aResponse().
          withStatus(status).
          withBody(responseBody)
      )
    )

  def buildGetClient(path: String): Future[HttpResponse] = Http().singleRequest(
    HttpRequest(
      HttpMethods.GET,
      uri = s"http://localhost:$port$path")
  )

  def buildPostClient(path: String, entity: RequestEntity): Future[HttpResponse] = Http().singleRequest(
    HttpRequest(
      HttpMethods.POST,
      uri = s"http://localhost:$port$path",
      entity = entity)
  )

  lazy val wmConfig = wireMockConfig().port(wiremockPort)
  lazy val wireMockServer = new WireMockServer(wmConfig)

  def startWiremock(): Unit = {
    wireMockServer.start()
    WireMock.configureFor(wiremockHost, wiremockPort)
  }

  def stopWiremock(): Unit = wireMockServer.stop()

  def resetWiremock(): Unit = WireMock.reset()
}
