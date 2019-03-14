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

package com.kse.shared.services.client

import cats.effect._
import cats.syntax.functor._
import fs2.Stream
import io.chrisdavenport.log4cats.Logger
import io.chrisdavenport.log4cats.slf4j.Slf4jLogger
//
import com.adrianrafo.seed.config.domain._
import com.adrianrafo.seed.config.ConfigService

abstract class ClientBoot[F[_]: Effect] {

  def program(
      args: List[String])(implicit TM: Timer[F], CE: ConcurrentEffect[F]): Stream[F, ExitCode] = {

    def setupConfig: F[ClientAppConfig] =
      ConfigService[F]
        .serviceConfig[ClientConfig]
        .map(config => ClientAppConfig(config, ClientParams.loadParams(config.name, args)))

    for {
      config   <- Stream.eval(setupConfig)
      logger   <- Stream.eval(Slf4jLogger.fromName[F](config.client.name))
      exitCode <- clientProgram(config)(logger, TM, CE)
    } yield exitCode
  }

  def clientProgram(config: ClientAppConfig)(
      implicit L: Logger[F],
      TM: Timer[F],
      F: ConcurrentEffect[F]): Stream[F, ExitCode]
}
