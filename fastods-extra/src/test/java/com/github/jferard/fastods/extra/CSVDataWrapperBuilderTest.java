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

package com.github.jferard.fastods.extra;

import com.github.jferard.fastods.Table;
import com.github.jferard.fastods.TableCellWalker;
import com.github.jferard.fastods.style.TableCellStyle;
import com.github.jferard.fastods.util.CharsetUtil;
import com.github.jferard.javamcsv.MetaCSVDataException;
import com.github.jferard.javamcsv.MetaCSVParseException;
import com.github.jferard.javamcsv.MetaCSVReadException;
import org.easymock.EasyMock;
import org.junit.Before;
import org.junit.Test;
import org.powermock.api.easymock.PowerMock;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.logging.Logger;

public class CSVDataWrapperBuilderTest {
    private TableCellWalker walker;

    @Before
    public void setUp() {
        this.walker = PowerMock.createMock(TableCellWalker.class);
    }

    @Test
    public void testDirectives()
            throws IOException, MetaCSVReadException, MetaCSVDataException, MetaCSVParseException {
        PowerMock.resetAll();
        this.prepareWalker();
        this.walker.setBooleanValue(true);
        this.finalizeWalker();

        PowerMock.replayAll();
        final byte[] bytes = ("VALUE\r\n" + "T").getBytes(StandardCharsets.UTF_8);
        final CSVDataWrapper dataWrapper =
                new CSVDataWrapperBuilder(new ByteArrayInputStream(bytes))
                        .metaCSVDirectives("data,col/0/type,boolean/T/F").build();
        dataWrapper.addToTable(this.walker);

        PowerMock.verifyAll();
    }

    @Test
    public void testInputStream()
            throws IOException, MetaCSVReadException, MetaCSVDataException, MetaCSVParseException {
        PowerMock.resetAll();
        this.prepareWalker();
        this.walker.setBooleanValue(true);
        this.finalizeWalker();

        PowerMock.replayAll();
        final byte[] bytes = ("VALUE\r\n" + "T").getBytes(StandardCharsets.UTF_8);
        final CSVDataWrapper dataWrapper =
                new CSVDataWrapperBuilder(new ByteArrayInputStream(bytes))
                        .metaCSVInputStream(new ByteArrayInputStream(
                                "domain,key,value\r\ndata,col/0/type,boolean/T/F\r\n"
                                        .getBytes(StandardCharsets.UTF_8))).build();
        dataWrapper.addToTable(this.walker);

        PowerMock.verifyAll();
    }

    @Test
    public void testParameters()
            throws IOException, MetaCSVReadException, MetaCSVDataException, MetaCSVParseException {
        final Table table = PowerMock.createMock(Table.class);

        PowerMock.resetAll();
        EasyMock.expect(this.walker.rowIndex()).andReturn(0);
        EasyMock.expect(this.walker.colIndex()).andReturn(0);
        this.walker.to(0);
        this.walker.setStringValue("VALUE");
        this.walker.setStyle(EasyMock.isA(TableCellStyle.class));
        this.walker.next();
        this.walker.nextRow();
        this.walker.to(0);
        this.walker.setBooleanValue(true);
        this.walker.next();
        this.walker.nextRow();
        this.walker.to(0);
        this.walker.setBooleanValue(false);
        this.walker.next();
        this.walker.nextRow();
        EasyMock.expect(this.walker.getTable()).andReturn(table);
        EasyMock.expect(this.walker.rowIndex()).andReturn(2);
        table.addAutoFilter("range_name", 0, 0, 2, 0);
        this.walker.nextRow();

        PowerMock.replayAll();
        final byte[] bytes = ("VALUE\r\nT\r\nF\r\nT\r\nT\r\nT\r\nF").getBytes(StandardCharsets.UTF_8);
        final CSVDataWrapper dataWrapper =
                new CSVDataWrapperBuilder(new ByteArrayInputStream(bytes))
                        .autoFilterRangeName("range_name").logger(
                        Logger.getAnonymousLogger()).headerStyle(TableCellStyle.DEFAULT_CELL_STYLE)
                        .max(2)
                        .metaCSVInputStream(new ByteArrayInputStream(
                                "domain,key,value\r\ndata,col/0/type,boolean/T/F\r\n"
                                        .getBytes(StandardCharsets.UTF_8))).build();
        dataWrapper.addToTable(this.walker);

        PowerMock.verifyAll();
    }

    @Test
    public void testNoHeader()
            throws IOException, MetaCSVReadException, MetaCSVDataException, MetaCSVParseException {
        PowerMock.resetAll();
        EasyMock.expect(this.walker.rowIndex()).andReturn(0);
        EasyMock.expect(this.walker.colIndex()).andReturn(0);
        this.walker.to(0);
        this.walker.setStringValue("VALUE");
        this.walker.next();
        this.walker.nextRow();
        this.walker.to(0);
        this.walker.setBooleanValue(true);
        this.finalizeWalker();

        PowerMock.replayAll();
        final byte[] bytes = ("VALUE\r\n" + "T").getBytes(StandardCharsets.UTF_8);
        final CSVDataWrapper dataWrapper =
                new CSVDataWrapperBuilder(new ByteArrayInputStream(bytes)).noHeaderStyle()
                        .metaCSVInputStream(new ByteArrayInputStream(
                                "domain,key,value\r\ndata,col/0/type,boolean/T/F\r\n"
                                        .getBytes(StandardCharsets.UTF_8))).build();
        dataWrapper.addToTable(this.walker);

        PowerMock.verifyAll();
    }

    private TableCellWalker prepareWalker() throws IOException {
        EasyMock.expect(this.walker.rowIndex()).andReturn(0);
        EasyMock.expect(this.walker.colIndex()).andReturn(0);
        this.walker.to(0);
        this.walker.setStringValue("VALUE");
        this.walker.setStyle(EasyMock.isA(TableCellStyle.class));
        this.walker.next();
        this.walker.nextRow();
        this.walker.to(0);
        return this.walker;
    }

    private void finalizeWalker() throws IOException {
        this.walker.next();
        this.walker.nextRow();
        this.walker.nextRow();
    }
}