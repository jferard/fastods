/*
 * FastODS - a Martin Schulz's SimpleODS fork
 *    Copyright (C) 2016-2017 J. Férard <https://github.com/jferard>
 * SimpleODS - A lightweight java library to create simple OpenOffice spreadsheets
 *    Copyright (C) 2008-2013 Martin Schulz <mtschulz at users.sourceforge.net>
 *
 * This file is part of FastODS.
 *
 * FastODS is free software: you can redistribute it and/or modify it under the
 * terms of the GNU General Public License as published by the Free Software
 * Foundation, either version 3 of the License, or (at your option) any later
 * version.
 *
 * FastODS is distributed in the hope that it will be useful, but WITHOUT ANY
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR
 * A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with
 * this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.github.jferard.fastods.style;

import com.github.jferard.fastods.testlib.DomTester;
import com.github.jferard.fastods.odselement.OdsElements;
import com.github.jferard.fastods.util.SimpleLength;
import com.github.jferard.fastods.util.XMLUtil;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.powermock.api.easymock.PowerMock;

import java.io.IOException;

public class TableColumnStyleTest {
	private XMLUtil util;

	@Before
	public void setUp() {
		this.util = XMLUtil.create();
	}

	@Test
	public final void testAddEmptyToFile() {
		final TableColumnStyle tcs = TableColumnStyle.builder("test").build();
		final OdsElements odsElements = PowerMock.createMock(OdsElements.class);

		odsElements.addObjectStyle(tcs);
		odsElements.addObjectStyle(TableCellStyle.DEFAULT_CELL_STYLE);
		PowerMock.replayAll();

		tcs.addToElements(odsElements);

		PowerMock.verifyAll();
	}

	@Test
	public final void testAddNewDefaultCellStyleToFile() {
		final TableCellStyle cellStyle = TableCellStyle.builder("ok").buildHidden();
		final TableColumnStyle tcs = TableColumnStyle.builder("test").defaultCellStyle(cellStyle).build();
		final OdsElements odsElements = PowerMock.createMock(OdsElements.class);

		odsElements.addObjectStyle(tcs);
		odsElements.addObjectStyle(cellStyle);
		PowerMock.replayAll();

		tcs.addToElements(odsElements);

		PowerMock.verifyAll();
	}

	@Test
	public final void testDefaultCellStyle() throws IOException {
		final TableCellStyle cs = TableCellStyle.builder("t").build();
		final TableColumnStyle tcs = TableColumnStyle.builder("test")
				.defaultCellStyle(cs).build();
		final StringBuilder sbt = new StringBuilder();

		tcs.appendXMLToTable(this.util, sbt, -1);

		DomTester.assertEquals(
				"<table:table-column table:style-name=\"test\" table:default-cell-style-name=\"t\"/>",
				sbt.toString());
		Assert.assertEquals(cs, tcs.getDefaultCellStyle());
	}

	@Test
	public final void testEmpty() throws IOException {
		final TableColumnStyle tcs = TableColumnStyle.builder("test").build();
		final StringBuilder sbc = new StringBuilder();
		final StringBuilder sbt = new StringBuilder();

		tcs.appendXMLContent(this.util, sbc);
		tcs.appendXMLToTable(this.util, sbt, 1);

		DomTester.assertEquals(
				"<style:style style:name=\"test\" style:family=\"table-column\">"
						+ "<style:table-column-properties fo:break-before=\"auto\" style:column-width=\"2.5cm\"/>"
						+ "</style:style>",
				sbc.toString());
		DomTester.assertEquals(
				"<table:table-column table:style-name=\"test\" table:default-cell-style-name=\"Default\"/>",
				sbt.toString());
	}

	@Test
	public final void testEmpty2() throws IOException {
		final TableColumnStyle tcs = TableColumnStyle.builder("test").build();
		final StringBuilder sbt = new StringBuilder();
		tcs.appendXMLToTable(this.util, sbt, 2);

		DomTester.assertEquals(
				"<table:table-column table:style-name=\"test\" table:number-columns-repeated=\"2\" table:default-cell-style-name=\"Default\"/>",
				sbt.toString());
	}

	@Test
	public final void testWidth() throws IOException {
		final TableColumnStyle tcs = TableColumnStyle.builder("test")
				.columnWidth(SimpleLength.pt(1.0)).build();
		final StringBuilder sbc = new StringBuilder();

		tcs.appendXMLContent(this.util, sbc);

		DomTester.assertEquals("<style:style style:name=\"test\" style:family=\"table-column\">"
						+ "<style:table-column-properties fo:break-before=\"auto\" style:column-width=\"1pt\"/>"
						+ "</style:style>", sbc.toString());
		Assert.assertEquals(SimpleLength.pt(1.0), tcs.getColumnWidth());
		Assert.assertEquals(tcs, tcs);
		Assert.assertEquals(tcs.hashCode(), tcs.hashCode());
	}
}
