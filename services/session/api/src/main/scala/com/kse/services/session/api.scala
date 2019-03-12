/*
 * Copyright 2017-2019 47 Degrees, LLC. <http://www.47deg.com>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.kse.services.session

import higherkindness.mu.rpc.internal.encoders.avro.bigDecimalTagged._
import higherkindness.mu.rpc.internal.encoders.avro.javatime._
import higherkindness.mu.rpc.protocol._

import shapeless.{:+:, CNil}

//@outputName("SessionService")
//@outputPackage("com.kse.services.session.api")
//@option("java_multiple_files", true)
//@option("java_outer_classname", "SessionApiProto")
object api {

  import com.kse.services.session.shared.domain

  sealed trait SessionR extends Product with Serializable

  @message
  final case class SessionNotFound(id: domain.SessionId) extends SessionR

  @message
  final case class Session(
      id: domain.SessionId,
      createdAt: domain.TimestampMs,
      expiresIn: domain.TimeMs)
      extends domain.Session
      with SessionR

  @message
  final case class Response(response: Session :+: SessionNotFound :+: CNil) extends SessionR

  @service(Protobuf)
  trait SessionService[F[_]] {

    /**
     *
     */
    def lookup(sessionId: domain.SessionId): F[Response]

    /**
     *
     */
    def expiresIn(sessionId: domain.SessionId): F[domain.TimeMs]

    /**
     * @param sessionId After this call the session is guaranteed to not be usable anymore.
     */
    def terminate(sessionId: domain.SessionId): F[Unit]
  }
}
