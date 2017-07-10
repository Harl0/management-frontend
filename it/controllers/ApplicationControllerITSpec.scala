package controllers

import helpers.IntegrationSpecBase
import org.scalatest.WordSpec

/**
  * Created by abhishek on 14/06/17.
  */
class ApplicationControllerITSpec extends WordSpec with IntegrationSpecBase {

  "Wiremock" should {
    "return a 200" in {
      lazy val res = buildGetClient("/client/create")
      whenReady(res) { response =>
        response.status.intValue() shouldBe 200
      }
    }
  }
}