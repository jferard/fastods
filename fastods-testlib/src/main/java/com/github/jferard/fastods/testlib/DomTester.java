/*
 * FastODS - A very fast and lightweight (no dependency) library for creating ODS
 *    (Open Document Spreadsheet, mainly for Calc) files in Java.
 *    It's a Martin Schulz's SimpleODS fork
 *    Copyright (C) 2016-2023 J. Férard <https://github.com/jferard>
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
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License
 *  for more details.
 *
 * You should have received a copy of the GNU General Public License along with
 * this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.github.jferard.fastods.testlib;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;

/**
 * A tester for nodes
 *
 * @author Julien Férard
 */
public class DomTester {
    private final DocumentBuilder builder;

    /**
     * Create a tester
     *
     * @throws ParserConfigurationException in case of service configuration error or if the
     *                                      implementation is not available or cannot be
     *                                      instantiated.
     */
    DomTester() throws ParserConfigurationException {
        final DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        this.builder = factory.newDocumentBuilder();
    }

    /**
     * Assert that two XML strings are equal
     *
     * @param expected the expected string
     * @param actual   the actual string
     */
    public static void assertEquals(final String expected, final String actual) {
        DomTester.assertEquals(expected, actual, new SortedChildrenTester());
    }

    /**
     * Assert that two XML strings are equal
     *
     * @param expected the expected string
     * @param actual   the actual string
     */
    public static void assertUnsortedEquals(final String expected, final String actual) {
        DomTester.assertEquals(expected, actual, new UnsortedChildrenTester());
    }

    /**
     * Assert that two XML strings are equal
     *
     * @param expected       the expected string
     * @param actual         the actual string
     * @param childrenTester a custom tester
     */
    public static void assertEquals(final String expected, final String actual,
                                    final ChildrenTester childrenTester) {
        if (!DomTester.equals(expected, actual, childrenTester)) {
            assert childrenTester.getFirstDifference() != null;
            final String msg =
                    "XML are different.\n" +
                            "Expected was: " + expected + "\n" +
                            "  Actual was: " + actual + "\n" +
                            childrenTester.getFirstDifference();
            throw new AssertionError(msg);
        }
    }

    /**
     * Assert that two XML strings are different
     *
     * @param expected the expected string
     * @param actual   the actual string
     */
    public static void assertNotEquals(final String expected, final String actual) {
        DomTester.assertNotEquals(expected, actual, new SortedChildrenTester());
    }

    /**
     * Assert that two XML strings are different
     *
     * @param expected the expected string
     * @param actual   the actual string
     */
    public static void assertUnsortedNotEquals(final String expected, final String actual) {
        DomTester.assertNotEquals(expected, actual, new UnsortedChildrenTester());
    }

    /**
     * Assert that two XML strings are different
     *
     * @param expected       the expected string
     * @param actual         the actual string
     * @param childrenTester a custom tester
     */
    public static void assertNotEquals(final String expected, final String actual,
                                       final ChildrenTester childrenTester) {
        if (DomTester.equals(expected, actual, childrenTester)) {
            throw new AssertionError(
                    String.format("XML contents %s and %s where equal", getEllipsis(expected),
                            getEllipsis(actual)));
        }
    }

    private static String getEllipsis(final String str) {
        final int length = str.length();
        if (length < 20) {
            return str;
        } else {
            return str.substring(0, 20) + "...";
        }
    }

    /**
     * @param s1 the first XML string
     * @param s2 the second XML string
     * @return true if the XML tree is equal
     */
    public static boolean equals(final String s1, final String s2) {
        return DomTester.equals(s1, s2, new SortedChildrenTester());
    }

    /**
     * The XML elements don't need to be sorted
     *
     * @param s1 the first XML string
     * @param s2 the second XML string
     * @return true if the XML tree is equal
     */
    public static boolean unsortedEquals(final String s1, final String s2) {
        return DomTester.equals(s1, s2, new UnsortedChildrenTester());
    }

    /**
     * @param s1             the first XML string
     * @param s2             the second XML string
     * @param childrenTester a custom tester
     * @return true if the XML tree is equal
     */
    public static boolean equals(final String s1, final String s2,
                                 final ChildrenTester childrenTester) {
        try {
            final DomTester tester = new DomTester();
            return tester.stringEquals(s1, s2, childrenTester);
        } catch (final Throwable e) {
            childrenTester.setFirstDifference(e.toString());
        }
        return false;
    }

    private boolean stringEquals(final String s1, final String s2,
                                 final ChildrenTester childrenTester)
            throws IOException, SAXException {
        final Document document1 = this.parse(this.wrapXML(s1));
        final Document document2 = this.parse(this.wrapXML(s2));
        final Element element1 = document1.getDocumentElement();
        final Element element2 = document2.getDocumentElement();
        return childrenTester.equals(element1, element2);
    }

    private Document parse(final ByteArrayInputStream is) throws IOException, SAXException {
        final PrintStream errBkp = System.err;
        System.setErr(new PrintStream(new OutputStream() {
            @Override
            public void write(final int b) {
                // pass
            }
        }) {
        });
        try {
            return this.builder.parse(is);
        } finally {
            System.setErr(errBkp);
        }
    }

    private ByteArrayInputStream wrapXML(final String s) {
        final String wrapped;
        if (s.startsWith("<?xml")) {
            wrapped = s;
        } else {
            wrapped = Util.XML_PROLOG + "<domtesterroot>" + s + "</domtesterroot>";
        }
        return new ByteArrayInputStream((wrapped).getBytes(StandardCharsets.UTF_8));
    }
}