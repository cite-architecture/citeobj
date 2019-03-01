# `citeobj`

## What it is

`citeobj` is a cross-platform library for working with collections of citable objects.

## Current version: 7.3.0

Status:  in active development.  See [release notes](releases.md)


## License

[GPL 3.0](https://opensource.org/licenses/gpl-3.0.html)


## Documentation

See <https://cite-architecture.github.io/citeobj/>.

## Using, building, testing

`citeobj` is compiled for both the JVM and ScalaJS using scala versions 2.11 and 2.12.  Due to a dependency on `scala-xml`, the JVM builds cannot be used with Scala 2.10.  ScalaJS builds can be compiled for 2.10 as well as 2.11 and 2.12.

Binaries for all platforms are available from jcenter.

If you are using sbt, include Resolver.jcenterRepo in your list of resolvers

    resolvers += Resolver.jcenterRepo

and add this to your library dependencies:

    "edu.holycross.shot" %%% "citeobj" % VERSION

For maven, ivy or gradle equivalents, refer to https://bintray.com/neelsmith/maven/citeobj.

To build from source and test, use normal sbt commands (`compile`, `test` ...).
