/*
 * FastODS - A very fast and lightweight (no dependency) library for creating ODS
 *    (Open Document Spreadsheet, mainly for Calc) files in Java.
 *    It's a Martin Schulz's SimpleODS fork
 *    Copyright (C) 2016-2020 J. Férard <https://github.com/jferard>
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

public class CSVDataWrapperTest {
    private TableCellWalker walker;

    @Before
    public void setUp() {
        this.walker = PowerMock.createMock(TableCellWalker.class);
    }

    @Test
    public void testText()
            throws IOException, MetaCSVReadException, MetaCSVDataException, MetaCSVParseException {
        PowerMock.resetAll();
        this.prepareWalker();
        this.walker.setStringValue("foo");
        this.finalizeWalker();

        PowerMock.replayAll();
        final CSVDataWrapper dataWrapper = this.getWrapper("foo", "text");
        dataWrapper.addToTable(this.walker);

        PowerMock.verifyAll();
    }

    @Test
    public void testInteger()
            throws IOException, MetaCSVReadException, MetaCSVDataException, MetaCSVParseException {
        PowerMock.resetAll();
        this.prepareWalker();
        this.walker.setFloatValue(10L);
        this.finalizeWalker();

        PowerMock.replayAll();
        final CSVDataWrapper dataWrapper = this.getWrapper("10", "integer");
        dataWrapper.addToTable(this.walker);

        PowerMock.verifyAll();
    }

    @Test
    public void testFloat()
            throws IOException, MetaCSVReadException, MetaCSVDataException, MetaCSVParseException {
        PowerMock.resetAll();
        this.prepareWalker();
        this.walker.setFloatValue(10.5d);
        this.finalizeWalker();

        PowerMock.replayAll();
        final CSVDataWrapper dataWrapper = this.getWrapper("\"10,5\"", "\"float//,\"");
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

    private CSVDataWrapper getWrapper(final String value, final String type)
            throws MetaCSVReadException, MetaCSVDataException, MetaCSVParseException, IOException {
        final byte[] bytes = ("VALUE\r\n" + value).getBytes(CharsetUtil.UTF_8);
        final CSVDataWrapper dataWrapper =
                new CSVDataWrapperBuilder(new ByteArrayInputStream(bytes))
                        .metaCSVDirectives("data,col/0/type," + type).build();
        return dataWrapper;
    }
}
