# `citeobj`

## What it is

`citeobj` is a cross-platform library for working with collections of citable objects.

## Current version: 0.1

Status:  early stages of development.

## License

[GPL 3.0](https://opensource.org/licenses/gpl-3.0.html)


## Using, building, testing

`citeobj` is compiled for both the JVM and ScalaJS using scala versions 2.11 and 2.12.  Due to a dependency on `scala-xml`, it cannot be used with Scala 2.10.


## Notes on design and work in progress



###Some requirements

- instantiating an object should validate values for each property against their type
- retrieve value by string name of property
- retrieval by a concrete object URN returns an object structure
- retrieval bt property level URN returns a value of the property's type



### Questions

When adding to a collection, perhaps expand an instance into a collection of property-level URN statements? This would reduce property value retrieval to reference by property-level URN.

So maybe a collection is not a vector of **objects**, but a vector of **property values**?  Kind of like leaf-node passages of text.  And erhaps like URN collapsing on texts, we can collapse to object-level?

Can we think of these concrete property values as *name* in URN mapping to *type-value* pair?


### Data about the collection

Need to be able to 'catalog' the collection: list names/types of members. (Perhaps recognize/document annotated methods???? ~~ extensions???)
