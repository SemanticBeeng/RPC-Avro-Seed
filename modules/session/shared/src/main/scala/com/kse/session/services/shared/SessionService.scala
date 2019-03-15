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

package com.kse.session.services.shared

import com.kse.session.domain
import io.chrisdavenport.log4cats.Logger

object algebra {

  /**
   *
   * @tparam R "response" coproduct
   */
  trait SessionService[F[_], R] {

    /**
     *
     */
    def lookup(sessionId: domain.SessionId): F[R]

    /**
     *
     */
    def expiresIn(sessionId: domain.SessionId): F[domain.TimeMs]

    /**
     * @param sessionId After this call the session is guaranteed to not be usable anymore.
     */
    def terminate(sessionId: domain.SessionId): F[Unit]

  }
}

abstract class SessionService[F[_]]
    extends algebra.SessionService[F, Either[domain.Error, domain.Session]] {

  def lookup(sessionId: domain.SessionId): F[Either[domain.Error, domain.Session]]

  def expiresIn(sessionId: domain.SessionId): F[domain.TimeMs]

  def terminate(sessionId: domain.SessionId): F[Unit]

}

import cats.effect.Sync

object SessionService {
  def apply[F[_]: Sync](implicit L: Logger[F]) = new SessionServiceImpl[F]
}
