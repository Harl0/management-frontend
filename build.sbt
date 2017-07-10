import scoverage.ScoverageKeys

name := "management-frontend"

scalaVersion := "2.12.2"

scalaSource in IntegrationTest := baseDirectory.value / "it"

libraryDependencies ++= Seq(
  "com.github.tomakehurst" % "wiremock-standalone" % "2.6.0" % IntegrationTest,
  "org.mockito" % "mockito-core" % "1.9.0" % "test",
  "org.jsoup" % "jsoup" % "1.10.2" % "test",
  "org.scalactic" %% "scalactic" % "3.0.1" % "test, it",
  "org.scalatest" %% "scalatest" % "3.0.1" % "test, it",
  "com.typesafe.scala-logging" %% "scala-logging" % "3.5.0",
  "org.scalatestplus.play" %% "scalatestplus-play" % "3.0.0" % "test, it",
  ws,
  guice
)

//SCoverage
lazy val scoverageSettings = {
  Seq(
    ScoverageKeys.coverageMinimum := 80,
    ScoverageKeys.coverageFailOnMinimum := false,
    ScoverageKeys.coverageHighlighting := true
  )
}

lazy val root = (project in file(".")).enablePlugins(PlayScala)
  .configs(IntegrationTest)
  .settings(scoverageSettings ++ Defaults.itSettings: _*)

assemblyJarName in assembly := "management-frontend.jar"

mainClass in assembly := Some("play.core.server.ProdServerStart")
fullClasspath in assembly += Attributed.blank(PlayKeys.playPackageAssets.value)

assemblyMergeStrategy in assembly := {
  case "play/reference-overrides.conf" => MergeStrategy.concat
  case x => (assemblyMergeStrategy in assembly).value(x)
}
