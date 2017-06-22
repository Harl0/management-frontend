
name := "client-frontend"

lazy val root = (project in file(".")).enablePlugins(PlayScala)
scalaVersion := "2.12.2"

libraryDependencies ++= Seq(
  "org.mockito" % "mockito-core" % "1.9.0" % "test",
  "org.jsoup" % "jsoup" % "1.10.2" % "test",
  "com.github.tomakehurst" % "wiremock" % "2.5.1" % "test",
  "org.scalatestplus.play" %% "scalatestplus-play" % "3.0.0-RC1" % "test",
  ws,
  guice
)

//SCoverage
coverageEnabled := false
coverageMinimum := 80
coverageFailOnMinimum := false

assemblyJarName in assembly := "client-frontend.jar"

mainClass in assembly := Some("play.core.server.ProdServerStart")
fullClasspath in assembly += Attributed.blank(PlayKeys.playPackageAssets.value)

assemblyMergeStrategy in assembly := {
  case "play/reference-overrides.conf" => MergeStrategy.concat
  case x => (assemblyMergeStrategy in assembly).value(x)
}
