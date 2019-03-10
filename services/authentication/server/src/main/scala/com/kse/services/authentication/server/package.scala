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

package com.kse.services.authentication

import cats.effect.Sync
import cats.syntax.functor._
import io.chrisdavenport.log4cats.Logger
import com.kse.services.authentication.api._
import com.kse.services.session.api.Session

package object server {

  class AuthenticationServiceHandler[F[_]: Sync](implicit L: Logger[F])
      extends AuthenticationService[F] {

    override def authenticate(email: String): F[com.kse.services.session.api.Session] =
      //implicitly[_root_.io.grpc.MethodDescriptor.Marshaller[String]]
      L.info(s"authenticate").as(Session("id", 1000L, 100L))
  }
}
