import higherkindness.mu.rpc.idlgen.IdlGenPlugin.autoImport._
import sbt.Keys._
import sbt.{AutoPlugin, PluginTrigger, Resolver, _}
import sbtprotoc.ProtocPlugin.autoImport._

object ProjectPlugin extends AutoPlugin {

  override def trigger: PluginTrigger = allRequirements

  import dependencies._

    lazy val coreSrvLibsSettings: Seq[Def.Setting[_]] = Seq(
      libraryDependencies ++= catsLibs ++ freesLibs ++ monixLibs ++ enumeratumLibs // commonDeps ++ freestyleCoreDeps()
    )

    lazy val coreSrvEntitySettings: Seq[Def.Setting[_]] = coreSrvLibsSettings ++ Seq(
      libraryDependencies ++= doobieLibs ++ aecorLibs ++ circeLibs
    )

   lazy val clientAppSettings: Seq[Def.Setting[_]] = logSettings ++ Seq(
      libraryDependencies ++= Seq(
        "com.github.scopt" %% "scopt" % V.scopt
      ))

    lazy val serverSettings: Seq[Def.Setting[_]] = logSettings

    lazy val serverAppSettings: Seq[Def.Setting[_]] = logSettings ++ Seq(
      libraryDependencies ++= Seq("io.higherkindness" %% "mu-rpc-server" % V.muRPC))

  private lazy val logSettings: Seq[Def.Setting[_]] = Seq(
    libraryDependencies ++= logLibs)

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

  override def projectSettings: Seq[Def.Setting[_]] =
    Seq(
      organizationName := "KSE",
      scalaVersion := "2.12.7",
      scalacOptions := commonScalacOptions ++ Seq("-Xmax-classfile-name", "128"),
//      scalacOptions in(Compile, console) ~= {
//        _.filterNot(unusedWarnings.toSet + "-Ywarn-value-discard")
//      },

      fork := true,

      libraryDependencies := Seq(
        //V.kindProjectorPlugin,      //#todo why aecor uses this?
        //V.scalametaParadisePlugin,  //#todo why aecor uses this?
        V.scalapbRuntime              //#todo not used yet
      ),
      V.scalametaParadisePluginAdd,
      V.kindProjectorPluginAdd,
      //addCompilerPlugin("org.scalamacros" % "paradise" % V.scalamacros_paradise cross CrossVersion.full),
      V.betterMonadicForPluginAdd,    //#todo not used yet

      PB.targets in Compile := Seq(   //#todo not used yet
        scalapb.gen(flatPackage = true) -> (sourceManaged in Compile).value
      )
    ) ++ resolvers

  lazy val commonScalacOptions = Seq(
        "-deprecation",
        "-encoding",
        "UTF-8",
        "-feature",
        "-language:existentials",
        "-language:higherKinds",
        "-language:implicitConversions",
        "-language:experimental.macros",
        "-unchecked",
        "-Xlint",
        "-Yno-adapted-args",
        "-Ywarn-dead-code",
        "-Ywarn-numeric-widen",
        "-Ywarn-value-discard",
        "-Xfuture",
        //"-Ylog-classpath",
        "-Ywarn-unused-import",
        //"-Xfatal-warnings",
        "-Ypartial-unification",
        "-Xmacro-settings:materialize-derivations" // https://pureconfig.github.io/docs/faq.html
        //"-Xplugin-require:macroparadise"
      ) //++ unusedWarnings

  lazy val unusedWarnings = Seq("-Ywarn-unused", "-Ywarn-unused-import")

  lazy val resolvers : Seq[Def.Setting[_]] =
    Seq(
      updateOptions := //updateOptions.value.withCachedResolution(true) +
                       updateOptions.value.withLatestSnapshots(false)
    ) ++ {
      sbt.Keys.resolvers ++=
      Seq(
        Resolver.sonatypeRepo("releases"),
        Resolver.sonatypeRepo("snapshots"),
        Resolver.typesafeIvyRepo("releases"),
        Resolver.bintrayRepo("ovotech", "maven"))
    }
}
