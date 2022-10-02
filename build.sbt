val scala3Version = "3.2.0"

lazy val root = project
  .in(file("."))
  .settings(
    name := "tiger-scala",
    version := "0.1.0-SNAPSHOT",
    scalaVersion := scala3Version,
    libraryDependencies ++= Seq(
      "org.typelevel" %% "cats-parse" % "0.3.8",
      "org.scalameta" %% "munit" % "0.7.29" % Test
    )
  )
