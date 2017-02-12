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
package com.github.jferard.fastods.datastyle;

import com.github.jferard.fastods.testutil.DomTester;
import com.github.jferard.fastods.util.FastOdsXMLEscaper;
import com.github.jferard.fastods.util.XMLUtil;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.Locale;

public class TimeStyleTest {
	private DataStyleBuilderFactory factory;
	private Locale locale;
	private XMLUtil util;

	@Before
	public void setUp() {
		this.util = new XMLUtil(new FastOdsXMLEscaper());
		this.locale = Locale.US;
		this.factory = new DataStyleBuilderFactory(this.util, this.locale);
	}

	@Test
	public final void testFormat() throws IOException {
		final TimeStyle ps = this.factory.timeStyleBuilder("test")
				.timeFormat(TimeStyle.Format.HHMMSS).build();
		final StringBuilder sb = new StringBuilder();
		ps.appendXML(this.util, sb);
		DomTester.assertEquals(
				"<number:time-style style:name=\"test\" number:language=\"en\" number:country=\"US\" style:volatile=\"true\" number:format-source=\"fixed\">"
						+ "<number:hours/>" + "<number:text>:</number:text>"
						+ "<number:minutes/>" + "<number:text>:</number:text>"
						+ "<number:seconds/>" + "</number:time-style>",
				sb.toString());
	}

	@Test
	public final void testNullFormat() throws IOException {
		final TimeStyle ps = this.factory.timeStyleBuilder("test")
				.timeFormat(null).build();
		final StringBuilder sb = new StringBuilder();
		ps.appendXML(this.util, sb);
		DomTester.assertEquals(
				"<number:time-style style:name=\"test\" number:language=\"en\" number:country=\"US\" style:volatile=\"true\" number:format-source=\"language\"/>",
				sb.toString());
	}
}
