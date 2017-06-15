// Copyright (C) 2011-2012 the original author or authors.
// See the LICENCE.txt file distributed with this work for additional
// information regarding copyright ownership.
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
// http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

package controllers

import org.jsoup.Jsoup
import org.scalatest.{Matchers, WordSpec}
import org.scalatestplus.play.guice.GuiceOneServerPerSuite
import utils.WiremockHelper

import utils.Constants._

class ApplicationControllerSpec extends WordSpec with WiremockHelper with GuiceOneServerPerSuite with Matchers{

  "The sign in screen" should {
    "Have a title of sign in" in {
      val url = buildClient(s"/login?redirect_uri=http").get()
      val response = await(url)
      val document = Jsoup.parse(response.body)
      document.title() shouldBe "Sign In"
      document.getElementsByClass("heading-xlarge").text() shouldBe "Sign In"
      document.getElementById(formTitle).text() shouldBe signInPageFormTitleText
      document.getElementById(orgAssistant).attr("type") shouldBe "radio"
      document.getElementById(orgAssistantButtonText).text() shouldBe orgAssistantText
      document.getElementById(orgAdmin).attr("type") shouldBe "radio"
      document.getElementById(orgAdminButtonText).text() shouldBe orgAdminText
      document.getElementById(individual).attr("type") shouldBe "radio"
      document.getElementById(individualButtonText).text() shouldBe individualText
      document.getElementById(agentAdmin).attr("type") shouldBe "radio"
      document.getElementById(agentAdminButtonText).text() shouldBe agentAdminText
      document.getElementById(agentAssistant).attr("type") shouldBe "radio"
      document.getElementById(agentAssistantButtonText).text() shouldBe agentAssistantText
    }
  }
}
