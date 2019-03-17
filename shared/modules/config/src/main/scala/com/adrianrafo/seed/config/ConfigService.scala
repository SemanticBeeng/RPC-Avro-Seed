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

package com.adrianrafo.seed
package config

import com.typesafe.config.ConfigFactory
import cats.effect._
import cats.implicits._
import cats.syntax.either._
import pureconfig._
//import com.kse.shared.common.syntax._

trait ConfigService[F[_]] {

  def serviceConfig[Config](implicit reader: Derivation[ConfigReader[Config]]): F[Config]

}

object ConfigService {
  def apply[F[_]: Effect]: ConfigService[F] = new ConfigService[F] {

    override def serviceConfig[Config](
        implicit reader: Derivation[ConfigReader[Config]]): F[Config] =
      for {
        config <- Effect[F].delay(ConfigFactory.load()) //.resource #todo
        r ← Effect[F].fromEither(pureconfig
          .loadConfig[Config](config)
          .leftMap(e => new IllegalStateException(s"Error loading configuration: $e"))) //.resource
      } yield r

  }
}
