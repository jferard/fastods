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

import org.junit.Before;
import org.junit.Test;

import com.github.jferard.fastods.Color;
import com.github.jferard.fastods.DomTester;
import com.github.jferard.fastods.util.FastOdsXMLEscaper;
import com.github.jferard.fastods.util.XMLUtil;

public class TableCellStyleTest {
	private XMLUtil util;

	@Before
	public final void setUp() {
		this.util = new XMLUtil(new FastOdsXMLEscaper());
	}

	@Test
	public final void testAllBorders() throws IOException {
		final TableCellStyle tcs = TableCellStyle.builder("test")
				.borderAll("1pt", Color.AQUA, BorderAttribute.Style.DOUBLE)
				.build();
		final StringBuilder sb = new StringBuilder();
		tcs.appendXMLToContentEntry(this.util, sb);
		DomTester.assertEquals(
				"<style:style style:name=\"test\" style:family=\"table-cell\" style:parent-style-name=\"Default\">"
						+ "<style:table-cell-properties fo:border=\"1pt double #00FFFF\"/>"
						+ "<style:paragraph-properties/>" + "</style:style>",
				sb.toString());
	}

	@Test
	public final void testAllMargins() throws IOException {
		final TableCellStyle tcs = TableCellStyle.builder("tcs")
				.allMargins("10pt").build();
		final StringBuilder sb = new StringBuilder();
		tcs.appendXMLToContentEntry(this.util, sb);
		DomTester.assertEquals("<style:style style:name=\"tcs\" style:family=\"table-cell\" style:parent-style-name=\"Default\">"
						+ "<style:table-cell-properties/>"
						+ "<style:paragraph-properties fo:margin=\"10pt\"/>"
						+ "</style:style>", sb.toString());
	}

	@Test
	public final void testBorders() throws IOException {
		final TableCellStyle tcs = TableCellStyle.builder("test")
				.borderTop("1pt", Color.AQUA, BorderAttribute.Style.DOUBLE)
				.borderRight("2pt", Color.BEIGE, BorderAttribute.Style.SOLID)
				.borderBottom("3pt", Color.CADETBLUE,
						BorderAttribute.Style.DOUBLE)
				.borderLeft("4pt", Color.DARKBLUE, BorderAttribute.Style.DOUBLE)
				.build();
		final StringBuilder sb = new StringBuilder();
		tcs.appendXMLToContentEntry(this.util, sb);
		DomTester.assertEquals(
				"<style:style style:name=\"test\" style:family=\"table-cell\" style:parent-style-name=\"Default\">"
						+ "<style:table-cell-properties fo:border-bottom=\"3pt double #5F9EA0\" fo:border-left=\"4pt double #00008B\" fo:border-right=\"2pt solid #F5F5DC\" fo:border-top=\"1pt double #00FFFF\"/>"
						+ "<style:paragraph-properties/>" + "</style:style>",
				sb.toString());
	}

	@Test
	public final void testMargins() throws IOException {
		final TableCellStyle tcs = TableCellStyle.builder("tcs")
				.marginTop("10pt").marginRight("11pt").marginBottom("12pt")
				.marginLeft("13pt").build();
		final StringBuilder sb = new StringBuilder();
		tcs.appendXMLToContentEntry(this.util, sb);
		DomTester.assertEquals("<style:style style:name=\"tcs\" style:family=\"table-cell\" style:parent-style-name=\"Default\">"
						+ "<style:table-cell-properties/>"
						+ "<style:paragraph-properties fo:margin-bottom=\"12pt\" fo:margin-left=\"13pt\" fo:margin-right=\"11pt\" fo:margin-top=\"10pt\"/>"
						+ "</style:style>", sb.toString());
	}

	@Test(expected = IllegalArgumentException.class)
	public final void testNullName() {
		TableCellStyle.builder(null);
	}
}
