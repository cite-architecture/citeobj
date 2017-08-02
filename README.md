# `citeobj`

## What it is

`citeobj` is a cross-platform library for working with collections of citable objects.

## Current version: 4.3.1

Status:  in active development.  See [release notes](releases.md)

## Known limitations

Boolean comparison of `CiteObject` numeric properties works correctly on the JVM.  When compiled to javascript, however, inherent defects in javascript cause comparison of integer and decimal values to fail.  Integer-to-integer comparisons work correctly.

Example:  on the JVM, if `citeObject` is a `CiteObject` with a numeric property named `numericProperty` given a value of `1`, then the following all work as expected:

    assert(citeObject.valueEquals(numericProperty,1))
    assert(citeObject.numericGreaterThan(numericProperty,1) == false)
    assert(citeObject.numericGreaterThan(numericProperty,1.5) == false)
    assert(citeObject.numericGreaterThan(numericProperty,0.9) == true)
    assert(citeObject.numericGreaterThan(numericProperty,0) == true)

Compiled to javascript, the same code produces the following nonsensical results:

    assert(citeObject.valueEquals(numericProperty,1))
    assert(citeObject.numericGreaterThan(numericProperty,1) == false)
    assert(citeObject.numericGreaterThan(numericProperty,1.5) == false)
    assert(citeObject.numericGreaterThan(numericProperty,0.9) == false)
    assert(citeObject.numericGreaterThan(numericProperty,0) == true)

## License

[GPL 3.0](https://opensource.org/licenses/gpl-3.0.html)



## Using, building, testing

`citeobj` is compiled for both the JVM and ScalaJS using scala versions 2.11 and 2.12.  Due to a dependency on `scala-xml`, the JVM builds cannot be used with Scala 2.10.  ScalaJS builds can be compiled for 2.10 as well as 2.11 and 2.12.

Binaries for all platforms are available from jcenter.

If you are using sbt, include Resolver.jcenterRepo in your list of resolvers

    resolvers += Resolver.jcenterRepo

and add this to your library dependencies:

    "edu.holycross.shot" %%% "citeobj" % VERSION

For maven, ivy or gradle equivalents, refer to https://bintray.com/neelsmith/maven/citeobj.

To build from source and test, use normal sbt commands (`compile`, `test` ...).
