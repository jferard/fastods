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
import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Iterator;

public class AttrListTest {
    private static final String UTF_8 = "utf-8";
    private NamedNodeMap attributes;
    private DocumentBuilder builder;
    private AttrList attrList;

    @Before
    public void setUp() throws ParserConfigurationException, IOException, SAXException {
        final DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        this.builder = factory.newDocumentBuilder();
        final Document document = this.builder.parse(new ByteArrayInputStream(("<r a='1' b='2'/>").getBytes(UTF_8)));
        this.attributes = document.getElementsByTagName("r").item(0).getAttributes();
        this.attrList = new AttrList(this.attributes);
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

        final Document document = this.builder.parse(new ByteArrayInputStream(("<s b='2' a='1'/>").getBytes(UTF_8)));
        final NamedNodeMap attributes2 = document.getElementsByTagName("s").item(0).getAttributes();
        final AttrList attrList2 = new AttrList(attributes2);
        Assert.assertEquals(attrList2, this.attrList);
        Assert.assertEquals(attrList2.hashCode(), this.attrList.hashCode());
    }

    @Test
    public void testCompare() throws IOException, SAXException {
        final Document document = this.builder.parse(new ByteArrayInputStream(("<s b='3' a='1'/>").getBytes(UTF_8)));
        final NamedNodeMap attributes2 = document.getElementsByTagName("s").item(0).getAttributes();
        final AttrList attrList2 = new AttrList(attributes2);
        Assert.assertEquals(0, this.attrList.compareTo(this.attrList));
        Assert.assertEquals(1, attrList2.compareTo(this.attrList));
        Assert.assertEquals(-1, this.attrList.compareTo(attrList2));
    }

    @Test(expected=NullPointerException.class)
    public void testCompareWithNull() throws IOException, SAXException {
        Assert.assertEquals(1, this.attrList.compareTo(null));
    }

    @Test
    public void testCompareWithDifferentSizes() throws IOException, SAXException {
        final Document document = this.builder.parse(new ByteArrayInputStream(("<s c='3' b='2' a='1'/>").getBytes(UTF_8)));
        final NamedNodeMap attributes2 = document.getElementsByTagName("s").item(0).getAttributes();
        final AttrList attrList2 = new AttrList(attributes2);
        Assert.assertEquals(1, attrList2.compareTo(this.attrList));
        Assert.assertNotEquals(attrList2.hashCode(), this.attrList.hashCode());
    }
}

