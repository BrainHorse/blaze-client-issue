package com.evolutiongaming.watchdog

import cats.effect._
import org.http4s.implicits._
import org.typelevel.jawn.fs2._
import io.circe.jawn.CirceSupportParser
import org.typelevel.jawn.Facade
import org.http4s.Status.Successful
import scala.concurrent.duration._
import org.http4s.Request
import org.http4s.Method
import fs2.Stream
import io.circe.Json
import org.http4s.blaze.client.BlazeClientBuilder

object TestClient extends IOApp {

  implicit val facade: Facade[Json] = new CirceSupportParser(None, false).facade

  override def run(args: List[String]): IO[ExitCode] = {
    BlazeClientBuilder[IO]
      .withIdleTimeout(5.hours)
      .withConnectTimeout(5.seconds)
      .withMaxTotalConnections(16)
      .withRequestTimeout(10.seconds)
      .resource
      .use { client =>
        val request =
          Request[IO](Method.GET, uri"http://localhost:8081/seconds")
        for {
          stream <- IO(client.stream(request))
          _ <- stream
            .flatMap {
              case Successful(resp) =>
                resp.body.chunks.parseJsonStream[Json]
              case failed =>
                Stream.eval(IO.println(s"failed response: $failed")) >>
                  Stream.empty
            }
            .evalTap { res =>
              IO.println(s"got response $res")
            }
            .compile
            .drain
        } yield ExitCode.Success
      }
  }
}
