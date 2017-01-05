/* *****************************************************************************
 * FastODS - a Martin Schulz's SimpleODS fork
 *    Copyright (C) 2016 J. Férard <https://github.com/jferard>
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
 * ****************************************************************************/
package com.github.jferard.fastods.style;

import com.github.jferard.fastods.testutil.DomTester;
import com.github.jferard.fastods.odselement.OdsElements;
import com.github.jferard.fastods.util.FastOdsXMLEscaper;
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
		this.util = new XMLUtil(new FastOdsXMLEscaper());
	}

	@Test
	public final void testAddEmptyToFile() {
		final TableColumnStyle tcs = TableColumnStyle.builder("test").build();
		final OdsElements odsElements = PowerMock.createMock(OdsElements.class);
		final StyleTag styleTag = tcs;

		odsElements.addStyleTag(styleTag);
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
		Assert.assertEquals(cs.getName(), tcs.getDefaultCellStyleName());
	}

	@Test
	public final void testEmpty() throws IOException {
		final TableColumnStyle tcs = TableColumnStyle.builder("test").build();
		final StringBuilder sbc = new StringBuilder();
		final StringBuilder sbt = new StringBuilder();

		tcs.appendXML(this.util, sbc);
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
				.columnWidth("1pt").build();
		final StringBuilder sbc = new StringBuilder();

		tcs.appendXML(this.util, sbc);

		Assert.assertTrue(DomTester
				.equals("<style:style style:name=\"test\" style:family=\"table-column\">"
						+ "<style:table-column-properties fo:break-before=\"auto\" style:column-width=\"1pt\"/>"
						+ "</style:style>", sbc.toString()));
		Assert.assertEquals("1pt", tcs.getColumnWidth());
		Assert.assertEquals(tcs, tcs);
		Assert.assertEquals(tcs.hashCode(), tcs.hashCode());
	}
}
