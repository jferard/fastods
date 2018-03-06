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

import org.easymock.EasyMock;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.powermock.api.easymock.PowerMock;

import javax.xml.parsers.ParserConfigurationException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 */
public class DomTesterTest {
    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Before
    public void setUp() throws ParserConfigurationException {
        UnsortedChildrenTester.logger = PowerMock.createMock(Logger.class);
        DomTester.logger = PowerMock.createMock(Logger.class);
    }

    @Test
    public void testEmpty() {
        PowerMock.resetAll();
        PowerMock.replayAll();
        DomTester.assertEquals("", "");
        PowerMock.verifyAll();
    }

    @Test
    public void testSelfClosing() {
        PowerMock.resetAll();
        PowerMock.replayAll();
        DomTester.assertEquals("<a/>", "<a></a>");

        PowerMock.verifyAll();
    }

    @Test
    public void testAttributes() {
        PowerMock.resetAll();
        PowerMock.replayAll();
        DomTester.assertEquals("<a b=\"1\" c=\"2\"/>", "<a b=\"1\" c=\"2\"/>");
        DomTester.assertEquals("<a b=\"1\" c=\"2\"/>", "<a c=\"2\" b=\"1\"/>");
        DomTester.assertNotEquals("<a b=\"2\" c=\"1\"/>", "<a c=\"2\" b=\"1\"/>");

        Assert.assertTrue(DomTester.equals("<a b=\"1\" c=\"2\"/>", "<a b=\"1\" c=\"2\"/>"));
        PowerMock.verifyAll();
    }

    @Test
    public void testAttributesFail() {
        PowerMock.resetAll();
        PowerMock.replayAll();
        this.thrown.expect(AssertionError.class);
        this.thrown.expectMessage("Values should be different. Actual: <a b=\"1\" c=\"2\"/>");
        DomTester.assertNotEquals("<a b=\"1\" c=\"2\"/>", "<a b=\"1\" c=\"2\"/>");

        PowerMock.verifyAll();
    }

    @Test
    public void testAttributesFail2() {
        PowerMock.resetAll();
        PowerMock.replayAll();
        this.thrown.expect(AssertionError.class);
        DomTester.assertNotEquals("<a b=\"1\" c=\"2\"/>", "<a c=\"2\" b=\"1\"/>");

        PowerMock.verifyAll();
    }

    @Test
    public void testAttributesFail3() {
        PowerMock.resetAll();
        PowerMock.replayAll();
        this.thrown.expect(AssertionError.class);
        this.thrown.expectMessage("expected:<<a [b=\"2\" c]=\"1\"/>> but was:<<a [c=\"2\" b]=\"1\"/>>");
        DomTester.assertEquals("<a b=\"2\" c=\"1\"/>", "<a c=\"2\" b=\"1\"/>");

        PowerMock.verifyAll();
    }

    @Test
    public void testOrderFail() {
        PowerMock.resetAll();
        PowerMock.replayAll();
        this.thrown.expect(AssertionError.class);
        this.thrown.expectMessage("expected:<<a><[b/><c]/></a>> but was:<<a><[c/><d]/></a>>");
        DomTester.assertUnsortedEquals("<a><b/><c/></a>", "<a><c/><d/></a>");

        PowerMock.verifyAll();
    }

    @Test
    public void testSortedChildren() {
        PowerMock.resetAll();
        PowerMock.replayAll();
        DomTester.assertEquals("<a><b/><c/></a>", "<a><b></b><c></c></a>");

        PowerMock.verifyAll();
    }

    @Test
    public void testSortedChildren2() {
        PowerMock.resetAll();
        UnsortedChildrenTester.logger.info("Different children [b: null] vs [c: null]");
        EasyMock.expectLastCall().times(2);

        PowerMock.replayAll();
        DomTester.assertNotEquals("<a><b/><c/></a>", "<a><c/><b/></a>");

        PowerMock.verifyAll();
    }

    @Test
    public void testUnsortedChildren() {
        PowerMock.resetAll();
        PowerMock.replayAll();
        DomTester.assertUnsortedEquals("<a><b/><c/></a>", "<a><b></b><c></c></a>");
        DomTester.assertUnsortedEquals("<a><b><x/><y/></b><c/></a>", "<a><c/><b><y/><x/></b></a>");
        DomTester.assertUnsortedEquals("<a><b/><c/></a>", "<a><c/><b/></a>");

        PowerMock.verifyAll();
    }

    @Test
    public void testEuro() {
        PowerMock.resetAll();
        PowerMock.replayAll();
        DomTester.assertEquals("<a b=\"€\"/>", "<a b=\"€\"/>");

        PowerMock.verifyAll();
    }

    @Test
    public void testUnsorted() {
        PowerMock.resetAll();
        UnsortedChildrenTester.logger.info("Different children [b: null] vs [c: null]");
        EasyMock.expectLastCall().times(2);

        PowerMock.replayAll();
        DomTester.assertNotEquals("<a><b/><c/></a>", "<a><c/><b/></a>");

        PowerMock.verifyAll();
    }

    @Test
    public void testUnsorted2() {
        PowerMock.resetAll();
        PowerMock.replayAll();
        DomTester.assertUnsortedEquals("<a><b/><c/></a>", "<a><c/><b/></a>");
        DomTester.assertUnsortedNotEquals("<a><b/><c/></a>", "<a><c/><d/></a>");
        DomTester.assertEquals("<a><b/><c/></a>", "<a><c/><b/></a>", new UnsortedChildrenTester());
        Assert.assertTrue(DomTester.unsortedEquals("<a><b/><c/></a>", "<a><c/><b/></a>"));

        PowerMock.verifyAll();
    }

    @Test
    public void testUnsorted3() {
        PowerMock.resetAll();
        UnsortedChildrenTester.logger.info("Different children [b: null] vs [c: null]");
        EasyMock.expectLastCall().times(2);

        PowerMock.replayAll();
        DomTester.assertNotEquals("<a><b/><c/></a>", "<a><c/><b/></a>", new SortedChildrenTester());

        PowerMock.verifyAll();
    }

    @Test
    public void testBadFormat() {
        PowerMock.resetAll();
        DomTester.logger.log(EasyMock.eq(Level.SEVERE), EasyMock.anyString(), EasyMock.isA(Throwable.class));

        PowerMock.replayAll();
        Assert.assertFalse(DomTester.equals("<a b=\"1\" c=\"2\"/>", "<a b=\"1\" c=\"2\">"));

        PowerMock.verifyAll();
    }

    @Test
    public void testTwoRoots() {
        PowerMock.resetAll();
        PowerMock.replayAll();
        DomTester.assertNotEquals("<a b=\"1\"/><a b=\"2\"/>", "<a b=\"1\"/>");

        PowerMock.verifyAll();
    }

    @Test(expected = AssertionError.class)
    public void bug() {
        final SortedChildrenTester tester = new SortedChildrenTester();

        PowerMock.resetAll();
        PowerMock.replayAll();
        DomTester.assertEquals("<a/>", "<b/>", tester);

        PowerMock.verifyAll();

    }
}