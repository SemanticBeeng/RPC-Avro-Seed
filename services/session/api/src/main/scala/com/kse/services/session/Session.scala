package com.kse.services.session

import java.time.LocalDate

import higherkindness.mu.rpc.internal.encoders.avro.bigDecimalTagged._
import higherkindness.mu.rpc.internal.encoders.avro.javatime._
import higherkindness.mu.rpc.protocol._

trait api {

  case class Session(id: String, createdAt: LocalDate, expiresIn: Long)
}
