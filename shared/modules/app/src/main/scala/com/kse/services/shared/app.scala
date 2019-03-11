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

package com.kse.services.shared

import cats.effect._
import cats.syntax.flatMap._
import cats.syntax.functor._
import io.chrisdavenport.log4cats.Logger
import io.chrisdavenport.log4cats.slf4j.Slf4jLogger

object app {

  abstract class ServerBoot[F[_]: Effect] {

    import com.adrianrafo.seed.config.ConfigService
    import com.adrianrafo.seed.server.common.models._

    def program(args: List[String])(implicit CE: ConcurrentEffect[F]): F[ExitCode] =
      for {
        config   <- ConfigService[F].serviceConfig[SeedServerConfig]
        logger   <- Slf4jLogger.fromName[F](config.server.name)
        exitCode <- serverProgram(config.server)(logger, CE)
      } yield exitCode

    def serverProgram(
        config: ServerConfig)(implicit L: Logger[F], CE: ConcurrentEffect[F]): F[ExitCode]
  }
}
