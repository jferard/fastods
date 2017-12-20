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
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.Closeable;
import java.io.Flushable;
import java.io.IOException;
import java.util.zip.ZipEntry;

public class ZipUTF8WriterMockHandlerTest {

    private ZipUTF8WriterMock mock;
    private ZipUTF8WriterMockHandler handler;

    @Before
    public void setUp() {
        this.mock = ZipUTF8WriterMock.createMock();
        this.handler = new ZipUTF8WriterMockHandler(this.mock);
    }

    @Test
    public void testGet() throws IOException, ParserConfigurationException, SAXException {
        this.mock.putNextEntry(new ZipEntry("test"));
        this.mock.append("<document />");
        this.mock.closeEntry();
        this.mock.close();
        Assert.assertEquals("<document />", this.handler.getEntryAsString("test"));
        Assert.assertEquals("document", this.handler.getEntryAsDocument("test").getDocumentElement().getTagName());
    }

    @Test
    public void testGetInstance() throws IOException, ParserConfigurationException, SAXException {
        this.mock.putNextEntry(new ZipEntry("test"));
        this.mock.append("<document />");
        this.mock.closeEntry();
        this.mock.close();
        this.mock.finish();
        final Iterable instance = this.handler.getInstance(Iterable.class);
        Assert.assertEquals(null, instance.iterator());
    }

    @Test
    public void testInvoke() throws Throwable {
        final ZUW instance = this.handler.getInstance(ZUW.class);
        instance.putNextEntry(new ZipEntry("test"));
        instance.append("<document />");
        instance.closeEntry();
        instance.close();
        instance.finish();
        Assert.assertEquals("<document />", this.handler.getEntryAsString("test"));
        Assert.assertEquals("document", this.handler.getEntryAsDocument("test").getDocumentElement().getTagName());
    }

    private interface ZUW
		extends Closeable, Flushable, Appendable {
        void closeEntry() throws IOException;
        void finish() throws IOException;
        void putNextEntry(final ZipEntry entry) throws IOException;
        void setComment(final String comment);
        void write(final String str) throws IOException;
    }
}