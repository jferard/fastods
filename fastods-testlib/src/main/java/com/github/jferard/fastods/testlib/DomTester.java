/*
 * FastODS - A very fast and lightweight (no dependency) library for creating ODS
 *    (Open Document Spreadsheet, mainly for Calc) files in Java.
 *    It's a Martin Schulz's SimpleODS fork
 *    Copyright (C) 2016-2018 J. Férard <https://github.com/jferard>
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
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
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
    static Logger logger = Logger.getLogger("DomTester");

    public static void assertEquals(final String string1, final String string2) {
        DomTester.assertEquals(string1, string2, new SortedChildrenTester());
    }

    public static void assertUnsortedEquals(final String string1, final String string2) {
        DomTester.assertEquals(string1, string2, new UnsortedChildrenTester());
    }

    public static void assertEquals(final String string1, final String string2, final ChildrenTester childrenTester) {
        if (!DomTester.equals(string1, string2, childrenTester)) {
            Assert.assertEquals(string1, string2); // shows the difference
            Assert.fail(); // in case there was a bug in DomTester, but strings are equal
        }
    }

    public static void assertNotEquals(final String string1, final String string2) {
        DomTester.assertNotEquals(string1, string2, new SortedChildrenTester());
    }

    public static void assertUnsortedNotEquals(final String string1, final String string2) {
        DomTester.assertNotEquals(string1, string2, new UnsortedChildrenTester());
    }

    public static void assertNotEquals(final String string1, final String string2,
                                       final ChildrenTester childrenTester) {
        if (DomTester.equals(string1, string2, childrenTester)) {
            Assert.assertNotEquals(string1, string2); // shows the difference
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
            DomTester.logger.log(Level.SEVERE, "can't test equality between " + s1 + " and " + s2, e);
            return false;
        }
    }

    private final Charset UTF_8 = Charset.forName("UTF-8");
    private final DocumentBuilder builder;

    DomTester() throws ParserConfigurationException {
        final DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        this.builder = factory.newDocumentBuilder();
    }

    private boolean stringEquals(final String s1, final String s2,
                                 final ChildrenTester childrenTester) throws SAXException, IOException {
        final Document document1 = this.builder
                .parse(new ByteArrayInputStream(("<r>" + s1 + "</r>").getBytes(this.UTF_8)));
        final Document document2 = this.builder
                .parse(new ByteArrayInputStream(("<r>" + s2 + "</r>").getBytes(this.UTF_8)));
        final NodeList childNodes1 = document1.getDocumentElement().getChildNodes();
        final NodeList childNodes2 = document2.getDocumentElement().getChildNodes();
        if (childNodes1.getLength() != childNodes2.getLength()) {
            return false;
        }
        for (int n = 0; n < childNodes1.getLength(); n++) {
            final Node e1 = childNodes1.item(n);
            final Node e2 = childNodes2.item(n);
            if (!childrenTester.equals(e1, e2)) return false;
        }
        return true;
    }
}