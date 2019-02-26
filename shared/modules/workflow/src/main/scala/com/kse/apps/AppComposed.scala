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

package com.kse.apps

import freestyle.free._
import freestyle.free.implicits._

import monix.execution.Scheduler.Implicits.global
//
import scala.concurrent.Await
import scala.concurrent.duration._
import scala.language.postfixOps

object AppComposed extends scala.App {

  def debugImplicits = {

    import com.kse.algebras._
    import com.kse.handlers.implicits2._
    import cats.mtl.instances.state._ // critical
    import com.kse.modules.st
    import st.implicits._

    import cats.arrow.FunctionK

    import freestyle.free.effects.error._
    import freestyle.free.effects.error.implicits._

    val errHandler: FunctionK[ErrorM.Op, Target]        = implicitly[FunctionK[ErrorM.Op, Target]]
    val stHandler: FunctionK[st.StateM.Op, Target]      = implicitly[FunctionK[st.StateM.Op, Target]]
    val interactHandler: FunctionK[Interact.Op, Target] = implicitly[FunctionK[Interact.Op, Target]]
    val validateHandler: FunctionK[Validation.StackSafe.Op, Target] =
      implicitly[FunctionK[Validation.StackSafe.Op, Target]]

    //val interpreter     = FreeS[FreeApp.Op, Target] //= CopK.FunctionK.summon
    import com.kse.modules.FreeApp

    val inter: FSHandler[FreeApp.Op, Target] = interpretIotaCopK[FreeApp.Op, Target]
    ()
  }

  import cats.instances.list._
  import cats.mtl.instances.state._ // essential for the interpret below to work

  import com.kse.algebras._

  import com.kse.modules.{st, _}
  import st.implicits._

  import com.kse.handlers.implicits2._

//  import iota._
//  import iota.debug.options.ShowTrees
  import freestyle.free.effects.error._
  import freestyle.free.effects.error.implicits._

  import com.kse.modules.FreeApp
  val app             = FreeApp[FreeApp.Op]
  val concreteProgram = app.program[FreeApp.Op]
  val state           = concreteProgram.interpret[Target]
  val task            = state.runEmpty

  val asyncResult = task.runToFuture

  Await.result(asyncResult, 3 seconds)
}
