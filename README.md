# FastODS
(C) J. Férard 2016

(C) M. Schulz 2008-2013 for SimpleODS

A fast Open Document Spreadsheet (ods) writing library in Java, under GPL v3.

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
Just download the **jar file** from the latest [release](https://github.com/jferard/fastods/releases/).

### Maven
Simply run the following command to install the jar in your local repo:

```
mvn org.apache.maven.plugins:maven-install-plugin:2.5.2:install-file -Dfile=fastods-<version>.jar
```

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
OdsDocument document = new OdsFactory().createDocument();
Table table = document.addTable("test");

TableCellStyle style = TableCellStyle.builder().name("tcs1").backgroundColor("#00FF00").build()
for (int y = 0; y < 50; y++) {
	final TableRow row = table.nextRow();
	for (int x = 0; x < 5; x++) {
		final TableCell cell = row.nextCell();
		cell.setFloat(random.nextInt(1000));
		cell.setStyle(style);
	}
}

document.saveAs("5columns.ods");
```

### Other examples
Two examples are implemented as optional tests, in the ```src/test/java``` folder: ```OdsFileCreation.java``` and ```OdsFileWithHeaderAndFooterCreation.java```. The sources are quite simple.

To test it, one has to run tests manually:
```
mvn -Dtest=OdsFileCreation test
```

And:
```
mvn -Dtest=OdsFileWithHeaderAndFooterCreation test
```

The resulting ods files are written in current directory, and can be opened with LibreOffice or OpenOffice.

## Speed
Let's be concrete : FastODS is approximately twice as fast as SimpleODS and ten times faster than JOpenDocument for writing a small (a single sheet with 5000 rows and 20 columns) simple ODS file. For bigger files, JOpenDocument becomes slower and slower in comparison with FastODS and SimpleODS.

## History

| version | date | comments |
| --- | --- | --- |
| 0.0.1 | 2016 | first version |
| 0.0.2 | 2016 | all existing data types implemented, fast writing |
| 0.1.0 | 2016-11-13 | footer and header |