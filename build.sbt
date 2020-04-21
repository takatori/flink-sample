

name := "flink-example"
version := "0.1-SNAPSHOT"
scalaVersion := "2.12.11"



// dependencies
  val flinkVersion = "1.9.1"
  val circeVersion = "0.12.0-M3"
  val dependencies = Seq(
    "org.apache.flink"           %% "flink-scala"                    % flinkVersion,
    "org.apache.flink"           %% "flink-streaming-scala"          % flinkVersion,
    "org.apache.flink"           %% "flink-connector-elasticsearch6" % flinkVersion,
    "org.apache.flink"           % "flink-connector-kinesis_2.11"    % flinkVersion,
    "com.typesafe.scala-logging" %% "scala-logging"                  % "3.9.2",
    "com.typesafe"               % "config"                          % "1.4.0",
    "ch.qos.logback"             % "logback-classic"                 % "1.2.3",
    "io.circe"                   %% "circe-core"                     % circeVersion,
    "io.circe"                   %% "circe-generic"                  % circeVersion,
    "io.circe"                   %% "circe-parser"                   % circeVersion,
    "org.apache.flink"           %% "flink-test-utils"               % "1.9.1" % Test,
    "org.scalatest"              %% "scalatest"                      % "3.1.0" % Test
  )
  /**
   * プロジェクト定義
   */
  lazy val project = Project(
    "proxy-writer",
    file("proxy-writer")
  ).settings(Settings.baseSettings)
    .settings()
    .settings(
      libraryDependencies ++= dependencies,
      resolvers ++= Seq(
        Resolver.sbtPluginRepo("releases"),
        Resolver.mavenLocal,
        Resolver.sonatypeRepo("releases"),
        Resolver.sonatypeRepo("snapshots"),
        "Apache Development Snapshot Repository" at "https://repository.apache.org/content/repositories/snapshots/",
        "S3 Snapshot Repository" at "https://s3-ap-northeast-1.amazonaws.com/daemonby-sbt-repo/snapshots/" // flink-connector-kinesis_2.11 の version を 1.10.0 にしたら不要
      )
    )
    .settings(
      mainClass in assembly := Some("inc.stanby.ElasticSearchWriter"),
      assemblyJarName in assembly := "ElasticSearchWriter.jar",
      assemblyOption in assembly := (assembly / assemblyOption).value.copy(includeScala = false),
      assemblyMergeStrategy in assembly := {
        case PathList("javax", "servlet", xs @ _*) => MergeStrategy.first
        case PathList(ps @ _*) if ps.last endsWith ".properties" =>
          MergeStrategy.first
        case PathList(ps @ _*) if ps.last endsWith ".xml"   => MergeStrategy.first
        case PathList(ps @ _*) if ps.last endsWith ".types" => MergeStrategy.first
        case PathList(ps @ _*) if ps.last endsWith ".class" => MergeStrategy.first
        case PathList(ps @ _*) if ps.last endsWith ".txt"   => MergeStrategy.first
        case "staging/application.conf"                     => MergeStrategy.concat
        case "unwanted.txt"                                 => MergeStrategy.discard
        case x =>
          val oldStrategy = (assemblyMergeStrategy in assembly).value
          oldStrategy(x)
      },
      test in assembly := {}
    )
    .settings(
      // stays inside the sbt console when we press "ctrl-c"
      // while a Flink programme executes with "run" or "runMain"
      fork in run := true,
      cancelable in Global := true,
      resourceDirectory in Compile := baseDirectory.value / "src" / "main" / "resources" / "develop"
    )
    .settings(
      addCommandAlias(
        "stagingAssembly",
        ";set resourceDirectory in Compile := baseDirectory.value / \"src\" / \"main\" / \"resources\" / \"staging\"; +assembly"
      ),
      addCommandAlias(
        "productionAssembly",
        ";set resourceDirectory in Compile := baseDirectory.value / \"src\" / \"main\" / \"resources\" / \"production\"; +assembly"
      )
    )
    .dependsOn(
      BuildCore.project
    )
