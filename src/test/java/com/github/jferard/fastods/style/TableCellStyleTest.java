/*******************************************************************************
 * FastODS - a Martin Schulz's SimpleODS fork
 *    Copyright (C) 2016 J. Férard <https://github.com/jferard>
 * SimpleODS - A lightweight java library to create simple OpenOffice spreadsheets
 *    Copyright (C) 2008-2013 Martin Schulz <mtschulz at users.sourceforge.net>
 *
 * This file is part of FastODS.
 *
 * FastODS is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * FastODS is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 ******************************************************************************/
package com.github.jferard.fastods.style;

import java.io.IOException;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.github.jferard.fastods.util.FastOdsXMLEscaper;
import com.github.jferard.fastods.util.XMLUtil;

public class TableCellStyleTest {
	private XMLUtil util;

	@Before
	public final void setUp() {
		this.util = new XMLUtil(new FastOdsXMLEscaper());
	}

	@Test
	public final void testAllMargins() throws IOException {
		TableCellStyle tcs = TableCellStyle.builder(this.util, "tcs")
				.addMargin("10pt", MarginAttribute.Position.ALL).build();
		StringBuilder sb = new StringBuilder();
		tcs.appendXMLToContentEntry(this.util, sb);
		Assert.assertEquals(
				"<style:style style:name=\"tcs\" style:family=\"table-cell\" style:parent-style-name=\"Default\">"
						+ "<style:table-cell-properties/>"
						+ "<style:paragraph-properties fo:margin=\"10pt\"/>"
						+ "</style:style>",
				sb.toString());
	}

	@Test
	public final void testMargins() throws IOException {
		TableCellStyle tcs = TableCellStyle.builder(this.util, "tcs")
				.addMargin("10pt", MarginAttribute.Position.TOP)
				.addMargin("11pt", MarginAttribute.Position.RIGHT)
				.addMargin("12pt", MarginAttribute.Position.BOTTOM)
				.addMargin("13pt", MarginAttribute.Position.LEFT).build();
		StringBuilder sb = new StringBuilder();
		tcs.appendXMLToContentEntry(this.util, sb);
		Assert.assertEquals(
				"<style:style style:name=\"tcs\" style:family=\"table-cell\" style:parent-style-name=\"Default\">"
						+ "<style:table-cell-properties/>"
						+ "<style:paragraph-properties fo:margin-bottom=\"12pt\" fo:margin-left=\"13pt\" fo:margin-right=\"11pt\" fo:margin-top=\"10pt\"/>"
						+ "</style:style>",
				sb.toString());
	}
}
