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

package com.kse.session.services.client

import cats.syntax.either._
import shapeless.Poly1

import com.kse.session.domain
import com.kse.session.services.api

/**
 * Marshals / maps `api` to `domain` types / language
 */
object ApiMarshaller {

  object ResponseMarshaller extends Poly1 {
    implicit val m1 =
      at[api.SessionNotFound](e ⇒ domain.SessionNotFound(e.id, "").asLeft[domain.Session])
    implicit val m2 = at[api.SystemError](e ⇒ domain.ErrorImpl("").asLeft[domain.Session])
    implicit val m3 =
      at[api.Session](s ⇒ domain.SessionProxy(s.id, s.createdAt, s.expiresIn).asRight[domain.Error])
  }
}
