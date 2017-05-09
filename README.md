# `citeobj`

## What it is

`citeobj` is a cross-platform library for working with collections of citable objects.

## Current version: 0.1.0

Status:  in active development.  See [release notes](releases.md)

## License

[GPL 3.0](https://opensource.org/licenses/gpl-3.0.html)


## Using, building, testing

`citeobj` is compiled for both the JVM and ScalaJS using scala versions 2.11 and 2.12.  Due to a dependency on `scala-xml`, the JVM builds cannot be used with Scala 2.10.  ScalaJS builds can be compiled for 2.10 as well as 2.11 and 2.12.


## Notes on design and work in progress


- A CITE property is a pairing of a property-level Cite2Urn with some value.
- A CITE data collection is a vector of CITE properties.  Applying URN twiddling to a CITE data collection creates a new (possibly empty) CITE data collection.
- A Catalog documents the structure of properties.

## Desiderata

Different kinds of validation:

- enforce 1<->1 relation of *properties* in a data collection and *property definitions* in a catalog
- enforce presence of all defined properties for all object instances of an collection
- validate valid values for each property

Need some kind of collection-level metadata, in addition to metdata residing in property definitions:

- label
- ordered or not
