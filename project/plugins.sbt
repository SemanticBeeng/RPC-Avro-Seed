addSbtPlugin("com.timushev.sbt" % "sbt-updates" % "0.3.4")
addSbtPlugin("com.typesafe.sbt" % "sbt-native-packager" % "1.3.14")

addSbtPlugin("io.higherkindness" % "sbt-mu-idlgen" % "0.17.2")
addSbtPlugin("io.frees"          % "sbt-freestyle" % "0.3.24")

addSbtPlugin("com.lucidchart"    % "sbt-scalafmt"  % "1.15")
addSbtPlugin("com.thesamet" % "sbt-protoc" % "0.99.19")
libraryDependencies += "com.thesamet.scalapb" %% "compilerplugin" % "0.8.2"
