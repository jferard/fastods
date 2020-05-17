/*
 * FastODS - A very fast and lightweight (no dependency) library for creating ODS
 *    (Open Document Spreadsheet, mainly for Calc) files in Java.
 *    It's a Martin Schulz's SimpleODS fork
 *    Copyright (C) 2016-2020 J. Férard <https://github.com/jferard>
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

public class UnsortedChildrenTesterTest {
    private static final String UTF_8 = "utf-8";
    private Node s;
    private DocumentBuilder builder;
    private Node t;
    private Node r;
    private UnsortedChildrenTester tester;

    @Before
    public void setUp() throws ParserConfigurationException, IOException, SAXException {
        this.tester = new UnsortedChildrenTester();
        final DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        this.builder = factory.newDocumentBuilder();
        final Document document = this.builder
                .parse(new ByteArrayInputStream(("<r a='1' b='2'><s/><t/></r>").getBytes(UTF_8)));
        this.r = document.getFirstChild();
    }

    @Test
    public void testEquals() throws IOException, SAXException {
        PowerMock.resetAll();

        PowerMock.replayAll();
        final Node r2 = this.getNode("<r a='1' b='2'><s/><t/></r>");
        Assert.assertTrue(this.tester.childrenEquals(this.r, r2));

        PowerMock.verifyAll();
    }

    @Test
    public void testNotEquals() throws IOException, SAXException {
        PowerMock.resetAll();

        PowerMock.replayAll();
        final Node r2 = this.getNode("<r a='1' b='2'><u/><v/></r>");
        Assert.assertFalse(this.tester.childrenEquals(this.r, r2));

        PowerMock.verifyAll();
    }

    @Test
    public void testChildrenEquals() throws IOException, SAXException {
        PowerMock.resetAll();

        PowerMock.replayAll();
        final Node x = this.getNode("<x><r/></x>");
        final Node y = this.getNode("<y><s/><t/></y>");
        final Node z = this.getNode("<z><s/><u/></z>");
        final Node a = this.getNode("<a><u/><s/></a>");
        Assert.assertFalse(this.tester.childrenEquals(x, y));
        Assert.assertFalse(this.tester.childrenEquals(y, z));
        Assert.assertTrue(this.tester.childrenEquals(z, a));

        PowerMock.verifyAll();
    }

    @Test
    public void testAttributesEquals2() throws IOException, SAXException {
        PowerMock.resetAll();

        PowerMock.replayAll();
        final Node r = this.getNode("<r a=\"1\"/>");
        final Node s = this.getNode("<s a=\"1\"/>");
        final Node ra0 = r.getAttributes().item(0);
        final Node sa0 = s.getAttributes().item(0);
        Assert.assertNotNull(sa0);
        Assert.assertFalse(this.tester.attributesEquals(r, sa0));
        Assert.assertFalse(this.tester.attributesEquals(ra0, s));
        Assert.assertTrue(this.tester.attributesEquals(ra0, ra0));
        Assert.assertTrue(this.tester.attributesEquals(ra0, sa0));

        PowerMock.replayAll();
    }

    private Node getNode(final String s) throws SAXException, IOException {
        final Document document = this.builder.parse(new ByteArrayInputStream(s.getBytes(UTF_8)));
        return document.getFirstChild();
    }
}