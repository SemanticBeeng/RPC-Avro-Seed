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

package com.kse.services.authentication.app

import cats.effect._
import cats.syntax.flatMap._
import cats.syntax.functor._
import io.chrisdavenport.log4cats.Logger
import higherkindness.mu.rpc.server._
//
import com.kse.services.shared.app.ServerBoot
//
import scala.concurrent.ExecutionContext.Implicits.global

/**
 * Starts the service
 */
object ServerApp extends ServerProgram[IO] with IOApp {
  implicit val ce: ConcurrentEffect[IO] = IO.ioConcurrentEffect

  def run(args: List[String]): IO[ExitCode] = program(args)
}

/**
 * Runs the `(service) authentication`
 */
class ServerProgram[F[_]: Effect] extends ServerBoot[F] {

  import com.adrianrafo.seed.server.common.models._
  import com.kse.services.authentication.server.AuthenticationServiceHandler
  //
  import com.kse.services.authentication.api._

  override def serverProgram(
      config: ServerConfig)(implicit L: Logger[F], CE: ConcurrentEffect[F]): F[ExitCode] = {

    implicit val PS: AuthenticationServiceHandler[F] = new AuthenticationServiceHandler[F]

    for {
      service  <- AuthenticationService.bindService[F]
      server   <- GrpcServer.default[F](config.port, List(AddService(service)))
      _        <- L.info(s"${config.name} - Starting server at ${config.host}:${config.port}")
      exitCode <- GrpcServer.server(server).as(ExitCode.Success)
    } yield exitCode
  }
}
