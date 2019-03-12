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

import cats.effect.Sync
import cats.syntax.functor._

import io.chrisdavenport.log4cats.Logger

package object server {

  //import com.kse.services.session._
  import com.kse.services.session.shared._
  import shapeless.Coproduct

  class SessionServiceHandler[F[_]: Sync](implicit L: Logger[F]) extends api.SessionService[F] {

    /**
     *
     */
    def lookup(sessionId: domain.SessionId): F[api.Response] = {

      /**
       * Lookup event sourced entity
       */
      val session: domain.Session = null

      L.info(s"$sessionId lookup").as(api.Response(Coproduct(api.Session(sessionId, 100, 100))))
    }

    def expiresIn(sessionId: String): F[domain.TimeMs] = {

      /**
       * Lookup event sourced entity
       */
      val session: domain.Session = null

      L.info(s"$sessionId expiresIn").as(1000)
    }

    def terminate(sessionId: domain.SessionId): F[Unit] = {

      /**
       * Lookup event sourced entity
       */
      val session: domain.Session = null

      L.info(s"$sessionId terminated ").as(Unit)
    }
  }
}
