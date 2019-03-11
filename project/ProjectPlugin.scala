import higherkindness.mu.rpc.idlgen.IdlGenPlugin.autoImport._
import sbt.Keys._
import sbt.{AutoPlugin, PluginTrigger, Resolver, _}

object ProjectPlugin extends AutoPlugin {

  override def trigger: PluginTrigger = allRequirements

  object autoImport {

    lazy val V = new {
      val cats           = "1.6.0"
      val catsEffect     = "1.2.0"
      val log4cats       = "0.2.0"
      val logbackClassic = "1.2.3"
      val muRPC          = "0.17.2"
      val frees          = "0.8.3-SNAPSHOT"
      val monix          = "3.0.0-RC2"
      val scopt          = "3.7.0"
      val pureconfig     = "0.9.1" // "0.10.2"
      val shapeless      = "2.3.3"
      val kindProjector  = "0.9.9"
      val scalameta_paradise   = "3.0.0-M11"
      //val scalamacros_paradise = "2.1.1"
    }
  }

  import autoImport._

  private lazy val logSettings: Seq[Def.Setting[_]] = Seq(
    libraryDependencies ++= Seq(
      "ch.qos.logback"    % "logback-classic" % V.logbackClassic,
      "io.chrisdavenport" %% "log4cats-core"  % V.log4cats,
      "io.chrisdavenport" %% "log4cats-slf4j" % V.log4cats
    ))

  lazy val configSettings: Seq[Def.Setting[_]] = Seq(
    libraryDependencies ++= Seq(
      "org.typelevel"         %% "cats-effect" % V.catsEffect,
      "com.github.pureconfig" %% "pureconfig"  % V.pureconfig))

  lazy val serverProtocolSettings: Seq[Def.Setting[_]] = Seq(
    idlType := "avro",
    srcGenSerializationType := "AvroWithSchema",
    sourceGenerators in Compile += (srcGen in Compile).taskValue,
    libraryDependencies ++= Seq(
      "io.higherkindness" %% "mu-rpc-channel" % V.muRPC
    )
  )

  lazy val clientRPCSettings: Seq[Def.Setting[_]] = logSettings ++ Seq(
    libraryDependencies ++= Seq(
      "io.higherkindness" %% "mu-rpc-netty"        % V.muRPC,
      "io.higherkindness" %% "mu-rpc-client-cache" % V.muRPC
    )
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
    Seq("org.typelevel" %% "cats-effect").map(_ % V.catsEffect)


  lazy val coreLibsSettings: Seq[Def.Setting[_]] = Seq(
    libraryDependencies ++= catsLibs ++ freesLibs ++ monixLibs // commonDeps ++ freestyleCoreDeps()
  )

 lazy val clientAppSettings: Seq[Def.Setting[_]] = logSettings ++ Seq(
    libraryDependencies ++= Seq(
      "com.github.scopt" %% "scopt" % V.scopt
    ))

  lazy val serverSettings: Seq[Def.Setting[_]] = logSettings

  lazy val serverAppSettings: Seq[Def.Setting[_]] = logSettings ++ Seq(
    libraryDependencies ++= Seq("io.higherkindness" %% "mu-rpc-server" % V.muRPC))

  override def projectSettings: Seq[Def.Setting[_]] =
    Seq(
      organizationName := "AdrianRaFo",
      scalaVersion := "2.12.6",
      scalacOptions := Seq(
        "-deprecation",
        "-encoding",
        "UTF-8",
        "-feature",
        "-language:existentials",
        "-language:higherKinds",
        "-language:implicitConversions",
        "-unchecked",
        "-Xlint",
        "-Yno-adapted-args",
        "-Ywarn-dead-code",
        "-Ywarn-numeric-widen",
        "-Ywarn-value-discard",
        "-Xfuture",
        //"-Ylog-classpath",
        "-Ywarn-unused-import"
        //"-Xplugin-require:macroparadise"
      ),
      //scalafmtCheck := true,
      //scalafmtOnCompile := true,
      addCompilerPlugin("org.scalameta" % "paradise" % V.scalameta_paradise cross CrossVersion.full),
      //addCompilerPlugin("org.scalamacros" % "paradise" % V.scalamacros_paradise cross CrossVersion.full),
      addCompilerPlugin("org.spire-math" %% "kind-projector" % V.kindProjector cross CrossVersion.binary)
    ) ++ resolvers
    //.settings(scalaMetaSettings)

  lazy val resolvers : Seq[Def.Setting[_]] =
    Seq(updateOptions := updateOptions.value.withCachedResolution(true)) ++ {
      sbt.Keys.resolvers ++=
      Seq(
        Resolver.sonatypeRepo("releases"),
        Resolver.sonatypeRepo("snapshots"),
        Resolver.typesafeIvyRepo("releases"))
    }
}
