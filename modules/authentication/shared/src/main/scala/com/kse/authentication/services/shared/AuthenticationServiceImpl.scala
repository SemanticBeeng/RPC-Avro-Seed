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

import cats.effect._
import cats.syntax.functor._
import cats.syntax.flatMap._
//import cats.syntax.apply._
//
import com.kse.authentication.domain
import com.kse.session.{domain ⇒ sesson_domain}

//
import io.chrisdavenport.log4cats.Logger

/**
 * Core implementation, free of transport protocol concerns (like protobuf, Avro)
 * Can be tested in isolation from remote execution / calls.
 */
class AuthenticationServiceImpl[F[_]: Sync](implicit L: Logger[F])
    extends algebra.AuthenticationService[F] {

  override def authenticate(email: String): F[Either[domain.Error, sesson_domain.Session]] = ???
}
