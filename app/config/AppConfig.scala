package config

import com.typesafe.config.{Config, ConfigFactory}

class AppConfig {
  val config: Config = ConfigFactory.load()
  lazy val clientUrl: String = config.getString("client.url")
}
