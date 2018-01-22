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

import com.github.jferard.fastods.Color;
import com.github.jferard.fastods.SimpleColor;
import com.github.jferard.fastods.testlib.DomTester;
import com.github.jferard.fastods.util.XMLUtil;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.Locale;

public class PercentageStyleTest {
	private DataStyleBuilderFactory factory;
	private Locale locale;
	private XMLUtil util;

	@Before
	public void setUp() {
		this.util = XMLUtil.create();
		this.locale = Locale.US;
		this.factory = new DataStyleBuilderFactory(this.util, this.locale);
	}

	@Test
	public final void testDecimalPlaces() throws IOException {
		final PercentageStyle ps = this.factory.percentageStyleBuilder("test")
				.decimalPlaces(5).build();
		final StringBuilder sb = new StringBuilder();
		ps.appendXMLRepresentation(this.util, sb);
		DomTester.assertEquals(
				"<number:percentage-style style:name=\"test\" number:language=\"en\" number:country=\"US\" style:volatile=\"true\">"
						+ "<number:number number:decimal-places=\"5\" number:min-integer-digits=\"1\"/>"
						+ "<number:text>%</number:text>"
						+ "</number:percentage-style>",
				sb.toString());
	}

	@Test
	public final void testGroupThousands() throws IOException {
		final PercentageStyle ps = this.factory.percentageStyleBuilder("test")
				.groupThousands(true).build();
		final StringBuilder sb = new StringBuilder();
		ps.appendXMLRepresentation(this.util, sb);
		DomTester.assertEquals(
				"<number:percentage-style style:name=\"test\" number:language=\"en\" number:country=\"US\" style:volatile=\"true\">"
						+ "<number:number number:decimal-places=\"2\" number:min-integer-digits=\"1\" number:grouping=\"true\"/>"
						+ "<number:text>%</number:text>"
						+ "</number:percentage-style>",
				sb.toString());
	}

	@Test
	public final void testMinIntegeDigits() throws IOException {
		final PercentageStyle ps = this.factory.percentageStyleBuilder("test")
				.minIntegerDigits(8).build();
		final StringBuilder sb = new StringBuilder();
		ps.appendXMLRepresentation(this.util, sb);
		DomTester.assertEquals(
				"<number:percentage-style style:name=\"test\" number:language=\"en\" number:country=\"US\" style:volatile=\"true\">"
						+ "<number:number number:decimal-places=\"2\" number:min-integer-digits=\"8\"/>"
						+ "<number:text>%</number:text>"
						+ "</number:percentage-style>",
				sb.toString());
	}

	@Test
	public final void testNegativeValueColor() throws IOException {
		final PercentageStyle ps = this.factory.percentageStyleBuilder("test")
				.negativeValueColor(SimpleColor.GREEN).build();
		final StringBuilder sb = new StringBuilder();
		ps.appendXMLRepresentation(this.util, sb);
		DomTester.assertEquals(
				"<number:percentage-style style:name=\"test\" number:language=\"en\" number:country=\"US\" style:volatile=\"true\">"
						+ "<number:number number:decimal-places=\"2\" number:min-integer-digits=\"1\"/>"
						+ "<number:text>%</number:text>"
						+ "</number:percentage-style>"
						+ "<number:percentage-style style:name=\"test-neg\" number:language=\"en\" number:country=\"US\" style:volatile=\"true\">"
						+ "<style:text-properties fo:color=\"#008000\"/>"
						+ "<number:text>-</number:text>"
						+ "<number:number number:decimal-places=\"2\" number:min-integer-digits=\"1\"/>"
						+ "<number:text>%</number:text>"
						+ "<style:map style:condition=\"value()&gt;=0\" style:apply-style-name=\"test\"/>"
						+ "</number:percentage-style>",
				sb.toString());
	}

	@Test
	public final void testNegativeValueRed() throws IOException {
		final PercentageStyle ps = this.factory.percentageStyleBuilder("test")
				.negativeValueRed().build();
		final StringBuilder sb = new StringBuilder();
		ps.appendXMLRepresentation(this.util, sb);
		DomTester.assertEquals(
				"<number:percentage-style style:name=\"test\" number:language=\"en\" number:country=\"US\" style:volatile=\"true\">"
						+ "<number:number number:decimal-places=\"2\" number:min-integer-digits=\"1\"/>"
						+ "<number:text>%</number:text>"
						+ "</number:percentage-style>"
						+ "<number:percentage-style style:name=\"test-neg\" number:language=\"en\" number:country=\"US\" style:volatile=\"true\">"
						+ "<style:text-properties fo:color=\"#FF0000\"/>"
						+ "<number:text>-</number:text>"
						+ "<number:number number:decimal-places=\"2\" number:min-integer-digits=\"1\"/>"
						+ "<number:text>%</number:text>"
						+ "<style:map style:condition=\"value()&gt;=0\" style:apply-style-name=\"test\"/>"
						+ "</number:percentage-style>",
				sb.toString());
	}
}
