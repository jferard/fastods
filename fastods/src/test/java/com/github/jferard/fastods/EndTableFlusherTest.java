/*
 * FastODS - A very fast and lightweight (no dependency) library for creating ODS
 *    (Open Document Spreadsheet, mainly for Calc) files in Java.
 *    It's a Martin Schulz's SimpleODS fork
 *    Copyright (C) 2016-2019 J. Férard <https://github.com/jferard>
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

package com.github.jferard.fastods;

import com.github.jferard.fastods.util.XMLUtil;
import com.github.jferard.fastods.util.ZipUTF8Writer;
import org.junit.Before;
import org.junit.Test;
import org.powermock.api.easymock.PowerMock;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Created by jferard on 09/05/17.
 */
public class EndTableFlusherTest {
    private XMLUtil util;
    private ZipUTF8Writer w;
    private TableAppender appender;

    @Before
    public void setUp() {
        this.util = XMLUtil.create();
        this.w = PowerMock.createMock(ZipUTF8Writer.class);
        this.appender = PowerMock.createMock(TableAppender.class);
    }

    @Test
    public void flushIntoEmptytList() throws Exception {
        final List<TableRow> rows = Collections.emptyList();

        PowerMock.resetAll();
        this.appender.appendPostamble(this.w);

        PowerMock.replayAll();
        final OdsFlusher f = new EndTableFlusher(this.appender, rows);
        f.flushInto(this.util, this.w);

        PowerMock.verifyAll();
    }

    @Test
    public void flushInto() throws Exception {
        final TableRow r1 = PowerMock.createMock(TableRow.class);
        final TableRow r2 = PowerMock.createMock(TableRow.class);
        final List<TableRow> rows = Arrays.asList(r1, r2);

        PowerMock.resetAll();
        r1.appendXMLToTable(this.util, this.w);
        r2.appendXMLToTable(this.util, this.w);
        this.appender.appendPostamble(this.w);

        PowerMock.replayAll();
        final OdsFlusher f = new EndTableFlusher(this.appender, rows);
        f.flushInto(this.util, this.w);

        PowerMock.verifyAll();
    }
}