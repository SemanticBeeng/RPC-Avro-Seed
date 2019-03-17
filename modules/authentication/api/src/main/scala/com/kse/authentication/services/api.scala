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

package com.kse.authentication.services

import higherkindness.mu.rpc.protocol.{service, _}
import higherkindness.mu.rpc.protocol._
import shapeless.{:+:, CNil}

//@outputName("AuthenticationService")
//@outputPackage("com.kse.services.authentication.api")
//@option("java_multiple_files", true)
//@option("java_outer_classname", "AuthenticationApiProto")
object api {

  import com.kse.session.services.api._
  import com.kse.authentication.domain

  sealed trait AuthR extends Product with Serializable

  /**
   * "duplicate" with [[domain.AuthenticationError]]
   */
  @message
  case class AuthenticationError(reason: String) extends AuthR

  type ResponseT = Session :+: AuthenticationError :+: SystemError :+: CNil

  @message
  final case class Response(result: ResponseT) extends AuthR

  @service(Protobuf)
  trait AuthenticationService[F[_]]
      extends shared.algebra.AuthenticationServiceBase[F, api.Response] {

    def authenticate(email: String): F[api.Response]
  }
}
