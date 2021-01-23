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

import org.easymock.EasyMock;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.powermock.api.easymock.PowerMock;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.Closeable;
import java.io.Flushable;
import java.io.IOException;
import java.util.zip.ZipEntry;

public class ZipUTF8WriterMockHandlerTest {
    @Rule
    public final ExpectedException thrown = ExpectedException.none();

    private ZipUTF8WriterMock mock;
    private ZipUTF8WriterMockHandler handler;

    @Before
    public void setUp() {
        this.mock = ZipUTF8WriterMock.createMock();
        this.handler = new ZipUTF8WriterMockHandler(this.mock);
    }

    @Test
    public void testCreate() {
        final ZipUTF8WriterMockHandler h = ZipUTF8WriterMockHandler.create();
        PowerMock.resetAll();
        PowerMock.replayAll();
        PowerMock.verifyAll();
    }

    @Test
    public void testGet() throws IOException, ParserConfigurationException, SAXException {
        this.mock.putNextEntry(new ZipEntry("test"));
        this.mock.append("<document />");
        this.mock.closeEntry();
        this.mock.close();
        Assert.assertEquals("<document />", this.handler.getEntryAsString("test"));
        Assert.assertEquals("document",
                this.handler.getEntryAsDocument("test").getDocumentElement().getTagName());
    }

    @Test
    @SuppressWarnings("unchecked")
    public void testGetInstance() throws IOException {
        this.mock.putNextEntry(new ZipEntry("test"));
        this.mock.append("<document />");
        this.mock.closeEntry();
        this.mock.close();
        this.mock.finish();
        final Iterable<Object> instance = (Iterable<Object>) this.handler.getInstance(Iterable.class);

        this.thrown.expect(IllegalArgumentException.class);
        this.thrown.expectMessage(
                "Method not found: ZipUTF8WriterMock.iterator");
        Assert.assertNull(instance.iterator());
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
        Assert.assertEquals("document",
                this.handler.getEntryAsDocument("test").getDocumentElement().getTagName());
    }


    @Test
    public void testInvokeOther() throws Throwable {
        final ZipUTF8WriterMock mock1 = PowerMock.createMock(ZipUTF8WriterMock.class);
        final ZipUTF8WriterMockHandler handler1 = new ZipUTF8WriterMockHandler(mock1);
        final ZipEntry e = new ZipEntry("e");

        EasyMock.expect(mock1.append('c')).andReturn(mock1);
        EasyMock.expect(mock1.append("c")).andReturn(mock1);
        EasyMock.expect(mock1.append("c", 0, 1)).andReturn(mock1);
        mock1.close();
        mock1.closeEntry();
        mock1.finish();
        mock1.flush();
        mock1.putNextEntry(e);
        mock1.setComment("c");

        final ZUW instance = handler1.getInstance(ZUW.class);
        PowerMock.replayAll();
        instance.append('c');
        instance.append("c");
        instance.append("c", 0, 1);
        instance.close();
        instance.closeEntry();
        instance.finish();
        instance.flush();
        instance.putNextEntry(e);
        instance.setComment("c");
    }

    @Test
    public void testGetBuilder() throws ParserConfigurationException, SAXException, IOException {
        final ZUW instance = this.handler.getInstance(ZUW.class);
        Assert.assertNull(this.handler.getEntryAsDocument("ok"));
    }

    private interface ZUW extends Closeable, Flushable, Appendable {
        void closeEntry();

        void finish();

        void putNextEntry(final ZipEntry entry);

        void setComment(final String comment);

        void write(final String str);
    }
}