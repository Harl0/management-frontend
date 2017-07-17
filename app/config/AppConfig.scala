package config

import javax.inject.Singleton

import com.typesafe.config.{Config, ConfigFactory}

@Singleton
class AppConfig {
  val config: Config = ConfigFactory.load()
  lazy val appName: String = config.getString("app.name")
  lazy val clientUrl: String = config.getString("client.url")
  lazy val tokenUrl: String = config.getString("token.url")
}
