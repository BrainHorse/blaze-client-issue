val Http4sVersion  = "0.23.8"
val CirceVersion   = "0.14.1"
val JawnVersion    = "2.2.0"
lazy val root = (project in file("."))
  .settings(
    organization := "dev.mikhailov.am",
    name := "blaze-client-issue",
    version := "0.0.1-SNAPSHOT",
    scalaVersion := "2.13.8",
    libraryDependencies ++= Seq(
      "org.http4s"    %% "http4s-blaze-server" % Http4sVersion,
      "org.http4s"    %% "http4s-blaze-client" % Http4sVersion,
      "org.http4s"    %% "http4s-circe"        % Http4sVersion,
      "org.http4s"    %% "http4s-dsl"          % Http4sVersion,
      "io.circe"      %% "circe-generic"       % CirceVersion,
      "org.typelevel" %% "jawn-fs2"            % JawnVersion,
    ),
    addCompilerPlugin(
      "org.typelevel" %% "kind-projector" % "0.13.2" cross CrossVersion.full,
    ),
    addCompilerPlugin("com.olegpy" %% "better-monadic-for" % "0.3.1"),
  )
