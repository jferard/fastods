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
        final Document document = this.builder.parse(new ByteArrayInputStream(("<r a='1' b='2'><s/><t/></r>").getBytes(UTF_8)));
        this.r = document.getFirstChild();
    }

    @Test
    public void testEquals() throws IOException, SAXException {
        final Document document;
        document = this.builder.parse(new ByteArrayInputStream(("<r a='1' b='2'><s/><t/></r>").getBytes(UTF_8)));
        final Node r2 = document.getFirstChild();
        Assert.assertTrue(this.tester.childrenEquals(this.r, r2));
    }

    @Test
    public void testNotEquals() throws IOException, SAXException {
        final Document document;
        document = this.builder.parse(new ByteArrayInputStream(("<r a='1' b='2'><u/><v/></r>").getBytes(UTF_8)));
        final Node r2 = document.getFirstChild();
        Assert.assertFalse(this.tester.childrenEquals(this.r, r2));
    }
}