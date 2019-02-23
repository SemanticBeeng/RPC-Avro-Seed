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

import freestyle.free._
import freestyle.tagless._

import scala.concurrent.Future

object algebras {

  /* Handles user interaction */
  @free trait Interact {
    def ask(prompt: String): FS[String]

    def tell(msg: String): FS[Unit]
  }

  /* Validates user input */
  @tagless(stacksafe = true) trait Validation {
    def minSize(s: String, n: Int): FS[Boolean]
    def hasNumber(s: String): FS[Boolean]
  }

  trait Implicits {

    implicit val handlerInteract: Interact.Handler[Future] = new Interact.Handler[Future] {

      def ask(prompt: String): Future[String] = Future.successful {
        println(prompt)
        "koko"
      }

      def tell(msg: String): Future[Unit] = Future.successful(println(msg))
    }
  }

  object implicits extends Implicits

  trait Implicits2 {

    import monix.eval.Task
    import cats.data.StateT
    import cats.syntax.flatMap._

    type Target[A] = StateT[Task, List[String], A]

    implicit val handlerInteract: Interact.Handler[Target] = new Interact.Handler[Target] {

      def ask(prompt: String): Target[String] = tell(prompt) >> StateT.liftF(Task.now("Isidoro1"))

      def tell(msg: String): Target[Unit] = StateT.liftF(Task { println(msg) })
    }

    implicit val validationHandler: Validation.Handler[Target] = new Validation.Handler[Target] {

      def minSize(s: String, n: Int): Target[Boolean] = StateT.liftF(Task.now(s.length >= n))

      def hasNumber(s: String): Target[Boolean] =
        StateT.liftF(Task.now(s.exists(c ⇒ "0123456789".contains(c))))
    }
  }

  object implicit2 extends Implicits2
}
