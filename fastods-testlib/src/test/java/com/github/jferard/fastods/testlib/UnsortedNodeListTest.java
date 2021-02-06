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
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.Iterator;

public class UnsortedNodeListTest {
    private Node s;
    private DocumentBuilder builder;
    private Node t;
    private UnsortedNodeList r;
    private UnsortedChildrenTester tester;

    @Before
    public void setUp() throws ParserConfigurationException, IOException, SAXException {
        this.tester = new UnsortedChildrenTester();
        final DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        this.builder = factory.newDocumentBuilder();
        final Document document = this.parse("<doc><r a='1' b='2' /><r c='3' /></doc>");
        this.r = new UnsortedNodeList(document.getElementsByTagName("r"));
    }

    private Document parse(final String xml) throws IOException, SAXException {
        return this.builder.parse(new ByteArrayInputStream(
                xml.getBytes(Util.UTF_8)));
    }


    @Test
    public void testSize() {
        Assert.assertEquals(2, this.r.size());
    }

    @Test
    public void testIterator() {
        final Iterator<Node> it = this.r.iterator();
        Assert.assertTrue(it.hasNext());
        it.next();
        Assert.assertTrue(it.hasNext());
        it.next();
        Assert.assertFalse(it.hasNext());
    }

    @Test
    public void testCompareDifferentLength() throws IOException, SAXException {
        final Document document = this.parse("<doc><r a='1' b='2' /><r c='3' /><r /></doc>");
        final UnsortedNodeList r2 = new UnsortedNodeList(document.getElementsByTagName("r"));
        Assert.assertEquals(-1, this.r.compareTo(r2));
        Assert.assertEquals(1, r2.compareTo(this.r));
        Assert.assertEquals(r2, r2);
        Assert.assertNotEquals(r2, this.r);
        Assert.assertNotEquals(null, r2);
        Assert.assertNotEquals("null", r2);
    }

    @Test
    public void testCompare() throws IOException, SAXException {
        final Document document = this.parse("<doc><r c='3' /><r a='1' b='2' /></doc>");
        final UnsortedNodeList r2 = new UnsortedNodeList(document.getElementsByTagName("r"));
        Assert.assertEquals(0, this.r.compareTo(r2));
        Assert.assertEquals(0, r2.compareTo(this.r));
        Assert.assertEquals(this.r, r2);
    }

    @Test
    public void testDifferent() throws IOException, SAXException {
        final Document document = this.parse("<doc><r c='4' /><r a='1' b='2' /></doc>");
        final UnsortedNodeList r2 = new UnsortedNodeList(document.getElementsByTagName("r"));
        Assert.assertEquals(-1, this.r.compareTo(r2));
        Assert.assertEquals(1, r2.compareTo(this.r));
        Assert.assertNotEquals(this.r, r2);
    }

    @Test
    public void testToString() {
        Assert.assertEquals("[[r: null], [r: null]]", this.r.toString());
        Assert.assertEquals("Node[name=r, value=null, attr=[c=\"3\"]]",
                UnsortedNodeList.toString(this.r.iterator().next()));
        Assert.assertEquals("[null]", UnsortedNodeList.toString(null));
    }

    @Test
    public void testHashcode() {
        final Iterator<Node> it = this.r.iterator();
        final int h = 31 * (31 + it.next().hashCode()) + it.next().hashCode();

        Assert.assertEquals(h, this.r.hashCode());
    }

    @Test
    public void testComparator() throws IOException, SAXException {
        final Document document = this.parse("<doc><r c='4' /><r a='1' b='2' /></doc>");

        final Node doc = document.getFirstChild();
        Assert.assertEquals(8, UnsortedNodeList.cmp.compare(document, doc));
        Assert.assertEquals(-8, UnsortedNodeList.cmp.compare(doc, document));


        final Document document2 = this.parse("<doc><r/><r a='1' b='2' /></doc>");
        Assert.assertNotEquals(doc.getFirstChild(), doc.getLastChild());
        Assert.assertEquals(-1,
                UnsortedNodeList.cmp.compare(doc.getFirstChild(), doc.getLastChild()));

        Assert.assertEquals(-1, UnsortedNodeList.cmp
                .compare(doc.getFirstChild(), doc.getLastChild().getAttributes().item(0)));
        Assert.assertEquals(1, UnsortedNodeList.cmp
                .compare(doc.getLastChild().getAttributes().item(0), doc.getFirstChild()));
    }

    @Test
    public void testComparator2() throws IOException, SAXException {
        final Document document = this.parse("<doc><r c='4' /><s c='1'/></doc>");

        final Node doc = document.getFirstChild();
        final Node item = doc.getFirstChild().getAttributes().item(0);
        final Node item2 = doc.getLastChild().getAttributes().item(0);

        Assert.assertEquals(0, UnsortedNodeList.cmp.compare(item, item2));
    }
}