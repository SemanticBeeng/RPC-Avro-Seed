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

package com.kse.authentication.services.shared

import cats.effect.Sync
import cats.tagless._
//import freestyle.free._
//import freestyle.tagless._
//
import com.kse.authentication.domain
import com.kse.session.{domain ⇒ sesson_domain}
import io.chrisdavenport.log4cats.Logger

object algebra {

  /**
   *
   * @tparam R "response" coproduct
   */
  //@free
  //@finalAlg @autoFunctor
  trait AuthenticationServiceBase[F[_], R] {

    //  type F[_]
    //  type R

    /**
     *
     */
    def authenticate(email: String): F[R]
    //def authenticate(email: String): FS[R]
  }

  //@free
  @finalAlg
  @autoFunctorK
  @autoSemigroupalK
  @autoProductNK
  trait AuthenticationService[F[_]]
      extends AuthenticationServiceBase[F, Either[domain.Error, sesson_domain.Session]] {

    def authenticate(email: String): F[Either[domain.Error, sesson_domain.Session]]
  }
}

object AuthenticationService {
  def apply[F[_]: Sync](implicit L: Logger[F]) = new AuthenticationServiceImpl[F]
}
