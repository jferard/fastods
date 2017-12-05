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
import com.github.jferard.fastods.testlib.DomTester;
import com.github.jferard.fastods.util.XMLUtil;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.Currency;
import java.util.Locale;

public class CurrencyStyleTest {
	private DataStyleBuilderFactory factory;
	private Locale locale;
	private XMLUtil util;
	private String usSymbol;
	private String frSymbol;

	@Before
	public void setUp() {
		this.util = XMLUtil.create();
		this.locale = Locale.US;
		this.factory = new DataStyleBuilderFactory(this.util, this.locale);
		this.usSymbol = Currency.getInstance(this.locale).getSymbol();
		this.frSymbol = Currency.getInstance(Locale.FRANCE).getSymbol();
	}

	@Test
	public final void testDecimalPlaces() throws IOException {
		final CurrencyStyle ps = this.factory.currencyStyleBuilder("test")
				.decimalPlaces(5).build();
		final StringBuilder sb = new StringBuilder();
		ps.appendXMLRepresentation(this.util, sb);
		DomTester.assertEquals("<number:currency-style style:name=\"test\" number:language=\"en\" number:country=\"US\" style:volatile=\"true\">"
				+ "<number:number number:decimal-places=\"5\" number:min-integer-digits=\"1\"/>"
				+ "<number:text> </number:text>"
				+ "<number:currency-symbol>" + this.usSymbol + "</number:currency-symbol>"
				+ "</number:currency-style>", sb.toString());
	}

	@Test
	public final void testEuro() throws IOException {
		final CurrencyStyle cs = this.factory
				.currencyStyleBuilder("currency-data").locale(Locale.FRANCE)
				.build();
		final StringBuilder sb = new StringBuilder();
		cs.appendXMLRepresentation(this.util, sb);
		DomTester.assertEquals(
				"<number:currency-style style:name=\"currency-data\" number:language=\"fr\" number:country=\"FR\" style:volatile=\"true\">"
						+ "<number:number number:decimal-places=\"2\" number:min-integer-digits=\"1\"/>"
						+ "<number:text> </number:text>"
						+ "<number:currency-symbol>" + this.frSymbol + "</number:currency-symbol>"
						+ "</number:currency-style>",
				sb.toString());
	}

	@Test
	public final void testGroupThousands() throws IOException {
		final CurrencyStyle ps = this.factory.currencyStyleBuilder("test")
				.groupThousands(true).build();
		final StringBuilder sb = new StringBuilder();
		ps.appendXMLRepresentation(this.util, sb);
		DomTester.assertEquals("<number:currency-style style:name=\"test\" number:language=\"en\" number:country=\"US\" style:volatile=\"true\">"
				+ "<number:number number:decimal-places=\"2\" number:min-integer-digits=\"1\" number:grouping=\"true\"/>"
				+ "<number:text> </number:text>"
				+ "<number:currency-symbol>" + this.usSymbol + "</number:currency-symbol>"
				+ "</number:currency-style>", sb.toString());
	}

	@Test
	public final void testMinIntegerDigits() throws IOException {
		final CurrencyStyle ps = this.factory.currencyStyleBuilder("test")
				.minIntegerDigits(8).build();
		final StringBuilder sb = new StringBuilder();
		ps.appendXMLRepresentation(this.util, sb);
		DomTester.assertEquals("<number:currency-style style:name=\"test\" number:language=\"en\" number:country=\"US\" style:volatile=\"true\">"
				+ "<number:number number:decimal-places=\"2\" number:min-integer-digits=\"8\"/>"
				+ "<number:text> </number:text>"
				+ "<number:currency-symbol>" + this.usSymbol + "</number:currency-symbol>"
				+ "</number:currency-style>", sb.toString());
	}

	@Test
	public final void testNegativeValueColor() throws IOException {
		final CurrencyStyle ps = this.factory.currencyStyleBuilder("test")
				.negativeValueColor(Color.GREEN).build();
		final StringBuilder sb = new StringBuilder();
		ps.appendXMLRepresentation(this.util, sb);
		DomTester.assertEquals("<number:currency-style style:name=\"test\" number:language=\"en\" number:country=\"US\" style:volatile=\"true\">"
				+ "<number:number number:decimal-places=\"2\" number:min-integer-digits=\"1\"/>"
				+ "<number:text> </number:text>"
				+ "<number:currency-symbol>" + this.usSymbol + "</number:currency-symbol>"
				+ "</number:currency-style>"
				+ "<number:currency-style style:name=\"test-neg\">"
				+ "<style:text-properties fo:color=\"#008000\"/>"
				+ "<number:text>-</number:text>"
				+ "<number:number number:decimal-places=\"2\" number:min-integer-digits=\"1\"/>"
				+ "<number:text> </number:text>"
				+ "<number:currency-symbol>USD</number:currency-symbol>"
				+ "<style:map style:condition=\"value()&gt;=0\" style:apply-style-name=\"test\"/>"
				+ "</number:currency-style>", sb.toString());
	}

	@Test
	public final void testNegativeValueRed() throws IOException {
		final CurrencyStyle ps = this.factory.currencyStyleBuilder("test")
				.negativeValueRed().build();
		final StringBuilder sb = new StringBuilder();
		ps.appendXMLRepresentation(this.util, sb);
		DomTester.assertEquals("<number:currency-style style:name=\"test\" number:language=\"en\" number:country=\"US\" style:volatile=\"true\">"
				+ "<number:number number:decimal-places=\"2\" number:min-integer-digits=\"1\"/>"
				+ "<number:text> </number:text>"
				+ "<number:currency-symbol>" + this.usSymbol + "</number:currency-symbol>"
				+ "</number:currency-style>"
				+ "<number:currency-style style:name=\"test-neg\">"
				+ "<style:text-properties fo:color=\"#FF0000\"/>"
				+ "<number:text>-</number:text>"
				+ "<number:number number:decimal-places=\"2\" number:min-integer-digits=\"1\"/>"
				+ "<number:text> </number:text>"
				+ "<number:currency-symbol>USD</number:currency-symbol>"
				+ "<style:map style:condition=\"value()&gt;=0\" style:apply-style-name=\"test\"/>"
				+ "</number:currency-style>", sb.toString());
	}

	@Test
	public final void testStylesElements() throws IOException {
		final CurrencyStyle cs = this.factory.currencyStyleBuilder("test")
				.locale(this.locale).negativeValueRed().build();
		final StringBuilder sb = new StringBuilder();
		cs.appendXMLRepresentation(this.util, sb);
		DomTester.assertEquals("<number:currency-style style:name=\"test\" number:language=\"en\" number:country=\"US\" style:volatile=\"true\">"
				+ "<number:number number:decimal-places=\"2\" number:min-integer-digits=\"1\"/>"
				+ "<number:text> </number:text>" + "<number:currency-symbol>"
				+ this.usSymbol + "</number:currency-symbol>"
				+ "</number:currency-style>"
				+ "<number:currency-style style:name=\"test-neg\">"
				+ "<style:text-properties fo:color=\"#FF0000\"/>"
				+ "<number:text>-</number:text>"
				+ "<number:number number:decimal-places=\"2\" number:min-integer-digits=\"1\"/>"
				+ "<number:text> </number:text>" + "<number:currency-symbol>"
				+ this.usSymbol + "</number:currency-symbol>"
				+ "<style:map style:condition=\"value()&gt;=0\" style:apply-style-name=\"test\"/>"
				+ "</number:currency-style>", sb.toString());
	}
}
