# FastODS
(C) J. Férard 2016

(C) M. Schulz 2008-2013 for SimpleODS

A fast Open Document Spreadsheet (ods) writing library in Java, under GPL v3.

## Why FastODS ?
Because I need to write big and simple ODS files very fast in Java.

There are some very good libraries for [OASIS Open Document Format](https://www.oasis-open.org/standards#opendocumentv1.2), like [Simple ODF](http://incubator.apache.org/odftoolkit/simple/) or [JOpenDocument](www.jopendocument.org/), but they are a little bit slow and cumberstone for only writing **very simple** spreadsheets.
There is a simple and fast library by Martin Schulz, [Simple ODS](http://simpleods.sourceforge.net/), but it is now discontinued, outdated (Java 1.3), has a few limitations (incorrect handling of UTF-8 encoding, missing XML escaping for attributes) and can be speed up.

FastODS is a fork of SimpleODS that aims to be a very fast ODS writing library in Java.

## Limitations
FastODS won't deal with odt, odg, odf, or other od_ files.
It won't even *read* ods files. 
Because it doesn't use XML internally, but only for writing files. That's why it is fast and lightweight.

## Speed
Let's be concrete : FastODS is approximately twice as fast as SimpleODS and ten times faster than JOpenDocument for writing a simple ODS file.

## Example
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

## TODO
* Code cleaning;
* A lot of testing;
* Better ResultSet writing;
* Use the flyweight pattern for cells;
* Speed up float->string
* ...


