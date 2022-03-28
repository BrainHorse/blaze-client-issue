package com.evolutiongaming.watchdog

import io.circe._
import io.circe.generic.semiauto._

final case class SecondsResponse(seconds: String)

object SecondsResponse {
  implicit val tCodec: Codec[SecondsResponse] = deriveCodec
}
