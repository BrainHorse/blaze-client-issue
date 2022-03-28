package com.evolutiongaming.watchdog

import cats.effect._
import org.http4s._
import org.http4s.dsl.io._
import org.http4s.circe.CirceEntityEncoder._
import scala.concurrent.duration._
import fs2.Stream
import org.http4s.blaze.server.BlazeServerBuilder

object TestServer extends IOApp {

  def seconds = Stream
    .awakeEvery[IO](1.second)

  def streamRoute: HttpRoutes[IO] = {
    HttpRoutes.of[IO] { case GET -> Root / "seconds" =>
      val stream = seconds
        .map(s => SecondsResponse(s.toSeconds.toString))
        .evalTap(s => IO.println(s"emitting $s"))
      IO.println("started stream response") *> Ok(stream)
    }
  }

  override def run(args: List[String]): IO[ExitCode] =
    BlazeServerBuilder[IO]
      .withIdleTimeout(30.seconds)
      .withResponseHeaderTimeout(10.seconds)
      .bindHttp(port = 8081, host = "0.0.0.0")
      .withHttpApp(streamRoute.orNotFound)
      .withoutBanner
      .resource
      .use(_ => IO.never)
      .as(ExitCode.Success)

}
