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

package com.kse.authentication.process.defs

import java.time.{Duration, Instant}

object tech {

  /**
   * The `Ubiq` `technical domain` `bounded context`
   */
  object ubiqu {

    case class InvocationHandle(value: String)
    trait Call {
      def handle: InvocationHandle
    }
  }
}

object domain {

  case class EndUserId(id: String)

  type Nonce         = sun.security.krb5.internal.crypto.Nonce
  type NonceReadable = String

  type AuthenticationAssetIdentifier = String

  trait AuthenticationChallenge {
    def created: Instant
    def expiry: Duration
  }

  trait AuthWithNonce {
    def nonce: Nonce
  }

  case class AssetPossessionChallenge(
      handle: tech.ubiqu.InvocationHandle,
      nonce: Nonce,
      nonceReadable: NonceReadable,
      created: Instant,
      expiry: Duration)
      extends AuthenticationChallenge
      with AuthWithNonce
      with tech.ubiqu.Call

  import com.kse.process.interactions

  trait AssetPossessionChallengeOutcome extends interactions.Outcome

  case class AssetPossessionChallengeProof(
      handle: tech.ubiqu.InvocationHandle,
      nonce: Nonce,
      assetId: AuthenticationAssetIdentifier)
      extends AuthWithNonce
      with AssetPossessionChallengeOutcome
      with tech.ubiqu.Call

  case class AssetPossessionChallengeExpiration(
      handle: tech.ubiqu.InvocationHandle,
      nonce: Nonce,
      expired: Instant)
      extends AuthWithNonce
      with AssetPossessionChallengeOutcome
      with tech.ubiqu.Call
}
