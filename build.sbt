import ProjectPlugin._

////////////////////////
//// Shared Modules ////
////////////////////////

lazy val config = project
  .in(file("shared/modules/config"))
  .settings(configSettings)
  .dependsOn(service_shared_common)

lazy val workflow = project
  .in(file("shared/modules/workflow"))
  .settings(configSettings ++ coreSrvLibsSettings)

////////////////////////
////     Shared     ////
////////////////////////

lazy val allSharedModules: Seq[ProjectReference] = Seq(
  config,
  workflow
)

lazy val shared = project
  .in(file("shared"))
  .aggregate(allSharedModules: _*)
  .dependsOn(allSharedModules.map(ClasspathDependency(_, None)): _*)

//////////////////////////
////  Server Modules  ////
//////////////////////////

lazy val server_common = project
  .in(file("server/modules/common"))
  .dependsOn(service_shared_common)

lazy val server_protocol = project
  .in(file("server/modules/protocol"))
  .settings(serverProtocolSettings)

lazy val server_process = project
  .in(file("server/modules/process"))
  .settings(serverSettings ++ coreSrvLibsSettings)
  .dependsOn(server_common, server_protocol)

lazy val server_app = project
  .in(file("server/modules/app"))
  .settings(serverAppSettings)
  .dependsOn(server_process, config)

//////////////////////////
////      Server      ////
//////////////////////////

lazy val allServerModules: Seq[ProjectReference] = Seq(
  server_common,
  server_protocol,
  server_process,
  server_app
)

lazy val server = project
  .in(file("server"))
  .aggregate(allServerModules: _*)
  .dependsOn(allServerModules.map(ClasspathDependency(_, None)): _*)

addCommandAlias("runServer", "server_app/runMain com.adrianrafo.seed.server.app.ServerApp")

//////////////////////////
////  Client Modules  ////
//////////////////////////

lazy val client_common = project
  .in(file("client/modules/common"))

lazy val client_process = project
  .in(file("client/modules/process"))
  .settings(clientRPCSettings)
  .dependsOn(client_common, server_protocol, service_shared_client)

lazy val client_app = project
  .in(file("client/modules/app"))
  .settings(clientAppSettings)
  .dependsOn(client_process, config)

//////////////////////////
////      Client      ////
//////////////////////////

lazy val allClientModules: Seq[ProjectReference] = Seq(
  client_common,
  client_process,
  client_app
)

lazy val client = project
  .in(file("client"))
  .aggregate(allClientModules: _*)
  .dependsOn(allClientModules.map(ClasspathDependency(_, None)): _*)

addCommandAlias("runClient", "client_app/runMain com.adrianrafo.seed.client.app.ClientApp")

//////////////////////////
////  Shared `app`    ////
//////////////////////////

lazy val service_shared_common = project
  .in(file("shared/modules/common"))
  .settings(serverAppSettings ++ coreSrvLibsSettings)

lazy val service_shared_app = project
  .in(file("shared/modules/app"))
  .settings(serverAppSettings ++ coreSrvLibsSettings)
  .dependsOn(server_common, config)

lazy val service_shared_client = project
  .in(file("shared/modules/client"))
  .settings(clientRPCSettings ++ clientAppSettings ++ coreSrvLibsSettings)
  .dependsOn(config)

//////////////////////////
////  Session Service ////
//////////////////////////

lazy val session_module_api = project
  .in(file("modules/session/api"))
  .settings(serverProtocolSettings)
  .dependsOn(session_module_shared)

lazy val session_module_shared = project
  .in(file("modules/session/shared"))
  .settings(serverSettings ++ coreSrvLibsSettings)

lazy val session_module_entity = project
  .in(file("modules/session/entity"))
  .dependsOn(session_module_shared)
  .settings(serverSettings ++ coreSrvEntitySettings)

lazy val session_module_server = project
  .in(file("modules/session/server"))
  .settings(serverSettings ++ coreSrvLibsSettings)
  .dependsOn(session_module_api)

lazy val session_module_app = project
  .in(file("modules/session/app"))
  .dependsOn(session_module_server, session_module_shared, service_shared_app)

lazy val session_module_impl = project
  .in(file("modules/session/impl"))
  .settings(serverSettings ++ coreSrvLibsSettings)
  .dependsOn(session_module_api, session_module_shared)

lazy val session_module_client = project
  .in(file("modules/session/client"))
  .settings(clientRPCSettings ++ coreSrvLibsSettings)
  .dependsOn(service_shared_client, session_module_api, session_module_shared)

lazy val allModules_session: Seq[ProjectReference] = Seq(
  session_module_api,
  session_module_shared,
  session_module_entity,
  session_module_server,
  session_module_impl,
  session_module_app,
  session_module_client
)

lazy val session_module = project
  .in(file("modules/session"))
  .aggregate(allModules_session: _*)
  .dependsOn(allModules_session.map(ClasspathDependency(_, None)): _*)
  .enablePlugins(DockerPlugin, JavaAppPackaging)  //#todo not used yet

addCommandAlias("runServiceSession", "session_module_app/runMain com.kse.session.services.app.ServerApp")

/////////////////////////////////
////  Authentication Service ////
/////////////////////////////////
lazy val authentication_module_api = project
  .in(file("modules/authentication/api"))
  .settings(serverProtocolSettings)
  .dependsOn(authentication_module_shared, session_module_api, session_module_shared)

lazy val authentication_module_shared = project
  .in(file("modules/authentication/shared"))
  .settings(serverSettings ++ coreSrvLibsSettings)
  .dependsOn(session_module_shared)

lazy val authentication_module_server = project
  .in(file("modules/authentication/server"))
  .settings(serverSettings ++ coreSrvLibsSettings)
  .dependsOn(authentication_module_api, authentication_module_shared, session_module_api)

lazy val authentication_module_process = project
  .in(file("modules/authentication/process"))
  .settings(serverSettings ++ coreSrvProcessSettings)
  .dependsOn(authentication_module_shared, session_module_api)

lazy val authentication_module_app = project
  .in(file("modules/authentication/app"))
  .dependsOn(authentication_module_server, authentication_module_shared, service_shared_app)

lazy val authentication_module_impl = project
  .in(file("modules/authentication/impl"))
  .settings(serverSettings ++ coreSrvLibsSettings)
  .dependsOn(authentication_module_api, authentication_module_shared)

lazy val authentication_module_client = project
  .in(file("modules/authentication/client"))
  .settings(clientRPCSettings ++ coreSrvLibsSettings)
  .dependsOn(service_shared_client, authentication_module_api, authentication_module_shared, session_module_shared)

lazy val allModules_authentication: Seq[ProjectReference] = Seq(
  authentication_module_api,
  authentication_module_shared,
  authentication_module_impl,
  authentication_module_server,
  authentication_module_process,
  authentication_module_app,
  authentication_module_client
)

lazy val authentication_module = project
  .in(file("modules/authentication"))
  .aggregate(allModules_authentication: _*)
  .dependsOn(allModules_authentication.map(ClasspathDependency(_, None)): _*)
  .enablePlugins(DockerPlugin, JavaAppPackaging)  //#todo not used yet

addCommandAlias("runServiceAuthentication", "authentication_module_app/runMain com.kse.authentication.services.app.ServerApp")

/////////////////////////
////       Root       ////
/////////////////////////

lazy val allRootModules: Seq[ProjectReference] = Seq(
  shared,
  client,
  server,
  session_module,
  authentication_module
)

lazy val root = project
  .in(file("."))
  .settings(name := "Avro-Seed")
  .aggregate(allRootModules: _*)
  .dependsOn(allRootModules.map(ClasspathDependency(_, None)): _*)
