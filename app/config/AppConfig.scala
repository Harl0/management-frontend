package config

import com.typesafe.config.{Config, ConfigFactory}

trait AppConfig{
  val config: Config
  lazy val clientUrl: String = config.getString("client.url")
}

object AppConfig extends AppConfig{
  lazy val config: Config = ConfigFactory.load()
}
