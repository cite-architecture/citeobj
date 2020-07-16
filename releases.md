# `citeobj`: release notes

**7.3.3**:  Fixed bug with CEX output when labelling property is not called "label"; added requirement that ordering-properties and labelling properties belong to the same collection.

**7.3.2**:  Fixed bug with alternate secondary delimiter in CEX. 

**7.3.1**:  Update `xcite` library. 

**7.3.0**:  Exporting CEX.

**7.2.1**:  Significantly more informative error reporting.

**7.2.0**:  Updated version of `xcite` library.

**7.1.3**: Now throwing an exception when building from CEX data with duplicate URNs.

**7.1.2**:  Support specification of character encoing when reading files with `scala.io.Source`.


**7.1.1**: Updated `xcite` library.


**7.1.0**: Fixed bug with empty field values in citedata records.

**7.0.1**: `xcite` dependency to v.3.3.0.

**7.0.0**: All special-case handling of images removed.  Higher-order constructs than basic CITE Collections can be handled via data models with the CITE Architecture's `scm` library.

**6.1.1**: Bug fix in update to `cex` parser.

**6.1.0**:  Support for automated cross buildng.

**6.0.0**:  Correct semantic versioning for API-breaking release.

**5.2.3**: Correct dependency format in build.sbt.

**5.2.2**: Changing binary publication to Scala 2.12 only.

**5.2.1**: Republish 5.2.0 with corrected dependencies.

**5.2.0**: API-breaking update. Redesigned data storage for more rapid access to objects and ranges of objects.

**5.0.0**: Fixes a bug in the implementation of IipImageJpegString extension of CiteImage.  Only API-breaking change is removal of obsolete and previously deprecated code handling configuration from XML sources.

**4.3.3**: Numerous bug fixes related to searching repositories.

**4.3.2**: Corrects handling of header line in `citecollections` and `citeproperties` blocks of CEX to agree with CEX 3.0 specification.

**4.3.1**: Incorporate bug fixes in library dependencies.

**4.3.0**: Updated dependency on ScalaJS allowing binary build using Scala 2.12.3.

**4.2.0**: Full support for CEX 3.0 format.

**4.1.1**: Minor bug fixes.

**4.1.0**: Adds  implementations of `BinaryImageSource`.

**4.0.1**: Corrects bad data in unit test.

**4.0.0**:  API-breaking changes to upgrade to CEX specificadtion 3.0.

**3.1.3**: Fixes a bug in URN matching on citable objects including both `CtsUrn` and `Cite2Urn` objects.

**3.1.2**: Fixes bugs in searching properties of `CiteObject`s and `CiteCollectionRepository`s.

**3.1.1**: Corrects behavior of repository to allow catalog with empty data set.

**3.1.0**: Adds functions for querying data in a CITE Collection repository.


**3.0.0**:  Adds the `~~` function on `CiteCollectionRepository`s.  API-breaking changes in the JVM subproject remove dependency on CSV reader library, and simplify construction of `CiteCollectionRepository`s from file sources.  APIs in shared and JS subprojects are unaltered.

**2.0.0**: Introduces the `CitePropertyImplementation`.

**1.4.0**: Adds functionality to `ImageExtensions` to select extensions by protocol, or for individual collections.

**1.3.1**:  Fixes bugs in parsing CEX of image extensions. and of catalogs of Cite Collections.

**1.3.0**:  Adds `ImageExtensions` class and object.

**1.2.1**:  Better merge of working branches.

**1.2.0**: Adds functionality for working with ordered collections.

**1.1.1**: Bug fixes in exposing shared code to ScalaJS environment.

**1.1.0**: Supports building a data collection from a string source in CEX format.

**1.0.0**: initial release.  Supports building a CiteCollectionRepository in various ways, including from a String in CEX 1.1 format.  Alternatively, in the JVM subproject, XML catalogs can be used with delimited text data to create a repository.
