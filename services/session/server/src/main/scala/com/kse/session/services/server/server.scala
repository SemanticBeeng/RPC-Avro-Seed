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

package com.kse.session.services

import cats.effect._
import cats.syntax.functor._
import cats.syntax.flatMap._
//import cats.syntax.apply._
//
import com.kse.session.domain
import com.kse.session.services.api
//
import io.chrisdavenport.log4cats.Logger

package object server {

  import com.kse.session.services.shared._
  import shapeless.{Coproduct, _}
  import syntax.typeable._

  //type ResponseT = api.Session :+: api.SessionNotFound :+: api.SystemError :+: CNil

  /**
   * Transport protocol wrapper for the core implementation
   */
  class SessionServiceHandler[F[_]: Sync](implicit F: ConcurrentEffect[F], L: Logger[F])
      extends api.SessionService[F] {

    /**
     *
     */
    def lookup(sessionId: domain.SessionId): F[api.Response] = {

      SessionService[F].lookup(sessionId).map {
        case Left(e /*: domain.Error*/ ) ⇒
          //L.error(s"lookup($sessionId) error ${e.msg}") >>
          Coproduct[api.ResponseT](api.SessionNotFound(sessionId))

        case Right(s: domain.Session) ⇒
          s.cast[api.Session]
            .map(Coproduct[api.ResponseT](_))
            .getOrElse(Coproduct[api.ResponseT](api.SystemError(s"Failed to cast")))

      } flatMap { r ⇒
        L.info(s"lookup($sessionId) ").as(api.Response(r))
      }
    }

    def expiresIn(sessionId: String): F[domain.TimeMs] = {

      for {
        r ← SessionService[F].expiresIn(sessionId)
        _ ← L.info(s"$sessionId expiresIn").as(r)
      } yield r
    }

    def terminate(sessionId: domain.SessionId): F[Unit] = {

      for {
        r ← SessionService[F].terminate(sessionId)
        _ ← L.info(s"$sessionId terminated ").as(r)
      } yield r
    }
  }

  /**
   * Core implementation, free of transport protocol concerns (like protobuf, Avro)
   * Can be tested in isolation from remote execution / calls.
   */
  private class SessionServiceImpl[F[_]: Sync](implicit L: Logger[F])
      extends com.kse.session.services.shared.SessionService[F] {

    /**
     *
     */
    def lookup(sessionId: domain.SessionId): F[Either[Error, domain.Session]] = {

      /**
       * Lookup event sourced entity
       */
      val session: domain.Session = null

      L.info(s"$sessionId lookup").as(Right(session))
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

      L.info(s"$sessionId terminated").as(Unit)
    }
  }

  object SessionService {
    def apply[F[_]: Sync](implicit L: Logger[F]): SessionService[F] = new SessionServiceImpl[F]
  }
}
