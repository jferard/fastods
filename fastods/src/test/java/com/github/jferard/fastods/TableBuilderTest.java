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

import com.github.jferard.fastods.datastyle.DataStyles;
import com.github.jferard.fastods.datastyle.DataStylesBuilder;
import com.github.jferard.fastods.odselement.StylesContainer;
import com.github.jferard.fastods.odselement.config.ConfigItemMapEntrySet;
import com.github.jferard.fastods.style.TableCellStyle;
import com.github.jferard.fastods.style.TableColumnStyle;
import com.github.jferard.fastods.style.TableStyle;
import com.github.jferard.fastods.util.EqualityUtil;
import com.github.jferard.fastods.util.PositionUtil;
import com.github.jferard.fastods.util.TableNameUtil;
import com.github.jferard.fastods.util.WriteUtil;
import com.github.jferard.fastods.util.XMLUtil;
import com.google.common.collect.Lists;
import org.easymock.EasyMock;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.powermock.api.easymock.PowerMock;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class TableBuilderTest {
    private DataStyles ds;
    private StylesContainer stc;
    private TableBuilder builder;
    private XMLUtil xmlUtil;
    private Table table;
    private TableAppender appender;

    @Before
    public void setUp() {
        this.stc = PowerMock.createMock(StylesContainer.class);
        final PositionUtil positionUtil = new PositionUtil(new EqualityUtil(), new TableNameUtil());
        final XMLUtil xmlUtil = XMLUtil.create();
        this.ds = DataStylesBuilder.create(Locale.US).build();
        this.builder = new TableBuilder(positionUtil, WriteUtil.create(), xmlUtil,
                this.stc, this.ds, false, "mytable", 10, 100, ConfigItemMapEntrySet.createSet("mytable"), 2);
        this.xmlUtil = xmlUtil;

        this.table = PowerMock.createMock(Table.class);
        this.appender = PowerMock.createMock(TableAppender.class);
        PowerMock.resetAll();
    }

    @Test
    public final void testColumnStyles() throws FastOdsException {
        final List<TableColumnStyle> tcss = Lists.newArrayList();
        for (int c = 0; c < 10; c++) {
            final TableColumnStyle tcs = TableColumnStyle
                    .builder("test" + Integer.toString(c)).build();
            tcss.add(tcs);
        }

        // PLAY
        for (int c = 0; c < 10; c++) {
            final TableColumnStyle tcs = tcss.get(c);
            EasyMock.expect(this.stc.addContentFontFaceContainerStyle(tcs)).andReturn(true);
            EasyMock.expect(this.stc.addContentStyle(tcs)).andReturn(true);
            EasyMock.expect(this.stc.addContentStyle(TableCellStyle.DEFAULT_CELL_STYLE)).andReturn(true);
        }
        PowerMock.replayAll();

        for (int c = 0; c < 10; c++) {
            final TableColumnStyle tcs = tcss.get(c);
            this.builder.setColumnStyle(c, tcs);
        }
//        Assert.assertEquals(tcss, this.builder.getColumnStyles());
        PowerMock.verifyAll();
    }

    @Test
    public final void testGetRow() throws FastOdsException, IOException {
        PowerMock.replayAll();
        final List<TableRow> rows = Lists.newArrayList();
        for (int r = 0; r < 7; r++) { // 8 times
            rows.add(this.builder.nextRow(this.table, this.appender));
        }

        for (int r = 0; r < 7; r++) { // 8 times
            Assert.assertEquals(rows.get(r), this.builder.getRow(this.table, this.appender, r));
        }
        PowerMock.verifyAll();
    }

    @Test
    public final void testGetRowFromStringPos() throws FastOdsException, IOException {
        PowerMock.replayAll();
        final List<TableRow> rows = Lists.newArrayList();
        for (int r = 0; r < 7; r++) { // 8 times
            rows.add(this.builder.nextRow(this.table, this.appender));
        }

        Assert.assertEquals(rows.get(4), this.builder.getRow(this.table, this.appender, "A5"));
        PowerMock.verifyAll();
    }

    @Test
    public final void testGetRowHundred() throws FastOdsException, IOException {
        PowerMock.replayAll();
        for (int r = 0; r < 7; r++) { // 8 times
            this.builder.nextRow(this.table, this.appender);
        }
        this.builder.getRow(this.table, this.appender, 100);
        Assert.assertEquals(100, this.builder.getLastRowNumber());
        PowerMock.verifyAll();
    }

    @Test(expected = FastOdsException.class)
    public final void testGetRowNegative() throws FastOdsException, IOException {
        PowerMock.replayAll();
        this.builder.getRow(this.table, this.appender, -1);
        PowerMock.verifyAll();
    }

    @Test
    public final void testLastRow() throws IOException {
        PowerMock.replayAll();
        Assert.assertEquals(-1, this.builder.getLastRowNumber());
        for (int r = 0; r < 7; r++) { // 8 times
            this.builder.nextRow(this.table, this.appender);
        }
        Assert.assertEquals(6, this.builder.getLastRowNumber());
        PowerMock.verifyAll();
    }

    @Test
    public final void testRowsSpanned() throws IOException {
        // PLAY

        PowerMock.replayAll();
        this.builder.setRowsSpanned(this.table, this.appender, 10, 11, 12);

        PowerMock.verifyAll();
    }

    @Test
    public final void testMergeWithPosString() throws IOException, FastOdsException {
        PowerMock.replayAll();
        this.builder.setCellMerge(this.table, this.appender,  "B1",2,2);
        PowerMock.verifyAll();
    }


    @Test
    public final void testMerge() throws IOException, FastOdsException {
        PowerMock.replayAll();
        this.builder.setCellMerge(this.table, this.appender, 2,1,2,2);
        PowerMock.verifyAll();
    }

    @Test
    public final void testMergeWithObserver1() throws IOException, FastOdsException {
        final NamedOdsFileWriter o = PowerMock.createMock(NamedOdsFileWriter.class);
        this.builder.addObserver(o);

        o.update(EasyMock.isA(PreprocessedRowsFlusher.class));
        PowerMock.replayAll();
        this.builder.setCellMerge(this.table, this.appender, 2,1,2,2);
        PowerMock.verifyAll();
    }

    @Test
    public final void testNameAndStyle() {
        // PLAY
        final TableStyle ts = TableStyle.builder("b").build();
        EasyMock.expect(this.stc.addContentStyle(ts)).andReturn(true);
        EasyMock.expect(this.stc.addPageStyle(ts.getPageStyle())).andReturn(true);

        PowerMock.replayAll();

        this.builder.setName("tname");
        this.builder.setStyle(ts);
        Assert.assertEquals("tname", this.builder.getName());
        Assert.assertEquals("b", this.builder.getStyleName());
        PowerMock.verifyAll();
    }

    @Test
    public void testObserver() throws IOException {
        final NamedOdsFileWriter o = PowerMock.createMock(NamedOdsFileWriter.class);

/*
        o.update(EasyMock.isA(BeginTableFlusher.class));
        EasyMock.expectLastCall().times(3);
        o.update(EasyMock.isA(EndTableFlusher.class));
        EasyMock.expectLastCall().times(3);
*/
        PowerMock.replayAll();
        this.builder.addObserver(o);

//        final TableRow row = this.builder.getRowSecure(this.table, this.appender,10, true);
//        final TableCell cell = row.getOrCreateCell(11);
//        cell.setStringValue("a");

        PowerMock.verifyAll();
    }
}
