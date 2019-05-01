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

package com.kse.session.services.client

import cats.effect._
import cats.syntax.applicative._
import cats.syntax.flatMap._
import cats.syntax.functor._
import com.kse.shared.services.client.ClientRPC
import io.grpc.{CallOptions, ManagedChannel}
import io.chrisdavenport.log4cats.Logger
//
import com.kse.session.domain
import domain.{SessionId, TimeMs}
import com.kse.session.services.shared
import com.kse.session.services.api
//
import scala.concurrent.ExecutionContext
import scala.concurrent.duration.FiniteDuration

object SessionServiceClient {

  import com.kse.session.services.client.ApiMarshaller._

  def apply[F[_]: Effect](clientF: F[api.SessionService[F]])(
      implicit L: Logger[F]): shared.SessionService[F] =
    new shared.SessionService[F] {

      override def lookup(sessionId: SessionId): F[Either[domain.Error, domain.Session]] =
        for {
          client   ← clientF
          response ← client.lookup(sessionId)

        } yield response.result.map(ResponseMarshaller).unify

      override def expiresIn(sessionId: SessionId): F[TimeMs] = ???

      override def terminate(sessionId: SessionId): F[Unit] = ???
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
      EC: ExecutionContext): fs2.Stream[F, shared.SessionService[F]] = {

    def fromChannel(channel: F[ManagedChannel]): Resource[F, api.SessionService[F]] =
      api.SessionService.clientFromChannel(channel, CallOptions.DEFAULT)

    def wrap(client: F[api.SessionService[F]]): shared.SessionService[F] =
      SessionServiceClient(client)

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

//=== WIP ==
//class SessionServiceClient[F[_]: Effect] //(implicit F: ConcurrentEffect[F], EC: ExecutionContext)
//    extends ClientApiUnwrapper[F, shared.SessionService, api.SessionService] {
//
//  def apply(clientF: F[api.SessionService[F]])(implicit L: Logger[F]): shared.SessionService[F] =
//    new shared.SessionService[F] {
//
//      override def lookup(sessionId: SessionId): F[Either[domain.Error, domain.Session]] = ???
//
//      override def expiresIn(sessionId: SessionId): F[TimeMs] = ???
//
//      override def terminate(sessionId: SessionId): F[Unit] = ???
//    }
//
//  def fromChannel(channel: F[ManagedChannel]): Resource[F, api.SessionService[F]] =
//    api.SessionService.clientFromChannel(channel, CallOptions.DEFAULT)
//
//  def wrap(client: F[api.SessionService[F]]): shared.SessionService[F] =
//    new SessionServiceClient(client)
//
//}
//
//abstract class ClientApiUnwrapper[F[_]: Effect, SS[_[_]], AS[_[_]]] {
//
//  def fromChannel: F[ManagedChannel] ⇒ Resource[F, AS[F]]
//
//  def wrap: F[AS[F]] ⇒ SS[F]
//
//  def createClient(
//      hostname: String,
//      port: Int,
//      sslEnabled: Boolean = true,
//      tryToRemoveUnusedEvery: FiniteDuration,
//      removeUnusedAfter: FiniteDuration)(
//      implicit L: Logger[F],
//      F: ConcurrentEffect[F],
//      TM: Timer[F],
//      EC: ExecutionContext): fs2.Stream[F, SS[F]] = {
//
//    ClientRPC
//      .clientCache[F, AS](
//        (hostname, port).pure[F],
//        sslEnabled,
//        tryToRemoveUnusedEvery,
//        removeUnusedAfter,
//        fromChannel)
//      .map(cache => wrap(cache.getClient))
//  }
//
//}

//object Types {
//
//  abstract class MyInt2[F[_]: Effect[F]] extends (String ⇒ F[String])
//
//  def f[F]: MyInt2[ConcurrentEffect[F]] = s ⇒ println(s)
//
//  f.apply("1")
//
////  type createClientT[ = Function5[F[_] : Effect[F], SS[_[_]]] = (String, Int, Boolean, FiniteDuration, FiniteDuration)
////  (Logger[F], ConcurrentEffect[F], Timer[F], ExecutionContext) → fs2.Stream[F, SS[F]]
//
//}
