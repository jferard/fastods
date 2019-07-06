[![Build Status](https://travis-ci.org/jferard/fastods.svg?branch=master)](https://travis-ci.org/jferard/fastods)
[![Code Coverage](https://img.shields.io/codecov/c/github/jferard/fastods/master.svg)](https://codecov.io/github/jferard/fastods?branch=master)

# FastODS
(C) J. Férard 2016-2019

(C) M. Schulz 2008-2013 for SimpleODS

A very fast and lightweight (no dependency) library for creating ODS (Open Document Spreadsheet, mainly for Calc) files in Java. It's a Martin Schulz's SimpleODS fork.

**News:**
 
* **Please note that FastODS is almost ready for production use. The version 1.0 is coming.**

* **I wrote a little [tutorial](https://github.com/jferard/fastods/wiki/Tutorial) that covers most of the features of FastODS, 
but [feel free to ask a question or make a suggestion](https://github.com/jferard/fastods/issues/new).**

## Table of contents
* [Why FastODS?](#why-fastods)
* [Limitations](#limitations)
* [Installation](#installation)
* [Examples](#examples)
* [Documentation](#documentation)
* [Speed](#speed)
* [History](#history)

## Why FastODS?
Because I need to write big and simple ODS files very fast in Java.

There are some very good libraries for [OASIS Open Document Format](https://www.oasis-open.org/standards#opendocumentv1.2), like [Simple ODF](http://incubator.apache.org/odftoolkit/simple/) or [JOpenDocument](http://www.jopendocument.org/), but they are a little bit slow and cumberstone for only writing **very simple** spreadsheets.
There is a simple and fast library by Martin Schulz, [Simple ODS](http://simpleods.sourceforge.net/), but it is now discontinued, outdated (Java 1.3), has a few limitations (incorrect handling of UTF-8 encoding, missing XML escaping for attributes) and can be speed up.

FastODS is a fork of SimpleODS that aims to be a very fast ODS writing library in Java.

## Limitations
FastODS won't deal with odt, odg, odf, or other od_ files.
It won't even *read* ods files.
Because it doesn't use XML internally, but only for writing files. That's why it is fast and lightweight.

It's an *OpenDocument producer* (*Open Document Format for Office Applications (OpenDocument) Version 1.2*, 2.3.1) and only an *OpenDocument producer*:
> An *OpenDocument producer* is a program that creates at least one conforming OpenDocument document  

## Installation
### Standard
Add the following dependency to your POM:
```
<dependency>
		<groupId>com.github.jferard</groupId>
		<artifactId>fastods</artifactId>
		<version>0.6.1</version>
</dependency>
```

### From sources
Type the following command:

`git clone https://github.com/jferard/fastods.git`

Then:

`mvn clean install`

### From jar
First download the **jar file** from the latest [release](https://github.com/jferard/fastods/releases/).

Then run the following command to install the jar in your local repo:

```mvn org.apache.maven.plugins:maven-install-plugin:2.5.2:install-file -Dfile=fastods-<version>.jar```

### Usage
In your POM, you'll have to include the following dependency:
```
<dependencies>
	...
	<dependency>
		<groupId>com.github.jferard</groupId>
		<artifactId>fastods</artifactId>
		<version>[set the version number here]</version>
	</dependency>
	...
</dependencies>
```

## Examples
### Basic example
Taken from the tutorial:

```java
final OdsFactory odsFactory = OdsFactory.create(Logger.getLogger("hello-world"), Locale.US);
final AnonymousOdsFileWriter writer = odsFactory.createWriter();
final OdsDocument document = writer.document();
final Table table = document.addTable("hello-world");
final TableRowImpl row = table.getRow(0);
final TableCell cell = row.getOrCreateCell(0);
cell.setStringValue("Hello, world!");
writer.saveAs(new File("generated_files", "readme_example.ods"));
```

## Documentation
Writing a full documentation would be a considerable work, because every time you change the
library, you have to rewrite the doc.

My idea is to provide a set of examples of the features of FastODS. This is ensures that the doc is up to date.

Those examples are located in [the examples module](https://github.com/jferard/fastods/tree/master/fastods-examples/src/main/java/com/github/jferard/fastods/examples) and are fully commented.
A [tutorial](https://github.com/jferard/fastods/wiki/Tutorial) was extracted from this examples.

To run those examples, one has to run:

```mvn verify```

The resulting ods files are written in `generated_files` directory, and can be opened with LibreOffice or OpenOffice.

### Other examples
Other examples are implemented as integration tests: ```OdsFileCreationIT.java```, ```OdsFileWithHeaderAndFooterCreationIT.java```, etc.

To run those examples, one has to run:

```mvn verify```

The resulting ods files are written in `generated_files` directory, and can be opened with LibreOffice or OpenOffice.
See [the integration tests directory](https://github.com/jferard/fastods/tree/master/fastods/src/test/java/com/github/jferard/fastods/it)


## Speed
Let's be concrete : FastODS is approximately twice as fast as SimpleODS and ten times faster than JOpenDocument for writing large ODS files. (SimpleODF is clearly not the right tool to write large ODS files.)

For more details, see https://github.com/jferard/fastods/wiki/Benchmarking-and-profiling.

## History
See https://github.com/jferard/fastods/releases
