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

import java.nio.file.Paths

import akka.actor.ActorSystem
import akka.testkit.TestKit
import cats.effect.IO
import cats.implicits._
import com.ing.baker.il.CompiledRecipe
import com.typesafe.config.{Config, ConfigFactory}
import org.scalatest.concurrent.ScalaFutures
import org.scalatest._
import org.scalatest.mockito.MockitoSugar

import scala.concurrent.duration._
import scala.language.postfixOps

trait BakerRuntimeTestBase
    extends WordSpecLike
    with Matchers
    with MockitoSugar
    with BeforeAndAfter
    with BeforeAndAfterAll {

  def actorSystemName: String

  implicit val timeout: FiniteDuration = 10 seconds

  def writeRecipeToSVGFile(recipe: CompiledRecipe) = {

    import guru.nidi.graphviz.engine.{Format, Graphviz}
    import guru.nidi.graphviz.parse.Parser

    val g = Parser.read(recipe.getRecipeVisualization)

    Graphviz.fromGraph(g).render(Format.SVG).toFile(Paths.get(recipe.name).toFile)
  }

  protected def localLevelDBConfig(
      actorSystemName: String,
      journalInitializeTimeout: FiniteDuration = 10 seconds,
      journalPath: String = "target/journal",
      snapshotsPath: String = "target/snapshots"): Config =
    ConfigFactory.parseString(s"""
         |include "baker.conf"
         |
         |akka {
         |
         |  actor {
         |    provider = "akka.actor.LocalActorRefProvider"
         |    allow-java-serialization = off
         |    serialize-messages = on
         |    serialize-creators = off
         |  }
         |
         |  persistence {
         |     journal.plugin = "akka.persistence.journal.leveldb"
         |     journal.leveldb.dir = "$journalPath"
         |
         |     snapshot-store.plugin = "akka.persistence.snapshot-store.local"
         |     snapshot-store.local.dir = "$snapshotsPath"
         |
         |     auto-start-snapshot-stores = [ "akka.persistence.snapshot-store.local"]
         |     auto-start-journals = [ "akka.persistence.journal.leveldb" ]
         |
         |     journal.leveldb.native = off
         |  }
         |
         |  loggers = ["akka.event.slf4j.Slf4jLogger"]
         |  loglevel = "DEBUG"
         |  logging-filter = "akka.event.slf4j.Slf4jLoggingFilter"
         |}
         |
         |baker {
         |  actor.provider = "local"
         |  actor.read-journal-plugin = "akka.persistence.query.journal.leveldb"
         |  journal-initialize-timeout = $journalInitializeTimeout
         |}
         |
       |logging.root.level = DEBUG
    """.stripMargin)

  protected def clusterLevelDBConfig(
      actorSystemName: String,
      port: Int,
      journalInitializeTimeout: FiniteDuration = 10 seconds,
      journalPath: String = "target/journal",
      snapshotsPath: String = "target/snapshots"): Config =
    ConfigFactory
      .parseString(s"""
       |akka {
       |
       |  actor.provider = "akka.cluster.ClusterActorRefProvider"
       |
       |  remote {
       |    netty.tcp {
       |      hostname = localhost
       |      port = $port
       |    }
       |  }
       |}
       |
       |baker {
       |  actor.provider = "cluster-sharded"
       |  cluster.seed-nodes = ["akka.tcp://$actorSystemName@localhost:$port"]
       |}
    """.stripMargin)
      .withFallback(
        localLevelDBConfig(actorSystemName, journalInitializeTimeout, journalPath, snapshotsPath))

  implicit protected val defaultActorSystem = ActorSystem(actorSystemName)

  override def afterAll(): Unit =
    TestKit.shutdownActorSystem(defaultActorSystem)

}
