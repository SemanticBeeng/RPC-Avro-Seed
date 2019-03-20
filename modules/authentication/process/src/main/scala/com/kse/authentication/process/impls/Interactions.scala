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
import com.kse.authentication.process.defs
import com.kse.authentication.shared

object Interactions {

  trait GenerateAssetPossessionChallenge
      extends interactions.Impl[defs.domain.AssetPossessionChallengeOutcome] {

    /**
     * #todo could also use [[defs.InteractionKinds.generateAssetPossessionChallenge.name]] but refactoring would break running processes
     */
    val name = defs.InteractionKinds.names.generateAssetPossessionChallenge

    def apply(in: shared.domain.EndUserId): defs.domain.AssetPossessionChallengeOutcome = {
      if (in.id != null)
        defs.domain.AssetPossessionChallengeProof(null, null, "")
      else
        defs.domain.AssetPossessionChallengeExpiration(null, null, null)
    }
  }
}
