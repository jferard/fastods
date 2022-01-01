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
import org.junit.Before;
import org.junit.Test;
import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.Iterator;

public class AttrListTest {
    private NamedNodeMap attributes;
    private DocumentBuilder builder;
    private AttrList attrList;

    @Before
    public void setUp() throws ParserConfigurationException, IOException, SAXException {
        final DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        this.builder = factory.newDocumentBuilder();
        final Document document =
                this.parse("<r a='1' b='2'/>");
        this.attributes = document.getElementsByTagName("r").item(0).getAttributes();
        this.attrList = AttrList.create(this.attributes);
    }

    @Test
    public void testStr() {
        Assert.assertEquals("[a=\"1\", b=\"2\"]", this.attrList.toString());
    }

    @Test
    public void testSize() {
        Assert.assertEquals(2, this.attrList.size());
    }

    @Test
    public void testIterator() {
        Assert.assertEquals("[a=\"1\", b=\"2\"]", this.attrList.toString());
        final Iterator<Attr> it = this.attrList.iterator();
        Assert.assertTrue(it.hasNext());
        Assert.assertEquals(this.attributes.getNamedItem("a"), it.next());
        Assert.assertTrue(it.hasNext());
        Assert.assertEquals(this.attributes.getNamedItem("b"), it.next());
    }

    @Test
    public void testEquals() throws IOException, SAXException {
        Assert.assertEquals(this.attrList, this.attrList);
        Assert.assertNotEquals("attrList", this.attrList);
        Assert.assertNotEquals(this.attrList, "attrList");

        final Document document =
                this.parse("<s b='2' a='1'/>");
        final NamedNodeMap attributes2 = document.getElementsByTagName("s").item(0).getAttributes();
        final AttrList attrList2 = AttrList.create(attributes2);
        Assert.assertEquals(attrList2, this.attrList);
        Assert.assertEquals(attrList2.hashCode(), this.attrList.hashCode());
    }

    @Test
    public void testCompare() throws IOException, SAXException {
        final Document document =
                this.parse("<s b='3' a='1'/>");
        final NamedNodeMap attributes2 = document.getElementsByTagName("s").item(0).getAttributes();
        final AttrList attrList2 = AttrList.create(attributes2);
        Assert.assertEquals(0, this.attrList.compareTo(this.attrList));
        Assert.assertEquals(1, attrList2.compareTo(this.attrList));
        Assert.assertEquals(-1, this.attrList.compareTo(attrList2));
    }

    @Test(expected = NullPointerException.class)
    public void testCompareWithNull() {
        Assert.assertEquals(1, this.attrList.compareTo(null));
    }

    @Test
    public void testCompareWithDifferentSizes() throws IOException, SAXException {
        final Document document = this.parse("<s c='3' b='2' a='1'/>");
        final NamedNodeMap attributes2 = document.getElementsByTagName("s").item(0).getAttributes();
        final AttrList attrList2 = AttrList.create(attributes2);
        Assert.assertEquals(1, attrList2.compareTo(this.attrList));
        Assert.assertNotEquals(attrList2.hashCode(), this.attrList.hashCode());
    }

    @Test
    public void testNull() throws IOException, SAXException {
        final Document document = this.parse("<s c='3' b='2' a='1'/>");
        final NamedNodeMap attributes2 = document.getElementsByTagName("s").item(0).getAttributes();
        final AttrList attrList2 = AttrList.create(attributes2);
        final AttrList attrList3 = new AttrList(Arrays.<Attr>asList(null, null, null));
        final AttrList attrList4 = new AttrList(Arrays.<Attr>asList(null, null, null));
        Assert.assertEquals(-1, attrList2.compareTo(attrList3));
        Assert.assertEquals(1, attrList3.compareTo(attrList2));
        Assert.assertEquals(0, attrList3.compareTo(attrList3));
        Assert.assertEquals(0, attrList3.compareTo(attrList4));
    }

    private Document parse(final String xml) throws IOException, SAXException {
        return this.builder.parse(new ByteArrayInputStream(xml.getBytes(Util.UTF_8)));
    }

    @Test
    public void testHashCode() {
        Assert.assertEquals(2, this.attrList.hashCode());
    }
}

