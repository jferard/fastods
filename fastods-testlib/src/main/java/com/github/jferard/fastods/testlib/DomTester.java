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
package com.github.jferard.fastods.testlib;

import org.junit.Assert;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DomTester {
	final static Logger logger = Logger.getLogger("DomTester");
	private final Charset UTF_8 = Charset.forName("UTF-8");;

	public static void assertEquals(final String string1,
									final String string2) {
		if (!DomTester.equals(string1, string2, new SortedChildrenTester())) {
			Assert.assertEquals(string1, string2); // shows the difference
			Assert.fail(); // in case there was a bug in DomTester, but strings are equal
		}
	}

	public static void assertUnsortedEquals(final String string1,
									final String string2) {
		if (!DomTester.equals(string1, string2, new UnsortedChildrenTester())) {
			Assert.assertEquals(string1, string2); // shows the difference
			Assert.fail(); // in case there was a bug in DomTester, but strings are equal
		}
	}

	public static void assertEquals(final String string1,
									final String string2, final ChildrenTester childrenTester) {
		if (!DomTester.equals(string1, string2, childrenTester)) {
			Assert.assertEquals(string1, string2); // shows the difference
			Assert.fail(); // in case there was a bug in DomTester, but strings are equal
		}
	}

	public static boolean equals(final String s1, final String s2) {
		return DomTester.equals(s1, s2, new SortedChildrenTester());
	}

	public static boolean unsortedEquals(final String s1, final String s2) {
		return DomTester.equals(s1, s2, new UnsortedChildrenTester());
	}

	public static boolean equals(final String s1, final String s2, final ChildrenTester childrenTester) {
		try {
			final DomTester tester = new DomTester();
			return tester.stringEquals(s1, s2, childrenTester);
		} catch (final Throwable e) {
			DomTester.logger.log(Level.SEVERE, "can't test equality between "+s1+" and "+s2, e);
			return false;
		}
	}

	private final DocumentBuilder builder;

	DomTester() throws ParserConfigurationException {
		final DocumentBuilderFactory factory = DocumentBuilderFactory
				.newInstance();
		this.builder = factory.newDocumentBuilder();
	}

	private boolean stringEquals(final String s1, final String s2, final ChildrenTester childrenTester)
			throws SAXException, IOException {
		final Document document1 = this.builder.parse(
				new ByteArrayInputStream(("<r>" + s1 + "</r>").getBytes(UTF_8)));
		final Document document2 = this.builder.parse(
				new ByteArrayInputStream(("<r>" + s2 + "</r>").getBytes(UTF_8)));
		return childrenTester.equals(document1.getDocumentElement().getFirstChild(),
				document2.getDocumentElement().getFirstChild());
	}

	public static void assertNotEquals(final String string1, final String string2) {
		if (DomTester.equals(string1, string2, new SortedChildrenTester())) {
			Assert.assertNotEquals(string1, string2); // shows the difference
			Assert.fail(); // in case there was a bug in DomTester, but strings are equal
		}
	}

	public static void assertUnsortedNotEquals(final String string1, final String string2) {
		if (DomTester.equals(string1, string2, new UnsortedChildrenTester())) {
			Assert.assertNotEquals(string1, string2); // shows the difference
			Assert.fail(); // in case there was a bug in DomTester, but strings are equal
		}
	}
}