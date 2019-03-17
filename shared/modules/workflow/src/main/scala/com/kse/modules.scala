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

package com.kse

import freestyle.free.debug

object modules {

  import algebras._
  import freestyle.free._
  import freestyle.free.implicits._
  import freestyle.free.effects.error._
  import freestyle.free.effects.error.implicits._
  import freestyle.free.effects.state

  val st = state[List[String]]
  import st.implicits._

  //@debug
  @module
  trait FreeApp {

    val persistence: st.StateM
    val errorM: ErrorM

    val interact: Interact
    val validation: Validation.StackSafe

    import cats.syntax.semigroupal._

    def program[F[_]](
        implicit I: Interact[F],
        E: ErrorM[F],
        R: st.StateM[F],
        V: Validation.StackSafe[F]): FreeS[F, Unit] = {
      for {
        cat     ← I.ask("what is your kitty name?")
        isValid ← (V.minSize(cat, 5) |@| V.hasNumber(cat)).map(_ && _)
        _ ← if (isValid) R.modify(cat :: _)
        else E.error(new RuntimeException(s"Invalid name $cat!"))
        cats ← R.get
        _    ← I.tell(cats.toString)
      } yield ()
    }
  }
}
