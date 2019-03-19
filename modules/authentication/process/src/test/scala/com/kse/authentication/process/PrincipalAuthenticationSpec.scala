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

package com.kse.authentication.process

import akka.actor.ActorSystem
import akka.testkit.TestKit
import com.ing.baker.il.CompiledRecipe
import com.ing.baker.runtime.core.{Baker, RuntimeEvent}
import com.ing.baker.types.{Converters, Value}
import com.typesafe.config.{Config, ConfigFactory}
import org.mockito.Matchers._
import org.mockito.Mockito._
import org.scalatest._
import org.scalatest.mockito.MockitoSugar

import scala.concurrent.duration._
import scala.language.postfixOps

class PrincipalAuthenticationSpec extends BakerRuntimeTestBase {

  override def actorSystemName: String = "principalAuthentication"
}
