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

package com.github.jferard.fastods.testutil;

import com.github.jferard.fastods.util.ZipUTF8Writer;
import org.junit.Assert;
import org.junit.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.util.zip.ZipEntry;

/**
 * Created by jferard on 10/04/17.
 * @author Julien Férard
 */
public class ZipUTF8WriterMockTest {
    @Test(expected=IOException.class)
    public void testNoEntry() throws IOException {
        final ZipUTF8Writer mock = ZipUTF8WriterMock.createMock();
        mock.write("test");
    }

    @Test
    public void testOneEntry() throws IOException {
        final ZipUTF8WriterMock mock = ZipUTF8WriterMock.createMock();
        mock.putNextEntry(new ZipEntry("entry1"));
        mock.write("test");
        mock.closeEntry();
        mock.flush();
        mock.close();

        Assert.assertNull(mock.getEntryAsString("entry2"));
        Assert.assertEquals("test", mock.getEntryAsString("entry1"));
    }

    @Test
    public void testOneEntryWithXML() throws IOException, ParserConfigurationException, SAXException {
        final ZipUTF8WriterMock mock = ZipUTF8WriterMock.createMock();
        mock.putNextEntry(new ZipEntry("entry1"));
        mock.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
        mock.append("<tag>\n");
        mock.append("content\n");
        mock.append("</tag>\n");
        mock.closeEntry();
        mock.flush();
        mock.close();

        final Document doc = mock.getEntryAsDocument("entry1");
        final Node firstChild = doc.getFirstChild();
        final Node content = firstChild.getFirstChild();
        Assert.assertEquals("tag", firstChild.getNodeName());
        Assert.assertEquals(Node.ELEMENT_NODE, firstChild.getNodeType());
        Assert.assertEquals("\ncontent\n", content.getNodeValue());
        Assert.assertEquals(Node.TEXT_NODE, content.getNodeType());
    }
}