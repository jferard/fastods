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
import com.github.jferard.fastods.odselement.config.ConfigItem;
import com.github.jferard.fastods.odselement.config.ConfigItemMapEntrySet;
import com.github.jferard.fastods.style.TableCellStyle;
import com.github.jferard.fastods.style.TableColumnStyle;
import com.github.jferard.fastods.style.TableStyle;
import com.github.jferard.fastods.ref.PositionUtil;
import com.github.jferard.fastods.ref.TableNameUtil;
import com.github.jferard.fastods.util.WriteUtil;
import com.github.jferard.fastods.util.XMLUtil;
import com.google.common.collect.Lists;
import org.easymock.EasyMock;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.powermock.api.easymock.PowerMock;

import java.io.IOException;
import java.text.ParseException;
import java.util.List;
import java.util.Locale;

public class TableBuilderTest {
    private DataStyles ds;
    private StylesContainer stc;
    private TableBuilder builder;
    private XMLUtil xmlUtil;
    private Table table;
    private TableAppender appender;
    private ConfigItemMapEntrySet ce;

    @Before
    public void setUp() {
        final PositionUtil positionUtil = new PositionUtil(new TableNameUtil());
        final XMLUtil xmlUtil = XMLUtil.create();

        this.stc = PowerMock.createMock(StylesContainer.class);
        this.ds = DataStylesBuilder.create(Locale.US).build();
        this.ce = ConfigItemMapEntrySet.createSet("mytable");
        this.builder = new TableBuilder(positionUtil, WriteUtil.create(), xmlUtil, this.stc,
                this.ds, false, "mytable", 10, 100, this.ce, 2);
        this.xmlUtil = xmlUtil;

        this.table = PowerMock.createMock(Table.class);
        this.appender = PowerMock.createMock(TableAppender.class);
    }

    @Test
    public final void testColumnStyles() {
        final List<TableColumnStyle> tcss = Lists.newArrayList();
        for (int c = 0; c < 10; c++) {
            final TableColumnStyle tcs = TableColumnStyle.builder("test" + c).build();
            tcss.add(tcs);
        }

        PowerMock.resetAll();
        for (int c = 0; c < 10; c++) {
            final TableColumnStyle tcs = tcss.get(c);
            EasyMock.expect(this.stc.addContentFontFaceContainerStyle(tcs)).andReturn(true);
            EasyMock.expect(this.stc.addContentStyle(tcs)).andReturn(true);
            EasyMock.expect(this.stc.addContentStyle(TableCellStyle.DEFAULT_CELL_STYLE))
                    .andReturn(true);
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
    public final void testGetRow() throws IOException {
        final List<TableRowImpl> rows = Lists.newArrayList();
        for (int r = 0; r < 7; r++) { // 8 times
            rows.add(this.builder.nextRow(this.table, this.appender));
        }

        PowerMock.resetAll();
        PowerMock.replayAll();
        for (int r = 0; r < 7; r++) { // 8 times
            Assert.assertEquals(rows.get(r), this.builder.getRow(this.table, this.appender, r));
        }
        PowerMock.verifyAll();
    }

    @Test
    public final void testGetRowFromStringPos() throws IOException, ParseException {
        final List<TableRowImpl> rows = Lists.newArrayList();
        for (int r = 0; r < 7; r++) { // 8 times
            rows.add(this.builder.nextRow(this.table, this.appender));
        }
        PowerMock.resetAll();

        PowerMock.replayAll();
        final TableRowImpl row = this.builder.getRow(this.table, this.appender, "A5");

        PowerMock.verifyAll();
        Assert.assertEquals(rows.get(4), row);
    }

    @Test
    public final void testGetRowHundred() throws IOException {
        PowerMock.resetAll();

        PowerMock.replayAll();
        for (int r = 0; r < 7; r++) { // 8 times
            this.builder.nextRow(this.table, this.appender);
        }
        this.builder.getRow(this.table, this.appender, 100);
        Assert.assertEquals(101, this.builder.getRowCount());

        PowerMock.verifyAll();
    }

    @Test(expected = IllegalArgumentException.class)
    public final void testGetRowNegative() throws IOException {
        PowerMock.resetAll();
        PowerMock.replayAll();
        this.builder.getRow(this.table, this.appender, -1);
        PowerMock.verifyAll();
    }

    @Test
    public final void testLastRow() throws IOException {
        PowerMock.resetAll();
        PowerMock.replayAll();
        final int initialRowCount = this.builder.getRowCount();
        for (int r = 0; r < 7; r++) { // 8 times
            this.builder.nextRow(this.table, this.appender);
        }
        final int rowCount = this.builder.getRowCount();

        PowerMock.verifyAll();
        Assert.assertEquals(0, initialRowCount);
        Assert.assertEquals(7, rowCount);
    }

    @Test
    public final void testRowsSpanned() throws IOException {
        PowerMock.resetAll();

        PowerMock.replayAll();
        this.builder.setRowsSpanned(this.table, this.appender, 10, 11, 12);

        PowerMock.verifyAll();
    }

    @Test
    public final void testMergeWithPosString() throws IOException, ParseException {
        PowerMock.resetAll();
        PowerMock.replayAll();
        this.builder.setCellMerge(this.table, this.appender, "B1", 2, 2);
        PowerMock.verifyAll();
    }

    @Test
    public final void testMerge() throws IOException {
        PowerMock.resetAll();
        PowerMock.replayAll();
        this.builder.setCellMerge(this.table, this.appender, 2, 1, 2, 2);
        PowerMock.verifyAll();
    }

    @Test(expected = IllegalArgumentException.class)
    public final void testMergeCovered() throws IOException {
        PowerMock.resetAll();
        PowerMock.replayAll();
        this.builder.setCellMerge(this.table, this.appender, 0, 0, 22, 22);
        assert this.builder.getRow(this.table, this.appender, 2).getOrCreateCell(1).isCovered();
        this.builder.setCellMerge(this.table, this.appender, 2, 1, 2, 2);
        PowerMock.verifyAll();
    }

    @Test
    public final void testMergeWithObserver1() throws IOException {
        final NamedOdsFileWriter o = PowerMock.createMock(NamedOdsFileWriter.class);

        PowerMock.resetAll();
        this.builder.addObserver(o);
        o.update(EasyMock.isA(PreprocessedRowsFlusher.class));

        PowerMock.replayAll();
        this.builder.setCellMerge(this.table, this.appender, 2, 1, 2, 2);

        PowerMock.verifyAll();
    }

    @Test
    public final void testNameAndStyle() {
        final TableStyle ts = TableStyle.builder("b").build();

        PowerMock.resetAll();
        EasyMock.expect(this.stc.addContentStyle(ts)).andReturn(true);
        EasyMock.expect(this.stc.addPageStyle(ts.getPageStyle())).andReturn(true);

        PowerMock.replayAll();
        this.builder.setName("tname");
        this.builder.setStyle(ts);
        final String name = this.builder.getName();
        final String styleName = this.builder.getStyleName();

        PowerMock.verifyAll();
        Assert.assertEquals("tname", name);
        Assert.assertEquals("b", styleName);
    }

    @Test
    public void testObserver() {
        final NamedOdsFileWriter o = PowerMock.createMock(NamedOdsFileWriter.class);

        PowerMock.resetAll();

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

    @Test
    public void testFlushes() throws IOException {
        final NamedOdsFileWriter o = PowerMock.createMock(NamedOdsFileWriter.class);

        PowerMock.resetAll();
        o.update(EasyMock.isA(BeginTableFlusher.class));
        o.update(EasyMock.isA(EndTableFlusher.class));

        PowerMock.replayAll();
        this.builder.addObserver(o);
        this.builder.asyncFlushBeginTable(this.appender);
        this.builder.asyncFlushEndTable(this.appender);

        PowerMock.verifyAll();
    }

    @Test
    public void testSetting() {
        PowerMock.resetAll();

        PowerMock.replayAll();
        this.builder.setConfigItem("item", "string", "value");
        this.builder.updateConfigItem("item", "value");

        PowerMock.verifyAll();
        final ConfigItem item = (ConfigItem) this.ce.getByName("item");
        Assert.assertEquals("item", item.getName());
        Assert.assertEquals("string", item.getType());
        Assert.assertEquals("value", item.getValue());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetRowsSpannedNeg() throws IOException {
        PowerMock.resetAll();

        PowerMock.replayAll();
        this.builder.setRowsSpanned(this.table, this.appender, 2, 2, -1);

        PowerMock.verifyAll();

    }

    @Test
    public void testSetRowsSpanned1() throws IOException {
        PowerMock.resetAll();

        PowerMock.replayAll();
        this.builder.setRowsSpanned(this.table, this.appender, 2, 2, 1);

        PowerMock.verifyAll();

    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetRowsSpannedCovered() throws IOException {
        PowerMock.resetAll();

        PowerMock.replayAll();
        this.builder.setCellMerge(this.table, this.appender, 1, 1, 5, 5);
        assert this.builder.getRow(this.table, this.appender, 2).getOrCreateCell(2).isCovered();
        this.builder.setRowsSpanned(this.table, this.appender, 2, 2, 2);

        PowerMock.verifyAll();

    }

    @Test
    public void testSetRowsSpanned() throws IOException {
        PowerMock.resetAll();

        PowerMock.replayAll();
        this.builder.setRowsSpanned(this.table, this.appender, 2, 2, 2);

        PowerMock.verifyAll();

    }

    @Test(expected = IllegalArgumentException.class)
    public void testColumnStyle() {
        PowerMock.resetAll();

        PowerMock.replayAll();
        this.builder.setColumnStyle(-1, null);

        PowerMock.verifyAll();

    }

    @Test
    public void testNotify() throws IOException {
        final NamedOdsFileWriter o = PowerMock.createMock(NamedOdsFileWriter.class);

        PowerMock.resetAll();
        o.update(EasyMock.isA(PreprocessedRowsFlusher.class));

        PowerMock.replayAll();
        this.builder.addObserver(o);
        this.builder.getRow(this.table, this.appender, 1024);

        PowerMock.verifyAll();
    }

    @Test
    public final void testFindDefaultCellStyle() {
        PowerMock.resetAll();
        PowerMock.replayAll();
        Assert.assertEquals(TableCellStyle.DEFAULT_CELL_STYLE,
                this.builder.findDefaultCellStyle(10));
        PowerMock.verifyAll();
    }
}
