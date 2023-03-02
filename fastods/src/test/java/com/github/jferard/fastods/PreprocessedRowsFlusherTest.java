/*
 * FastODS - A very fast and lightweight (no dependency) library for creating ODS
 *    (Open Document Spreadsheet, mainly for Calc) files in Java.
 *    It's a Martin Schulz's SimpleODS fork
 *    Copyright (C) 2016-2023 J. Férard <https://github.com/jferard>
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

package com.github.jferard.fastods;

import com.github.jferard.fastods.util.XMLUtil;
import com.github.jferard.fastods.util.ZipUTF8Writer;
import org.easymock.Capture;
import org.easymock.EasyMock;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.powermock.api.easymock.PowerMock;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Created by jferard on 09/05/17.
 */
public class PreprocessedRowsFlusherTest {
    private XMLUtil util;
    private ZipUTF8Writer w;
    private StringBuilder sb;

    @Before
    public void setUp() {
        this.util = XMLUtil.create();
        this.w = PowerMock.createMock(ZipUTF8Writer.class);
        this.sb = new StringBuilder(1024 * 32);
    }

    @Test
    public void create() throws Exception {
        final List<TableRowImpl> rows = Collections.emptyList();

        PowerMock.resetAll();
        PowerMock.replayAll();
        PreprocessedRowsFlusher.create(this.util, rows);

        PowerMock.verifyAll();
    }

    @Test
    public void testFlushIntoEmptyList() throws Exception {
        final List<TableRowImpl> rows = Collections.emptyList();

        PowerMock.resetAll();
        EasyMock.expect(this.w.append(this.sb)).andReturn(this.sb);

        PowerMock.replayAll();
        final OdsAsyncFlusher flusher = new PreprocessedRowsFlusher(this.sb);
        flusher.flushInto(this.util, this.w);

        PowerMock.verifyAll();
    }

    @Test
    public void testFlushInto() throws Exception {
        final TableRowImpl r1 = PowerMock.createMock(TableRowImpl.class);
        final TableRowImpl r2 = PowerMock.createMock(TableRowImpl.class);
        final List<TableRowImpl> rows = Arrays.asList(r1, r2);

        PowerMock.resetAll();
        r1.appendXMLToTable(EasyMock.eq(this.util), EasyMock.anyObject(StringBuilder.class));
        r2.appendXMLToTable(EasyMock.eq(this.util), EasyMock.anyObject(StringBuilder.class));
        EasyMock.expect(this.w.append(EasyMock.anyObject(StringBuilder.class))).andReturn(this.sb);

        PowerMock.replayAll();
        final OdsAsyncFlusher flusher = PreprocessedRowsFlusher.create(this.util, rows);
        flusher.flushInto(this.util, this.w);

        PowerMock.verifyAll();
        Assert.assertEquals("", this.sb.toString());
    }

    @Test
    public void testFlushIntoNullRow() throws Exception {
        final List<TableRowImpl> rows = new ArrayList<TableRowImpl>();
        rows.add(null);

        PowerMock.resetAll();
        final Capture<CharSequence> capturedArgument = EasyMock.newCapture();
        EasyMock.expect(this.w.append(EasyMock.capture(capturedArgument))).andReturn(this.w);

        PowerMock.replayAll();
        final OdsAsyncFlusher flusher = PreprocessedRowsFlusher.create(this.util, rows);
        flusher.flushInto(this.util, this.w);

        PowerMock.verifyAll();
        Assert.assertEquals("<row />", capturedArgument.getValue().toString());
    }

    @Test
    public void testEnd() throws Exception {
        final List<TableRowImpl> rows = new ArrayList<TableRowImpl>();

        PowerMock.resetAll();
        PowerMock.replayAll();
        final OdsAsyncFlusher flusher = PreprocessedRowsFlusher.create(this.util, rows);

        PowerMock.verifyAll();
        Assert.assertFalse(flusher.isEnd());
    }
}