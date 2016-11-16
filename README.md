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
* [Technical informations](#technical-informations)
* [HISTORY](#history)

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
Just download the latest jar and run:

```mvn install:install-file -Dfile=fastods-<version>.jar```

## Examples
### Basic example
```java
OdsFile file = new OdsFile("5columns.ods");
Table table = file.addTable("test");

TableCellStyle style = TableCellStyle.builder().name("tcs1").backgroundColor("#00FF00").build()
for (int y = 0; y < 50; y++) {
	final TableRow row = table.nextRow();
	for (int x = 0; x < 5; x++) {
		final TableCell cell = row.nextCell();
		cell.setFloat(random.nextInt(1000));
		cell.setStyle(style);
	}
}

file.save();
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

## Technical informations
### Benchmark
#### SimpleODS installation
To benchmark FastODS, you'll have to install the SimpleODS jar, which is present in the ```src/bench/resources``` folder. Use your favourite command-line interface. Assuming that you have just "git cloned" the repo and that you are in the fastods directory, just type the following lines:
```
cd src/bench/resources
mvn org.apache.maven.plugins:maven-install-plugin:2.5.2:install-file -Dfile=simpleODS-0.5.3.jar
cd ../../..
```
#### Run the benchmark
Then run the benchark:
```
mvn -P bench -Dtest=Benchmark test
```

### Profiling with VisualVM
Download and install VisualVM: https://visualvm.github.io/download.html.

Install and configure the Startup Profiler plugin: https://visualvm.java.net/startupprofiler.html.

#### Step 1: Preset
Create a profiling preset named "fastods" (Preset Name):
* Do not profile packages: java.*, javax.*, sun.*, sunw.*, com.sun.*
* Start profiling from classes: org.apache.maven.surefire.booter.ForkedBooter

#### Step 2: Launch VisualVM
Click on the clock icon (Profile startup), and set application configuration, select "fastods" preset for settings, and copy arguments to clipboard.

#### Step3: Start the test
Type the command:
```mvn -Dmaven.surefire.debug="<copy from clipboard and escape quotes>" -Dtest=ProfileFastODS#testFast test```

Note : if you see a warning: "Profiled application started too soon", just wait for the command line message "Waiting for connection on port 5140". Type a CTRL+C, then click cancel in VisualVM window. 

Currently, the most greedy methods are:
* ```XMLUtil.appendEAttribute```: 30% of the time, 9.6 million calls
* ```FullList.init```, ```set```, ```get```: 27% of the time
* ```HeavyTableRow.appendXMLToTableRow```: 11% of the time, 3.2 million calls

## HISTORY

| version | date | comments |
| --- | --- | --- |
| 0.0.1 | 2016 | first version |
| 0.0.2 | 2016 | all existing data types implemented, fast writing |
| 0.1.0 | 2016-11-13 | footer and header |