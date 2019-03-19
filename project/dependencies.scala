import sbt.{CrossVersion, addCompilerPlugin, _}

object dependencies {

    lazy val V = new {
      val cats           = "1.6.0"
      val catsEffect     = "1.2.0"
      val catsTagless    = "0.2.0"
      val catsMTL        = "0.4.0"
      val betterMonadicFor = "0.2.4"
      val log4cats       = "0.2.0"
      val logbackClassic = "1.2.3"
      val muRPC          = "0.17.2"
      val frees          = "0.8.3-SNAPSHOT"
      val monix          = "3.0.0-RC2"
      val scopt          = "3.7.0"
      val pureconfig     = "0.10.0" // "0.10.2"
      val shapeless      = "2.3.3"
      val kindProjector  = "0.9.9"
      val scalameta_paradise   = "3.0.0-M11"
      //val simulacrum     = "0.12.0"
      //val scalamacros_paradise = "2.1.1"
      val aecor = "0.18.0"
      val aecorPostgres = "0.3.0"
      val akka = "2.5.18"
      val boopickle = "1.3.0"
      val circeDerivation = "0.10.0-M1"
      val circe = "0.10.1"
      val doobie = "0.6.0"
      val enumeratum = "1.5.13"
      val baker = "2.0.3"

      // Test
      val scalaCheck = "1.14.0"
      val scalaTest = "3.0.5"
      val mockito = "1.10.19"

      //val simulacrumPlugin = "com.github.mpilquist" %% "simulacrum" % simulacrum
      //lazy val kindProjectorPlugin    =    compilerPlugin("org.spire-math" %% "kind-projector" % kindProjector)
      lazy val kindProjectorPluginAdd = addCompilerPlugin("org.spire-math" %% "kind-projector" % kindProjector cross CrossVersion.binary)

      //lazy val scalametaParadisePlugin    =    compilerPlugin("org.scalameta" % "paradise" % scalameta_paradise cross CrossVersion.full)
      lazy val scalametaParadisePluginAdd = addCompilerPlugin("org.scalameta" % "paradise" % scalameta_paradise cross CrossVersion.full)

      lazy val betterMonadicForPluginAdd = addCompilerPlugin("com.olegpy" %% "better-monadic-for" % betterMonadicFor)

      lazy val scalapbRuntime = "com.thesamet.scalapb" %% "scalapb-runtime" % scalapb.compiler.Version.scalapbVersion % "protobuf"
    }

  lazy val logLibs = Seq(
      "ch.qos.logback"     % "logback-classic" % V.logbackClassic,
      "io.chrisdavenport" %% "log4cats-core"   % V.log4cats,
      "io.chrisdavenport" %% "log4cats-slf4j"  % V.log4cats,
      "com.typesafe.akka" %% "akka-slf4j" % V.akka
      //"io.chrisdavenport" %% "cats-par" % "0.2.0"
    )

    lazy val configLibs = Seq(
      "org.typelevel"         %% "cats-effect" % V.catsEffect,
      "com.github.pureconfig" %% "pureconfig"  % V.pureconfig)

   lazy val monixLibs = Seq(
      "io.monix" %% "monix-eval" % V.monix,
      "io.monix" %% "monix-execution" % V.monix,
      "io.monix" %% "monix-reactive" % V.monix)

   lazy val catsLibs =
    Seq("org.typelevel" %% "cats-kernel",
        "org.typelevel" %% "cats-macros",
        "org.typelevel" %% "cats-core",
        "org.typelevel" %% "cats-laws",
        "org.typelevel" %% "cats-free",
        "org.typelevel" %% "cats-testkit").map(_ % V.cats) ++
    Seq("org.typelevel" %% "cats-effect").map(_ % V.catsEffect) ++
    Seq("org.typelevel" %% "cats-tagless-core",
        "org.typelevel" %% "cats-tagless-laws",
        "org.typelevel" %% "cats-tagless-macros").map(_ % V.catsTagless)

    lazy val enumeratumLibs = Seq(
      "com.beachape" %% "enumeratum" % V.enumeratum,
      "com.beachape" %% "enumeratum-circe" % V.enumeratum
    )

    lazy val aecorLibs = Seq(
      "io.aecor" %% "core" % V.aecor,
      "io.aecor" %% "schedule" % V.aecor,
      "io.aecor" %% "akka-cluster-runtime" % V.aecor,
      "io.aecor" %% "distributed-processing" % V.aecor,
      "io.aecor" %% "boopickle-wire-protocol" % V.aecor,
      "io.aecor" %% "aecor-postgres-journal" % V.aecorPostgres,
      "com.ovoenergy" %% "fs2-kafka" % "0.16.4",
      "io.suzaku" %% "boopickle-shapeless" % V.boopickle,
      "io.aecor" %% "test-kit" % V.aecor % Test
    )

    lazy val doobieLibs = Seq(
      "org.tpolecat" %% "doobie-core" % V.doobie,
      "org.tpolecat" %% "doobie-postgres" % V.doobie,
      "org.tpolecat" %% "doobie-hikari" % V.doobie
    )

    lazy val circeLibs = Seq(
      "io.circe" %% "circe-core" % V.circe,
      "io.circe" %% "circe-derivation" % V.circeDerivation,
      "io.circe" %% "circe-generic" % V.circe,
      "io.circe" %% "circe-java8" % V.circe,
      "io.circe" %% "circe-parser" % V.circe
    )

  lazy val freesLibs = Seq(
        "io.frees" %% "frees-core" % V.frees,
        "io.frees" %% "frees-effects" % V.frees,
        "io.frees" %% "frees-cache" % V.frees,
        "io.frees" %% "frees-config" % V.frees,
        "io.frees" %% "frees-logging" % V.frees,
        "io.frees" %% "frees-async" % V.frees,
        "io.frees" %% "frees-async-cats-effect" % V.frees,
        "io.frees" %% "frees-monix" % V.frees)

    lazy val muRPCLbs = Seq(
      "io.higherkindness" %% "mu-rpc-netty"        % V.muRPC,
      "io.higherkindness" %% "mu-rpc-client-cache" % V.muRPC
    )

    lazy val muRPCSrvLibs = Seq(
      "io.higherkindness" %% "mu-rpc-server" % V.muRPC)

    lazy val muRPCProtocolLibs = Seq(
      "io.higherkindness" %% "mu-rpc-channel" % V.muRPC
    )

    lazy val bakerLibs = Seq(
      "com.ing.baker" %% "baker-recipe-dsl" % V.baker,
      "com.ing.baker" %% "baker-compiler"   % V.baker,
      "com.ing.baker" %% "baker-runtime"    % V.baker % Test
    ) ++ graphLibs

    lazy val graphLibs = Seq(
      "org.scala-graph"  %% "graph-core"     % "1.11.5",
      "org.scala-graph"  %% "graph-dot"      % "1.11.5",
      "guru.nidi"         % "graphviz-java"  % "0.8.0"
    )

    lazy val testLibs = Seq(
        "org.scalatest"  %% "scalatest"   % V.scalaTest  % Test,
        "org.scalacheck" %% "scalacheck"  % V.scalaCheck % Test,
         "org.mockito"    % "mockito-all" % V.mockito    % Test,
         "com.typesafe.akka" %% "akka-testkit" % V.akka  % Test
    )
}
