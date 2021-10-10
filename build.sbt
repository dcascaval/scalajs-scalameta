// BASE SBT / SCALA3 / SCALAJS PROJECT SETUP
lazy val root = project
  .in(file("."))
  .enablePlugins(ScalaJSPlugin)
  .settings(
    name := "example",
    scalaVersion := "3.0.2",

    // Add Scala dependencies.
    libraryDependencies ++= Seq(
      "org.scala-js" %%% "scalajs-dom" % "1.2.0",
      "org.scalameta" %%% "scalameta" % "4.4.28"
    ).map(_.cross(CrossVersion.for3Use2_13)),

    // This is a top-level application with a SJS main method
    scalaJSUseMainModuleInitializer := true
  )
