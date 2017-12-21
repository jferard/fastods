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
import java.io.UnsupportedEncodingException;
import java.util.Iterator;

public class UnsortedNodeListTest {
    private static final String UTF_8 = "utf-8";
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
        final Document document = this.builder
                .parse(new ByteArrayInputStream(("<doc><r a='1' b='2' /><r c='3' /></doc>").getBytes(UTF_8)));
        this.r = new UnsortedNodeList(document.getElementsByTagName("r"));
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
        final Document document = this.builder
                .parse(new ByteArrayInputStream(("<doc><r a='1' b='2' /><r c='3' /><r /></doc>").getBytes(UTF_8)));
        final UnsortedNodeList r2 = new UnsortedNodeList(document.getElementsByTagName("r"));
        Assert.assertEquals(-1, this.r.compareTo(r2));
        Assert.assertEquals(1, r2.compareTo(this.r));
        Assert.assertTrue(r2.equals(r2));
        Assert.assertFalse(r2.equals(this.r));
        Assert.assertFalse(r2.equals(null));
        Assert.assertFalse(r2.equals("null"));
    }

    @Test
    public void testCompare() throws IOException, SAXException {
        final Document document = this.builder
                .parse(new ByteArrayInputStream(("<doc><r c='3' /><r a='1' b='2' /></doc>").getBytes(UTF_8)));
        final UnsortedNodeList r2 = new UnsortedNodeList(document.getElementsByTagName("r"));
        Assert.assertEquals(0, this.r.compareTo(r2));
        Assert.assertEquals(0, r2.compareTo(this.r));
        Assert.assertTrue(this.r.equals(r2));
    }

    @Test
    public void testDifferent() throws IOException, SAXException {
        final Document document = this.builder
                .parse(new ByteArrayInputStream(("<doc><r c='4' /><r a='1' b='2' /></doc>").getBytes(UTF_8)));
        final UnsortedNodeList r2 = new UnsortedNodeList(document.getElementsByTagName("r"));
        Assert.assertEquals(-1, this.r.compareTo(r2));
        Assert.assertEquals(1, r2.compareTo(this.r));
        Assert.assertFalse(this.r.equals(r2));
    }
}