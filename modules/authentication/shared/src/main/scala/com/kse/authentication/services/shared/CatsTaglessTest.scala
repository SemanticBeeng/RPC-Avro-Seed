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

import cats.tagless._

class CatsTaglessTest {

  trait ExpressionAlgI[F[_], T] {
    def num(i: String): F[T]
    def divide(dividend: Float, divisor: Float): F[Float]
  }

  @finalAlg
  @autoFunctorK
  @autoSemigroupalK
  @autoProductNK
  trait ExpressionAlgLong[F[_]] extends ExpressionAlgI[F, Float] {
    def num(i: String): F[Float]
    def divide(dividend: Float, divisor: Float): F[Float]
  }

  import util.Try

  implicit object tryExpression extends ExpressionAlgLong[Try] {
    def num(i: String)                          = Try(i.toFloat)
    def divide(dividend: Float, divisor: Float) = Try(dividend / divisor)
  }

  import cats.tagless.implicits._
  import cats.implicits._
  import cats._

  implicit val fk: Try ~> Option = λ[Try ~> Option](_.toOption)

  tryExpression.mapK(fk).num("2")

  import cats.free.Free
  import cats.arrow.FunctionK
  import ExpressionAlgLong.autoDerive._

  def toFree[F[_]]: F ~> Free[F, ?] = λ[F ~> Free[F, ?]](t => Free.liftF(t))
  //λ[F ~> Free[F, ?]](t => Free.liftF(t))
}
