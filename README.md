[![Build Status](https://travis-ci.org/jferard/fastods.svg?branch=master)](https://travis-ci.org/jferard/fastods)
[![Code Coverage](https://img.shields.io/codecov/c/github/jferard/fastods/master.svg)](https://codecov.io/github/jferard/fastods?branch=master)

# FastODS
(C) J. Férard 2016-2017

(C) M. Schulz 2008-2013 for SimpleODS

A fast Open Document Spreadsheet (ods) writing library in Java, under GPL v3.

**Please note that FastODS is almost ready for production use. The version 1.0 is coming.**

## Table of contents
* [Why FastODS?](#why-fastods)
* [Limitations](#limitations)
* [Installation](#installation)
* [Examples](#examples)
* [Speed](#speed)
* [History](#history)

## Why FastODS?
Because I need to write big and simple ODS files very fast in Java.

There are some very good libraries for [OASIS Open Document Format](https://www.oasis-open.org/standards#opendocumentv1.2), like [Simple ODF](http://incubator.apache.org/odftoolkit/simple/) or [JOpenDocument](www.jopendocument.org/), but they are a little bit slow and cumberstone for only writing **very simple** spreadsheets.
There is a simple and fast library by Martin Schulz, [Simple ODS](http://simpleods.sourceforge.net/), but it is now discontinued, outdated (Java 1.3), has a few limitations (incorrect handling of UTF-8 encoding, missing XML escaping for attributes) and can be speed up.

FastODS is a fork of SimpleODS that aims to be a very fast ODS writing library in Java.

## Limitations
FastODS won't deal with odt, odg, odf, or other od_ files.
It won't even *read* ods files.
Because it doesn't use XML internally, but only for writing files. That's why it is fast and lightweight.

## Installation
### Standard
Add the following dependency to your POM:
```
<dependency>
		<groupId>com.github.jferard</groupId>
		<artifactId>fastods</artifactId>
		<version>0.4.1</version>
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
```java
final OdsFactory odsFactory = OdsFactory.create(Logger.getLogger("example"), Locale.US);
final AnonymousOdsFileWriter writer = odsFactory.createWriter();
final AnonymousOdsDocument document = writer.document();
final Table table = document.addTable("test");

final TableCellStyle style = TableCellStyle.builder("green cell style").backgroundColor("#00FF00").build();
for (int y = 0; y < 50; y++) {
	final TableRow row = table.nextRow();
	final TableCellWalker cell = row.getWalker();
	for (int x = 0; x < 5; x++) {
		cell.setFloatValue(x*y);
		cell.setStyle(style);
		cell.next();
	}
}

writer.saveAs(new File("generated_files", "readme_example.ods"));
```

### Other examples
Other examples are implemented as integration tests: ```OdsFileCreationIT.java```, ```OdsFileWithHeaderAndFooterCreationIT.java```, etc. The sources are quite simple.

To run those examples, one has to run:

```mvn verify```

The resulting ods files are written in current directory, and can be opened with LibreOffice or OpenOffice.

## Speed
Let's be concrete : FastODS is approximately twice as fast as SimpleODS and ten times faster than JOpenDocument for writing a small (a single sheet with 5000 rows and 20 columns) simple ODS file. For bigger files, JOpenDocument becomes slower and slower in comparison with FastODS and SimpleODS.

**Please note that this claim is not valid in the following cases :**
* **when the available RAM grows, JOpenDocument speeds up** (five times slower than FastODS is a better approximation)
* **when the document gets bigger and with enough RAM, FastODS slows down relative to others** (it has the same speed that SimpleODS and is three times faster than JOpenDocument)

For more details, see https://github.com/jferard/fastods/wiki/Benchmarking-and-profiling.

## History
See https://github.com/jferard/fastods/releases
