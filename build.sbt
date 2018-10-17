import scala.collection.Seq

homepage in ThisBuild := Some(url("https://github.com/slamdata/tectonic"))

scmInfo in ThisBuild := Some(ScmInfo(
  url("https://github.com/slamdata/tectonic"),
  "scm:git@github.com:slamdata/tectonic.git"))

// Include to also publish a project's tests
lazy val publishTestsSettings = Seq(
  publishArtifact in (Test, packageBin) := true)

lazy val root = project
  .in(file("."))
  .settings(noPublishSettings)
  .aggregate(core, fs2, test)
  .enablePlugins(AutomateHeaderPlugin)

lazy val core = project
  .in(file("core"))
  .settings(name := "tectonic")
  .settings(
    performMavenCentralSync := false,
    publishAsOSSProject := true)
  .enablePlugins(AutomateHeaderPlugin)

lazy val fs2 = project
  .in(file("fs2"))
  .dependsOn(
    core,
    test % "test->test")
  .settings(name := "tectonic-fs2")
  .settings(
    performMavenCentralSync := false,
    publishAsOSSProject := true,

    libraryDependencies += "co.fs2" %% "fs2-core" % "1.0.0")
  .enablePlugins(AutomateHeaderPlugin)

lazy val test = project
  .in(file("test"))
  .dependsOn(core)
  .settings(name := "tectonic-test")
  .settings(
    performMavenCentralSync := false,
    publishAsOSSProject := true,

    libraryDependencies += "org.specs2" %% "specs2-core" % "4.3.4")
  .enablePlugins(AutomateHeaderPlugin)
