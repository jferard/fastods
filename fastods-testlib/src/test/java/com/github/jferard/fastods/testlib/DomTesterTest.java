/*
 * FastODS - A very fast and lightweight (no dependency) library for creating ODS
 *    (Open Document Spreadsheet, mainly for Calc) files in Java.
 *    It's a Martin Schulz's SimpleODS fork
 *    Copyright (C) 2016-2022 J. Férard <https://github.com/jferard>
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

import org.junit.Assert;
import org.junit.Test;

/**
 *
 */
public class DomTesterTest {
    @Test
    public void testEmpty() {
        DomTester.assertEquals("", "");
    }

    @Test
    public void testSelfClosing() {
        DomTester.assertEquals("<a/>", "<a></a>");
    }

    @Test
    public void testAttributes() {
        DomTester.assertEquals("<a b=\"1\" c=\"2\"/>", "<a b=\"1\" c=\"2\"/>");
        DomTester.assertEquals("<a b=\"1\" c=\"2\"/>", "<a c=\"2\" b=\"1\"/>");
        DomTester.assertNotEquals("<a b=\"2\" c=\"1\"/>", "<a c=\"2\" b=\"1\"/>");
        Assert.assertTrue(DomTester.equals("<a b=\"1\" c=\"2\"/>", "<a b=\"1\" c=\"2\"/>"));
    }

    @Test
    public void testAttributesFail() {
        Assert.assertThrows(
                "XML contents <a b=\"1\" c=\"2\"/> and <a b=\"1\" c=\"2\"/> " + "where equal",
                AssertionError.class,
                () -> DomTester.assertNotEquals("<a b=\"1\" c=\"2\"/>", "<a b=\"1\" c=\"2\"/>"));
    }

    @Test
    public void testAttributesFail2() {
        Assert.assertThrows(AssertionError.class,
                () -> DomTester.assertNotEquals("<a b=\"1\" c=\"2\"/>", "<a c=\"2\" b=\"1\"/>"));
    }

    @Test
    public void testAttributesFail3() {
        Assert.assertThrows(
                "Different attributes: a vs a ([b=\"2\", c=\"1\"] vs [b=\"1\", c=\"2\"]",
                AssertionError.class,
                () -> DomTester.assertEquals("<a b=\"2\" c=\"1\"/>", "<a c=\"2\" b=\"1\"/>"));
    }

    @Test
    public void testOrderFail() {
        Assert.assertThrows("Different nodes names: a/b vs a/c", AssertionError.class,
                () -> DomTester.assertUnsortedEquals("<a><b/><c/></a>", "<a><c/><d/></a>"));
    }

    @Test
    public void testSortedChildren() {
        DomTester.assertEquals("<a><b/><c/></a>", "<a><b></b><c></c></a>");
    }

    @Test
    public void testSortedChildren2() {
        DomTester.assertNotEquals("<a><b/><c/></a>", "<a><c/><b/></a>");
    }

    @Test
    public void testUnsortedChildren() {
        DomTester.assertUnsortedEquals("<a><b/><c/></a>", "<a><b></b><c></c></a>");
        DomTester.assertUnsortedEquals("<a><b><x/><y/></b><c/></a>", "<a><c/><b><y/><x/></b></a>");
        DomTester.assertUnsortedEquals("<a><b/><c/></a>", "<a><c/><b/></a>");
    }

    @Test
    public void testEuro() {
        DomTester.assertEquals("<a b=\"€\"/>", "<a b=\"€\"/>");
    }

    @Test
    public void testUnsorted() {
        DomTester.assertNotEquals("<a><b/><c/></a>", "<a><c/><b/></a>");
    }

    @Test
    public void testUnsorted2() {
        DomTester.assertUnsortedEquals("<a><b/><c/></a>", "<a><c/><b/></a>");
        DomTester.assertUnsortedNotEquals("<a><b/><c/></a>", "<a><c/><d/></a>");
        DomTester.assertEquals("<a><b/><c/></a>", "<a><c/><b/></a>", new UnsortedChildrenTester());
        Assert.assertTrue(DomTester.unsortedEquals("<a><b/><c/></a>", "<a><c/><b/></a>"));
    }

    @Test
    public void testSorted() {
        DomTester.assertNotEquals("<a><b/><c/></a>", "<a><c/><b/></a>", new SortedChildrenTester());
    }

    @Test
    public void testBadFormat() {
        Assert.assertFalse(DomTester.equals("<a b=\"1\" c=\"2\"/>", "<a b=\"1\" c=\"2\">"));
    }

    @Test
    public void testTwoRoots() {
        DomTester.assertNotEquals("<a b=\"1\"/><a b=\"2\"/>", "<a b=\"1\"/>");
    }

    @Test
    public void testDifferentNodes() {
        final SortedChildrenTester tester = new SortedChildrenTester();
        Assert.assertThrows("Expected was: <a/>\n" +
                        "  Actual was: <b/>\n" +
                        "Different nodes names: a vs b", AssertionError.class,
                () -> DomTester.assertEquals("<a/>", "<b/>", tester));
    }
}