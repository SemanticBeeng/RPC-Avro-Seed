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

import java.net.InetAddress

import cats.effect._
import cats.syntax.flatMap._
import higherkindness.mu.rpc.ChannelForAddress
import higherkindness.mu.rpc.channel.cache.ClientCache
import higherkindness.mu.rpc.channel.cache.ClientCache.HostPort
import higherkindness.mu.rpc.channel.{ManagedChannelInterpreter, UsePlaintext}
import io.grpc.ManagedChannel

import scala.concurrent.ExecutionContext
import scala.concurrent.duration.FiniteDuration

/**
 * Utility to manage (cache, release, etc) RPC "client" references.
 */
object ClientRPC {

  def clientCache[F[_], Client[_[_]]](
      hostAndPort: F[HostPort],
      sslEnabled: Boolean,
      tryToRemoveUnusedEvery: FiniteDuration,
      removeUnusedAfter: FiniteDuration,
      fromChannel: F[ManagedChannel] => Resource[F, Client[F]])(
      implicit F: ConcurrentEffect[F],
      TM: Timer[F],
      EC: ExecutionContext): fs2.Stream[F, ClientCache[Client, F]] = {

    def serviceClient(hostname: String, port: Int): Resource[F, Client[F]] = {

      val channel: F[ManagedChannel] =
        F.delay(InetAddress.getByName(hostname).getHostAddress).flatMap { ip =>
          val channelFor    = ChannelForAddress(ip, port)
          val channelConfig = if (!sslEnabled) List(UsePlaintext()) else Nil
          new ManagedChannelInterpreter[F](channelFor, channelConfig).build
        }

      fromChannel(channel)
    }

    ClientCache
      .fromResource[Client, F](
        hostAndPort,
        Function.tupled(serviceClient),
        tryToRemoveUnusedEvery,
        removeUnusedAfter
      )
  }

}
