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

package com.kse.authentication.services.client

import cats.effect._
import cats.syntax.applicative._
import com.kse.authentication.shared.domain
import com.kse.shared.services.client.ClientRPC
import io.chrisdavenport.log4cats.Logger
import io.grpc.{CallOptions, ManagedChannel}
//
import com.kse.session.{domain ⇒ sess_domain}
import com.kse.authentication.services.api
import com.kse.authentication.services.shared

//
import scala.concurrent.ExecutionContext
import scala.concurrent.duration.FiniteDuration

object AuthenticationServiceClient {

  def apply[F[_]: Effect](clientF: F[api.AuthenticationService[F]])(
      implicit L: Logger[F]): shared.algebra.AuthenticationService[F] =
    new shared.algebra.AuthenticationService[F] {

      override def authenticate(email: String): F[Either[domain.Error, sess_domain.Session]] = ???
    }

  def createClient[F[_]: ContextShift](
      hostname: String,
      port: Int,
      sslEnabled: Boolean = true,
      tryToRemoveUnusedEvery: FiniteDuration,
      removeUnusedAfter: FiniteDuration)(
      implicit L: Logger[F],
      F: ConcurrentEffect[F],
      TM: Timer[F],
      EC: ExecutionContext): fs2.Stream[F, shared.algebra.AuthenticationService[F]] = {

    def fromChannel(channel: F[ManagedChannel]): Resource[F, api.AuthenticationService[F]] =
      api.AuthenticationService.clientFromChannel(channel, CallOptions.DEFAULT)

    def wrap(client: F[api.AuthenticationService[F]]): shared.algebra.AuthenticationService[F] =
      AuthenticationServiceClient(client)

    ClientRPC
      .clientCache(
        (hostname, port).pure[F],
        sslEnabled,
        tryToRemoveUnusedEvery,
        removeUnusedAfter,
        fromChannel)
      .map(cache => wrap(cache.getClient))
  }
}
