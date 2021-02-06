/*
 * FastODS - A very fast and lightweight (no dependency) library for creating ODS
 *    (Open Document Spreadsheet, mainly for Calc) files in Java.
 *    It's a Martin Schulz's SimpleODS fork
 *    Copyright (C) 2016-2021 J. Férard <https://github.com/jferard>
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
import org.junit.Before;
import org.junit.Test;
import org.powermock.api.easymock.PowerMock;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.ByteArrayInputStream;
import java.io.IOException;

public class SortedChildrenTesterTest {
    private ChildrenTester tester;
    private DocumentBuilder builder;

    @Before
    public void setUp() throws ParserConfigurationException {
        this.tester = new SortedChildrenTester();
        final DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        this.builder = factory.newDocumentBuilder();
    }

    @Test
    public void testAttributesEquals() throws IOException, SAXException {
        PowerMock.resetAll();

        PowerMock.replayAll();
        final Node r = this.getNode("<r/>");
        final Node s = this.getNode("<s/>");
        final Node t = this.getNode("<t a=\"1\" b=\"2\"/>");
        final Node u = this.getNode("<u a=\"1\" b=\"2\"/>");
        final Node v = this.getNode("<v b=\"2\" a=\"1\"/>");
        Assert.assertTrue(this.tester.attributesEquals(r, s));
        Assert.assertTrue(this.tester.attributesEquals(r, s));
        Assert.assertFalse(this.tester.attributesEquals(r, t));
        Assert.assertFalse(this.tester.attributesEquals(u, s));
        Assert.assertTrue(this.tester.attributesEquals(t, u));
        Assert.assertTrue(this.tester.attributesEquals(t, v));
    }

    @Test
    public void testAttributesEquals2() throws IOException, SAXException {
        final Node r = this.getNode("<r a=\"1\"/>");
        final Node s = this.getNode("<s a=\"1\"/>");
        final Node ra0 = r.getAttributes().item(0);
        final Node sa0 = s.getAttributes().item(0);
        Assert.assertNotNull(sa0);
        Assert.assertFalse(this.tester.attributesEquals(r, sa0));
        Assert.assertFalse(this.tester.attributesEquals(ra0, s));
        Assert.assertTrue(this.tester.attributesEquals(ra0, ra0));
        Assert.assertTrue(this.tester.attributesEquals(ra0, sa0));
    }

    @Test
    public void testChildrenEquals() throws IOException, SAXException {
        final Node x = this.getNode("<x><r/></x>");
        final Node y = this.getNode("<y><s/><t/></y>");
        final Node z = this.getNode("<z><s/><u/></z>");
        // TODO: make a test!
    }

    @Test
    public void testEquals() throws IOException, SAXException {
        final Node r = this.getNode("<ns:r/>");
        final Node s = this.getNode("<ns:r/>");
        final Node t = this.getNode("<ns:t/>");
        final Node u = this.getNode("<nsu:r/>");
        Assert.assertTrue(this.tester.equals(null, null));
        Assert.assertFalse(this.tester.equals(null, r));
        Assert.assertFalse(this.tester.equals(r, null));
        Assert.assertTrue(this.tester.equals(r, r));
        Assert.assertTrue(this.tester.equals(r, s));
        Assert.assertFalse(this.tester.equals(r, t));
        Assert.assertFalse(this.tester.equals(r, u));
    }

    private Node getNode(final String s) throws SAXException, IOException {
        final Document document =
                this.builder.parse(new ByteArrayInputStream(s.getBytes(Util.UTF_8)));
        return document.getFirstChild();
    }
}