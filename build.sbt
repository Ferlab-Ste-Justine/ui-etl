
name := "ui-etl"

version := "0.1"

val spark_version = "3.0.1"
libraryDependencies += "ca.uhn.hapi" % "hapi-structures-v23" % "2.3"
libraryDependencies +=  "org.apache.spark" %% "spark-sql" % spark_version % Provided
libraryDependencies +=  "org.apache.spark" %% "spark-avro" % spark_version % Provided

/* Test */
libraryDependencies += "org.scalatest" %% "scalatest" % "3.1.0" % "test"
libraryDependencies += "org.apache.spark" %% "spark-hive" % spark_version % "test"

scalaVersion := "2.12.12"
test in assembly := {}

assemblyMergeStrategy in assembly := {
  case "META-INF/io.netty.versions.properties" => MergeStrategy.last
  case "META-INF/native/libnetty_transport_native_epoll_x86_64.so" => MergeStrategy.last
  case "META-INF/DISCLAIMER" => MergeStrategy.last
  case "mozilla/public-suffix-list.txt" => MergeStrategy.last
  case "overview.html" => MergeStrategy.last
  case "git.properties" => MergeStrategy.discard
  case "mime.types" => MergeStrategy.first
  case x =>
    val oldStrategy = (assemblyMergeStrategy in assembly).value
    oldStrategy(x)
}
assemblyJarName in assembly := "ui-etl.jar"
