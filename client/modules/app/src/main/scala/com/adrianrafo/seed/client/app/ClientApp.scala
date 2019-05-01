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

package com.adrianrafo.seed.client
package app

import cats.effect._
import com.adrianrafo.seed.config.domain._
import com.adrianrafo.seed.client.process.runtime.PeopleServiceClient
import com.kse.shared.services.client.ClientBoot
import fs2.Stream
import io.chrisdavenport.log4cats.Logger

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration._
import scala.language.postfixOps

class ClientProgram[F[_]: /*ConcurrentEffect: */ ContextShift] extends ClientBoot[F] {

  def peopleServiceClient(host: String, port: Int)(
      implicit L: Logger[F],
      TM: Timer[F],
      F: ConcurrentEffect[F]): Stream[F, PeopleServiceClient[F]] =
    PeopleServiceClient.createClient(
      host,
      port,
      sslEnabled = false,
      tryToRemoveUnusedEvery = 30 minutes,
      removeUnusedAfter = 1 hour)

  def clientProgram(config: ClientAppConfig)(
      implicit L: Logger[F],
      TM: Timer[F],
      F: ConcurrentEffect[F]): Stream[F, ExitCode] = {
    for {
      peopleClient <- peopleServiceClient(config.client.host, config.client.port)
      result       <- Stream.eval(peopleClient.getPerson(config.params.request))
    } yield result.fold(_ => ExitCode.Error, _ => ExitCode.Success)
  }
}

object ClientApp extends IOApp {
  implicit val ce: ConcurrentEffect[IO] = IO.ioConcurrentEffect

  def run(args: List[String]): IO[ExitCode] =
    new ClientProgram[IO].program(args).compile.toList.map(_.headOption.getOrElse(ExitCode.Error))
}
