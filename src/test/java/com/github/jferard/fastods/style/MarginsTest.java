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

import java.io.IOException;

import com.github.jferard.fastods.util.SimpleLength;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.github.jferard.fastods.util.FastOdsXMLEscaper;
import com.github.jferard.fastods.util.XMLUtil;

public class MarginsTest {
	private MarginsBuilder builder;
	private XMLUtil util;

	@Before
	public void setUp() {
		this.util = XMLUtil.create();
		this.builder = new MarginsBuilder();
	}

	@Test
	public final void testAll() throws IOException {
		final Margins margins = this.builder.all(SimpleLength.pt(10.0)).build();
		final StringBuilder sb = new StringBuilder();
		margins.appendXMLToTableCellStyle(this.util, sb);
		Assert.assertEquals(" fo:margin=\"10pt\"", sb.toString());
	}

	@Test
	public final void testTRBL() throws IOException {
		final Margins margins = this.builder.all(SimpleLength.pt(10.0)).top(SimpleLength.pt(10.0))
				.right(SimpleLength.pt(11.0)).bottom(SimpleLength.pt(12.0)).left(SimpleLength.pt(13.0)).build();
		final StringBuilder sb = new StringBuilder();
		margins.appendXMLToTableCellStyle(this.util, sb);
		Assert.assertEquals(
				" fo:margin=\"10pt\" fo:margin-right=\"11pt\" fo:margin-bottom=\"12pt\" fo:margin-left=\"13pt\"",
				sb.toString());
	}

	@Test
	public final void testTRBL2() throws IOException {
		final Margins margins = this.builder.top(SimpleLength.pt(10.0)).right(SimpleLength.pt(11.0))
				.bottom(SimpleLength.pt(12.0)).left(SimpleLength.pt(13.0)).build();
		final StringBuilder sb = new StringBuilder();
		margins.appendXMLToTableCellStyle(this.util, sb);
		Assert.assertEquals(
				" fo:margin-top=\"10pt\" fo:margin-right=\"11pt\" fo:margin-bottom=\"12pt\" fo:margin-left=\"13pt\"",
				sb.toString());
	}
}
