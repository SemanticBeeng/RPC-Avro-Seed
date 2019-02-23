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

import scala.concurrent.Await
import scala.concurrent.duration._
//
import monix.execution.Scheduler.Implicits.global

object AppBasic extends scala.App {

  import com.kse.algebras.Interact
  import com.kse.algebras.implicits._

  val ask = Interact.AskOp("prompt 1")

  val r: String = Await.result(handlerInteract(ask), 3 seconds)

  val tell = Interact.TellOp(s"message = $r")
  Await.result(handlerInteract(tell), 3 seconds)

  import com.kse.algebras.Validation
  import com.kse.algebras.implicit2._

  val hasNumber = Validation.StackSafe.HasNumberOp("abc2")

  val e = Await.result(validationHandler(hasNumber).runF.runToFuture, 3 seconds)
  println(e)
}
