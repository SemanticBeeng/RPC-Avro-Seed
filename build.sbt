import ProjectPlugin._

////////////////////////
//// Shared Modules ////
////////////////////////

lazy val config = project
  .in(file("shared/modules/config"))
  .settings(configSettings)

lazy val workflow = project
  .in(file("shared/modules/workflow"))
  .settings(configSettings ++ coreLibsSettings)

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

lazy val server_protocol = project
  .in(file("server/modules/protocol"))
  .settings(serverProtocolSettings)

lazy val server_process = project
  .in(file("server/modules/process"))
  .settings(serverSettings ++ coreLibsSettings)
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

lazy val service_shared_app = project
  .in(file("shared/modules/app"))
  .settings(serverAppSettings ++ coreLibsSettings)
  .dependsOn(server_common, config)

lazy val service_shared_client = project
  .in(file("shared/modules/client"))
  .settings(clientRPCSettings ++ clientAppSettings ++ coreLibsSettings)
  .dependsOn(config)

//////////////////////////
////  Session Service ////
//////////////////////////

lazy val service_session_api = project
  .in(file("services/session/api"))
  .settings(serverProtocolSettings)
  .dependsOn(service_session_shared)

lazy val service_session_shared = project
  .in(file("services/session/shared"))

lazy val service_session_server = project
  .in(file("services/session/server"))
  .settings(serverSettings ++ coreLibsSettings)
  .dependsOn(service_session_api)

lazy val service_session_app = project
  .in(file("services/session/app"))
  .dependsOn(service_session_server, service_session_shared, service_shared_app)

lazy val service_session_impl = project
  .in(file("services/session/impl"))
  .settings(serverSettings ++ coreLibsSettings)
  .dependsOn(service_session_api, service_session_shared)

lazy val service_session_client = project
  .in(file("services/session/client"))
  .settings(clientRPCSettings ++ coreLibsSettings)
  .dependsOn(service_session_api, service_session_shared)

lazy val allModules_session: Seq[ProjectReference] = Seq(
  service_session_api,
  service_session_shared,
  service_session_impl,
  service_session_server,
  service_session_app,
  service_session_client
)

lazy val service_session = project
  .in(file("services/session"))
  .aggregate(allModules_session: _*)
  .dependsOn(allModules_session.map(ClasspathDependency(_, None)): _*)

addCommandAlias("runServiceSession", "service_session_app/runMain com.kse.services.session.app.ServerApp")

/////////////////////////////////
////  Authentication Service ////
/////////////////////////////////
lazy val service_authentication_api = project
  .in(file("services/authentication/api"))
  .settings(serverProtocolSettings)
  .dependsOn(service_authentication_shared, service_session_api, service_session_shared)

lazy val service_authentication_shared = project
  .in(file("services/authentication/shared"))

lazy val service_authentication_server = project
  .in(file("services/authentication/server"))
  .settings(serverSettings ++ coreLibsSettings)
  .dependsOn(service_authentication_api, service_authentication_shared, service_session_api)

lazy val service_authentication_app = project
  .in(file("services/authentication/app"))
  .dependsOn(service_authentication_server, service_authentication_shared, service_shared_app)

lazy val service_authentication_impl = project
  .in(file("services/authentication/impl"))
  .settings(serverSettings ++ coreLibsSettings)
  .dependsOn(service_authentication_api, service_authentication_shared)

lazy val service_authentication_client = project
  .in(file("services/authentication/client"))
  .settings(clientRPCSettings ++ coreLibsSettings)
  .dependsOn(service_authentication_api, service_authentication_shared)

lazy val allModules_authentication: Seq[ProjectReference] = Seq(
  service_authentication_api,
  service_authentication_shared,
  service_authentication_impl,
  service_authentication_server,
  service_authentication_app,
  service_authentication_client
)

lazy val service_authentication = project
  .in(file("services/authentication"))
  .aggregate(allModules_authentication: _*)
  .dependsOn(allModules_authentication.map(ClasspathDependency(_, None)): _*)

addCommandAlias("runServiceAuthentication", "service_authentication_app/runMain com.kse.services.authentication.app.ServerApp")

/////////////////////////
////       Root       ////
/////////////////////////

lazy val allRootModules: Seq[ProjectReference] = Seq(
  shared,
  client,
  server,
  service_session,
  service_authentication,
)

lazy val root = project
  .in(file("."))
  .settings(name := "Avro-Seed")
  .aggregate(allRootModules: _*)
  .dependsOn(allRootModules.map(ClasspathDependency(_, None)): _*)
