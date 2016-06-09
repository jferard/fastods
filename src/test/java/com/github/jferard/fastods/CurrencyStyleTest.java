/*
 * FastODS - a Martin Schulz's SimpleODS fork
 *    Copyright (C) 2016 J. Férard
 * SimpleODS - A lightweight java library to create simple OpenOffice spreadsheets
 *    Copyright (C) 2008-2013 Martin Schulz <mtschulz at users.sourceforge.net>
 *
 *    This program is free software: you can redistribute it and/or modify
 *    it under the terms of the GNU General Public License as published by
 *    the Free Software Foundation, either version 3 of the License, or
 *    (at your option) any later version.
 *
 *    This program is distributed in the hope that it will be useful,
 *    but WITHOUT ANY WARRANTY; without even the implied warranty of
 *    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *    GNU General Public License for more details.
 *
 *    You should have received a copy of the GNU General Public License
 *    along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.github.jferard.fastods;

import java.io.IOException;
import java.util.Locale;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.github.jferard.fastods.datastyle.CurrencyStyle;
import com.github.jferard.fastods.datastyle.DataStyleBuilderFactory;
import com.github.jferard.fastods.style.TableCellStyle;
import com.github.jferard.fastods.util.FastOdsXMLEscaper;
import com.github.jferard.fastods.util.XMLUtil;

/**
 * @author Julien Férard Copyright (C) 2016 J. Férard Copyright 2008-2013 Martin
 *         Schulz <mtschulz at users.sourceforge.net>
 *
 *         This file BenchIT.java is part of FastODS.
 */
public class CurrencyStyleTest {
	private XMLUtil util;
	private Locale locale;
	private DataStyleBuilderFactory factory;

	@Before
	public void setUp() {
		this.util = new XMLUtil(new FastOdsXMLEscaper());
		this.locale = Locale.US;
		this.factory = new DataStyleBuilderFactory(this.locale);
	}

	@Test
	public final void testEuro() throws IOException {
		CurrencyStyle cs = this.factory.currencyStyleBuilder("currency-data")
				.locale(Locale.FRANCE).build();
		StringBuilder sb = new StringBuilder();
		cs.appendXMLToStylesEntry(this.util, sb);
		Assert.assertEquals(
				"<number:currency-style style:name=\"currency-data\">"
						+ "<number:number number:decimal-places=\"2\" number:min-integer-digits=\"1\"/>"
						+ "<number:text> </number:text>"
						+ "<number:currency-symbol>€</number:currency-symbol>"
						+ "</number:currency-style>",
				sb.toString());
	}

	@Test
	public final void testStylesEntry() throws IOException {
		CurrencyStyle cs = this.factory.currencyStyleBuilder("test")
				.locale(this.locale).negativeValueRed().build();
		StringBuilder sb = new StringBuilder();
		cs.appendXMLToStylesEntry(this.util, sb);
		Assert.assertEquals("<number:currency-style style:name=\"test\">"
				+ "<number:number number:decimal-places=\"2\" number:min-integer-digits=\"1\"/>"
				+ "<number:text> </number:text>" + "<number:currency-symbol>"
				+ "USD" + "</number:currency-symbol>"
				+ "</number:currency-style>"
				+ "<number:currency-style style:name=\"test-neg\">"
				+ "<style:text-properties fo:color=\"#FF0000\"/>"
				+ "<number:text>-</number:text>"
				+ "<number:number number:decimal-places=\"2\" number:min-integer-digits=\"1\"/>"
				+ "<number:text> </number:text>" + "<number:currency-symbol>"
				+ "USD" + "</number:currency-symbol>"
				+ "<style:map style:condition=\"value()&gt;=0\" style:apply-style-name=\"test\"/>"
				+ "</number:currency-style>", sb.toString());
	}
}
