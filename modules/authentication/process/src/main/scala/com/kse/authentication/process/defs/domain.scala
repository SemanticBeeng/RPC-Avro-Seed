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

object domain {

  import com.kse.authentication._
  import com.kse.authentication.shared.tech

  trait AuthWithNonce {
    def nonce: shared.domain.Nonce
  }

  import com.kse.process.interactions

  trait AssetPossessionChallengeOutcome extends interactions.Outcome
  //trait NoOutcome                       extends Unit with AssetPossessionChallengeOutcome

  case class AssetPossessionChallenge(
      handle: tech.ubiqu.InvocationHandle,
      nonce: shared.domain.Nonce,
      nonceReadable: shared.domain.NonceReadable,
      created: Instant,
      expiry: Duration)
      extends shared.domain.AuthenticationChallenge
      with AuthWithNonce
      with AssetPossessionChallengeOutcome
      with tech.ubiqu.Call

  case class AssetPossessionChallengeProof(
      handle: tech.ubiqu.InvocationHandle,
      nonce: shared.domain.Nonce,
      assetId: shared.domain.AuthenticationAssetIdentifier)
      extends AuthWithNonce
      with AssetPossessionChallengeOutcome
      with tech.ubiqu.Call

  case class AssetPossessionChallengeExpiration(
      handle: tech.ubiqu.InvocationHandle,
      nonce: shared.domain.Nonce,
      expired: Instant)
      extends AuthWithNonce
      with AssetPossessionChallengeOutcome
      with tech.ubiqu.Call
}
