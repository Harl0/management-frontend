package helpers

import akka.http.scaladsl.model.HttpResponse
import org.scalatest._
import org.scalatest.concurrent.{Eventually, IntegrationPatience, ScalaFutures}
import org.scalatestplus.play.guice.GuiceOneServerPerSuite
import play.api.inject.guice.GuiceApplicationBuilder
import play.api.{Application, Environment, Mode}

import scala.concurrent.duration.Duration
import scala.concurrent.{Await, Future}

trait IntegrationSpecBase extends WordSpec
  with GivenWhenThen with TestSuite with ScalaFutures with IntegrationPatience with Matchers
  with WiremockHelper
  with GuiceOneServerPerSuite
  with BeforeAndAfterEach with BeforeAndAfterAll with Eventually {

  def config: Map[String, String] = Map(
    "client.url" -> s"$wiremockURL"
  )

  override implicit lazy val app: Application = new GuiceApplicationBuilder()
    .in(Environment.simple(mode = Mode.Dev))
    .configure(config)
    .build

  def statusOf(res: Future[HttpResponse])(implicit timeout: Duration): Int = Await.result(res, timeout).status.intValue()

  override def beforeEach(): Unit = {
    resetWiremock()
  }

  override def beforeAll(): Unit = {
    super.beforeAll()
    startWiremock()
  }

  override def afterAll(): Unit = {
    stopWiremock()
    super.afterAll()
  }
}