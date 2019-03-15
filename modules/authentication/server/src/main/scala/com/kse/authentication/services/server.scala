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

package com.kse.authentication.services

import cats.effect._
import cats.syntax.functor._
import cats.syntax.flatMap._
//
import com.kse.authentication.services.shared.AuthenticationService
import com.kse.session.services.{api ⇒ session_api}
import com.kse.session.{domain ⇒ sesson_domain}

//
import io.chrisdavenport.log4cats.Logger
//
object server {

  import shapeless.{Coproduct, _}
  import syntax.typeable._

  class AuthenticationServiceHandler[F[_]: Sync](implicit L: Logger[F])
      extends api.AuthenticationService[F] {

    def authenticate(email: String): F[api.Response] =
      AuthenticationService[F].authenticate(email).map {
        case Left(e /*: domain.Error*/ ) ⇒
          //L.error(s"lookup($sessionId) error ${e.msg}") >>
          Coproduct[api.ResponseT](api.AuthenticationError("#todo"))

        case Right(s: sesson_domain.Session) ⇒
          s.cast[session_api.Session]
            .map(Coproduct[api.ResponseT](_))
            .getOrElse(Coproduct[api.ResponseT](session_api.SystemError(s"Failed to cast")))

      } flatMap { r ⇒
        L.info(s"authenticate($email) ").as(api.Response(r))
      }
  }
}
