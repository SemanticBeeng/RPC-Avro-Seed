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

package com.kse.authentication.process.impls

import com.kse.process._
import com.kse.authentication.process.defs.InteractionTypes
import com.kse.authentication.process.defs.domain

object Interactions {

  trait GenerateAssetPossessionChallenge
      extends interactions.Impl[domain.AssetPossessionChallengeResult] {

    val name = InteractionTypes.names.generateAssetPossessionChallenge

    def apply(in: domain.EndUserId): domain.AssetPossessionChallengeResult = {
      if (in.id != null)
        domain.AssetPossessionChallengeProof(null, null, "")
      else
        domain.AssetPossessionChallengeExpiration(null, null, null)
    }
  }
}
