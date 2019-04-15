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

package com.github.jferard.fastods.odselement;

import com.github.jferard.fastods.Table;
import com.github.jferard.fastods.TableCell;
import com.github.jferard.fastods.datastyle.DataStyles;
import com.github.jferard.fastods.datastyle.DataStylesBuilder;
import com.github.jferard.fastods.style.TableCellStyle;
import com.github.jferard.fastods.testlib.ZipUTF8WriterMockHandler;
import com.github.jferard.fastods.util.PositionUtil;
import com.github.jferard.fastods.util.WriteUtil;
import com.github.jferard.fastods.util.XMLUtil;
import com.github.jferard.fastods.util.ZipUTF8Writer;
import org.easymock.EasyMock;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.powermock.api.easymock.PowerMock;

import java.io.IOException;
import java.util.Collections;
import java.util.Locale;

public class ContentElementTest {
    private StylesContainer container;
    private ContentElement content;
    private DataStyles format;

    @Before
    public void setUp() throws Exception {
        this.container = PowerMock.createMock(StylesContainer.class);
        this.format = DataStylesBuilder.create(Locale.US).build();
        this.content = new ContentElement(PositionUtil.create(), XMLUtil.create(), WriteUtil.create(), this.format,
                true, this.container);
        PowerMock.resetAll();
    }

    @After
    public void tearDown() {
        PowerMock.verifyAll();
    }

    @Test
    public void testAddChildCellStyle() {
        final TableCellStyle style = PowerMock.createNiceMock(TableCellStyle.class);

        // play
        EasyMock.expect(
                this.container.addChildCellStyle(TableCellStyle.DEFAULT_CELL_STYLE, this.format.getBooleanDataStyle()))
                .andReturn(style);

        PowerMock.replayAll();
        Assert.assertEquals(style,
                this.content.addChildCellStyle(TableCellStyle.DEFAULT_CELL_STYLE, TableCell.Type.BOOLEAN));
    }

    @Test
    public void testAddChildCellStyleOfTypeString() {
        PowerMock.replayAll();
        Assert.assertEquals(TableCellStyle.DEFAULT_CELL_STYLE,
                this.content.addChildCellStyle(TableCellStyle.DEFAULT_CELL_STYLE, TableCell.Type.STRING));
    }

    @Test
    public void testAddChildCellStyleOfTypeVoid() {
        PowerMock.replayAll();
        Assert.assertEquals(TableCellStyle.DEFAULT_CELL_STYLE,
                this.content.addChildCellStyle(TableCellStyle.DEFAULT_CELL_STYLE, TableCell.Type.VOID));
    }

    @Test
    public void testTable() {
        PowerMock.replayAll();
        Assert.assertEquals(0, this.content.getTableCount());
        Assert.assertEquals(Collections.emptyList(), this.content.getTables());
        final Table t = this.content.addTable("t1", 1, 1);
        Assert.assertEquals(t, this.content.addTable("t1", 2, 3));
        Assert.assertEquals(t, this.content.getTable("t1"));
        Assert.assertEquals(t, this.content.getTable(0));
        Assert.assertEquals(1, this.content.getTableCount());
        Assert.assertEquals(Collections.singletonList(t), this.content.getTables());
        Assert.assertEquals(t, this.content.getLastTable());
    }

    @Test()
    public void flushRowsNoTable() throws IOException {
        final ZipUTF8Writer z = ZipUTF8WriterMockHandler.create().getInstance(ZipUTF8Writer.class);
        final XMLUtil util = XMLUtil.create();

        // play
        this.playWriteHeader(util);

        PowerMock.replayAll();
        this.content.flushRows(util, z, SettingsElement.create());
    }

    private void playWriteHeader(XMLUtil util) throws IOException {
        this.container.writeFontFaceDecls(EasyMock.eq(util), EasyMock.isA(ZipUTF8Writer.class));
        this.container.writeHiddenDataStyles(EasyMock.eq(util), EasyMock.isA(ZipUTF8Writer.class));
        this.container
                .writeContentAutomaticStyles(EasyMock.eq(util), EasyMock.isA(ZipUTF8Writer.class));
    }

    @Test()
    public void flushRowsWithOneTable() throws IOException {
        final ZipUTF8Writer z = ZipUTF8WriterMockHandler.create().getInstance(ZipUTF8Writer.class);
        final XMLUtil util = XMLUtil.create();

        // play
        this.playWriteHeader(util);

        PowerMock.replayAll();
        this.content.flushRows(util, z, SettingsElement.create());
    }

    @Test()
    public void flushRowsWithTwoTables() throws IOException {
        final ZipUTF8Writer z = ZipUTF8WriterMockHandler.create().getInstance(ZipUTF8Writer.class);
        final XMLUtil util = XMLUtil.create();

        // play
        this.playWriteHeader(util);

        PowerMock.replayAll();
        this.content.flushRows(util, z, SettingsElement.create());
    }

    @Test()
    public void flushRowsWithThreeTables() throws IOException {
        final ZipUTF8Writer z = ZipUTF8WriterMockHandler.create().getInstance(ZipUTF8Writer.class);
        final XMLUtil util = XMLUtil.create();

        // play
        this.playWriteHeader(util);

        PowerMock.replayAll();
        this.content.flushRows(util, z, SettingsElement.create());
    }

    @Test
    public void flushTablesNoTable() throws IOException {
        final ZipUTF8Writer z = ZipUTF8WriterMockHandler.create().getInstance(ZipUTF8Writer.class);
        final XMLUtil util = XMLUtil.create();

        // play
        this.playWriteHeader(util);

        PowerMock.replayAll();
        this.content.flushTables(util, z);
    }

    @Test()
    public void flushTablesWithOneTable() throws IOException {
        final ZipUTF8Writer z = ZipUTF8WriterMockHandler.create().getInstance(ZipUTF8Writer.class);
        final XMLUtil util = XMLUtil.create();
        this.content.addTable("t1",1,1);

        // play
        this.playWriteHeader(util);

        PowerMock.replayAll();
        this.content.flushTables(util, z);
    }

    @Test()
    public void flushTablesWithTwoTables() throws IOException {
        final ZipUTF8Writer z = ZipUTF8WriterMockHandler.create().getInstance(ZipUTF8Writer.class);
        final XMLUtil util = XMLUtil.create();
        this.content.addTable("t1",1,1);
        this.content.addTable("t2",2,2);

        // play
        this.playWriteHeader(util);

        PowerMock.replayAll();
        this.content.flushRows(util, z, SettingsElement.create());
    }

    @Test()
    public void flushTablesWithThreeTables() throws IOException {
        final ZipUTF8Writer z = ZipUTF8WriterMockHandler.create().getInstance(ZipUTF8Writer.class);
        final XMLUtil util = XMLUtil.create();
        this.content.addTable("t1",1,1);
        this.content.addTable("t2",2,2);
        this.content.addTable("t2",3,3);

        // play
        this.playWriteHeader(util);

        PowerMock.replayAll();
        this.content.flushTables(util, z);
    }
}