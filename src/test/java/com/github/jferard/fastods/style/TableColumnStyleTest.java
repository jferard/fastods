package com.github.jferard.fastods.style;

import java.io.IOException;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.powermock.api.easymock.PowerMock;
import org.xml.sax.SAXException;

import com.github.jferard.fastods.DomTester;
import com.github.jferard.fastods.OdsFile;
import com.github.jferard.fastods.util.FastOdsXMLEscaper;
import com.github.jferard.fastods.util.XMLUtil;

public class TableColumnStyleTest {
	private XMLUtil util;

	@Before
	public void setUp() {
		this.util = new XMLUtil(new FastOdsXMLEscaper());
	}

	@Test
	public final void testAddEmptyToFile() {
		TableColumnStyle tcs = TableColumnStyle.builder("test").build();
		OdsFile f = PowerMock.createMock(OdsFile.class);

		f.addStyleTag(tcs);
		PowerMock.replayAll();

		tcs.addToFile(f);

		PowerMock.verifyAll();
	}

	@Test
	public final void testEmpty() throws IOException, SAXException {
		TableColumnStyle tcs = TableColumnStyle.builder("test").build();
		StringBuilder sbc = new StringBuilder();
		StringBuilder sbt = new StringBuilder();

		tcs.appendXMLToContentEntry(this.util, sbc);
		tcs.appendXMLToTable(this.util, sbt, 1);

		Assert.assertTrue(DomTester
				.equals("<style:style style:name=\"test\" style:family=\"table-column\">"
						+ "<style:table-column-properties fo:break-before=\"auto\" style:column-width=\"2.5cm\"/>"
						+ "</style:style>", sbc.toString()));
		Assert.assertTrue(DomTester.equals(
				"<table:table-column table:style-name=\"test\" table:default-cell-style-name=\"Default\"/>",
				sbt.toString()));
	}

	@Test
	public final void testEmpty2() throws IOException, SAXException {
		TableColumnStyle tcs = TableColumnStyle.builder("test").build();
		StringBuilder sbt = new StringBuilder();
		tcs.appendXMLToTable(this.util, sbt, 2);

		Assert.assertTrue(DomTester.equals(
				"<table:table-column table:style-name=\"test\" table:number-columns-repeated=\"2\" table:default-cell-style-name=\"Default\"/>",
				sbt.toString()));
	}

	@Test
	public final void testWidth() throws IOException, SAXException {
		TableColumnStyle tcs = TableColumnStyle.builder("test")
				.columnWidth("1pt").build();
		StringBuilder sbc = new StringBuilder();

		tcs.appendXMLToContentEntry(this.util, sbc);

		Assert.assertTrue(DomTester
				.equals("<style:style style:name=\"test\" style:family=\"table-column\">"
						+ "<style:table-column-properties fo:break-before=\"auto\" style:column-width=\"1pt\"/>"
						+ "</style:style>", sbc.toString()));
		Assert.assertEquals("1pt", tcs.getColumnWidth());
		Assert.assertEquals(tcs, tcs);
		Assert.assertEquals(tcs.hashCode(), tcs.hashCode());
	}

	@Test
	public final void testDefaultCellStyle() throws IOException, SAXException {
		TableCellStyle cs = TableCellStyle.builder("t").build();
		TableColumnStyle tcs = TableColumnStyle.builder("test")
				.defaultCellStyle(cs).build();
		StringBuilder sbt = new StringBuilder();

		tcs.appendXMLToTable(this.util, sbt, -1);

		Assert.assertTrue(DomTester.equals(
				"<table:table-column table:style-name=\"test\" table:default-cell-style-name=\"t\"/>",
				sbt.toString()));
		Assert.assertEquals(cs.getName(), tcs.getDefaultCellStyleName());
	}
}
