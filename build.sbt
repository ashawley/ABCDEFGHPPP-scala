organization := "io.github.ashawley"

name := "ABCDEFGHPPP"

version := "0.0-SNAPSHOT"

scalaVersion := "2.12.8"

crossScalaVersions := Seq(
  "2.10.7",
  "2.11.12",
  "2.12.8",
  "2.13.0-RC2"
)

scalacOptions ++= {
  CrossVersion.partialVersion(scalaVersion.value) match {
    case Some((2, 12)) =>
      // https://tpolecat.github.io/2017/04/25/scalac-flags.html
      Seq(
        "-deprecation",
        "-encoding", "utf-8",
        "-explaintypes",
        "-feature",
        "-language:existentials",
        "-language:experimental.macros",
        "-language:higherKinds",
        "-language:implicitConversions",
        "-unchecked",
        "-Xcheckinit",
        "-Xfatal-warnings",
        "-Xfuture",
        "-Xlint:adapted-args",
        "-Xlint:by-name-right-associative",
        "-Xlint:constant",
        "-Xlint:delayedinit-select",
        "-Xlint:doc-detached",
        "-Xlint:inaccessible",
        "-Xlint:infer-any",
        "-Xlint:missing-interpolator",
        "-Xlint:nullary-override",
        "-Xlint:nullary-unit",
        "-Xlint:option-implicit",
        "-Xlint:package-object-classes",
        "-Xlint:poly-implicit-overload",
        "-Xlint:private-shadow",
        "-Xlint:stars-align",
        "-Xlint:type-parameter-shadow",
        "-Xlint:unsound-match",
        "-Yno-adapted-args",
        "-Ypartial-unification",
        "-Ywarn-dead-code",
        "-Ywarn-extra-implicit",
        "-Ywarn-inaccessible",
        "-Ywarn-infer-any",
        "-Ywarn-nullary-override",
        "-Ywarn-nullary-unit",
        "-Ywarn-numeric-widen",
        "-Ywarn-unused:implicits",
        "-Ywarn-unused:imports",
        "-Ywarn-unused:locals",
        "-Ywarn-unused:params",
        "-Ywarn-unused:patvars",
        "-Ywarn-unused:privates",
        "-Ywarn-value-discard",
        "-Yno-predef",
        "-Yno-imports"
      )
    case Some((2, 11)) => 
      // https://tpolecat.github.io/2014/04/11/scalac-flags.html
      Seq(
        "-deprecation",
        "-encoding", "UTF-8",
        "-feature",
        "-language:existentials",
        "-language:higherKinds",
        "-language:implicitConversions",
        "-unchecked",
        "-Xfatal-warnings",
        "-Xlint",
        "-Yno-adapted-args",
        "-Ywarn-dead-code",
        "-Ywarn-numeric-widen",
        "-Ywarn-value-discard",
        "-Xfuture",
        "-Ywarn-unused-import",
        "-Yno-predef",
        "-Yno-imports"
      )
    case Some((2, 10)) => 
      Seq(
        "-deprecation",
        "-encoding", "UTF-8",
        "-feature",
        "-unchecked",
        "-Xfatal-warnings",
        "-Xlint",
        "-Ywarn-all",
        "-Xfuture",
        // "-Ywarn-unused-import",
        "-Yno-predef",
        "-Yno-imports"
      )
    case _ =>
      Seq()
  }
}

scalacOptions in (Compile, console) --= Seq(
  "-Xfatal-warnings",
  "-Ywarn-unused:imports",
  "-Yno-predef",
  "-Yno-imports"
)

apiMappings ++= Map(
  scalaInstance.value.libraryJar
    -> url(s"http://www.scala-lang.org/api/${scalaVersion.value}/")
)

apiMappings ++= Option(System.getProperty("sun.boot.class.path")).flatMap { classPath =>
    classPath.split(java.io.File.pathSeparator).filter(_.endsWith(java.io.File.separator + "rt.jar")).headOption
  }.map { jarPath =>
    Map(
      file(jarPath)
        -> url("http://docs.oracle.com/javase/8/docs/api")
    )
  } getOrElse {
    // If everything fails, jam in the Java 11 base module.
    Map(
      file("/modules/java.base")
        -> url("http://docs.oracle.com/en/java/javase/11/docs/api/java.base")
    )
  }
