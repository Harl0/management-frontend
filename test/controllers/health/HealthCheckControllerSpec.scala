package controllers.health

import org.scalatestplus.play.PlaySpec
import org.scalatestplus.play.guice.GuiceOneAppPerTest
import play.api.test.Helpers._
import play.api.test._


class HealthCheckControllerSpec extends PlaySpec with GuiceOneAppPerTest {

  "The healthcheck endpoint" must {
    "respond with a 200 status code when the service is OK" in {
      status(route(app, FakeRequest("GET", "/healthcheck")).get) must be (200)
    }
  }


}
