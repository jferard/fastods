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

import com.github.jferard.fastods.datastyle.BooleanStyle;
import com.github.jferard.fastods.datastyle.DataStyles;
import com.github.jferard.fastods.datastyle.DataStylesBuilder;
import com.github.jferard.fastods.odselement.ContentElement;
import com.github.jferard.fastods.odselement.StylesContainer;
import com.github.jferard.fastods.odselement.StylesContainerImpl;
import com.github.jferard.fastods.odselement.config.ConfigItemMapEntry;
import com.github.jferard.fastods.ref.PositionUtil;
import com.github.jferard.fastods.ref.TableNameUtil;
import com.github.jferard.fastods.style.TableCellStyle;
import com.github.jferard.fastods.style.TableColumnStyle;
import com.github.jferard.fastods.style.TableStyle;
import com.github.jferard.fastods.testlib.DomTester;
import com.github.jferard.fastods.util.AutoFilter;
import com.github.jferard.fastods.util.WriteUtil;
import com.github.jferard.fastods.util.XMLUtil;
import com.google.common.collect.Lists;
import org.easymock.Capture;
import org.easymock.EasyMock;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.powermock.api.easymock.PowerMock;

import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import static com.github.jferard.fastods.odselement.config.ConfigElement.ZOOM_VALUE;

public class TableTest {
    private DataStyles ds;
    private StylesContainer stc;
    private Table table;
    private XMLUtil xmlUtil;
    private StringBuilder sb;
    private ContentElement ce;
    private TableBuilder tb;
    private Table tableWithMockBuilder;
    private TableAppender ta;

    @Before
    public void setUp() {
        this.ce = PowerMock.createMock(ContentElement.class);
        this.stc = PowerMock.createMock(StylesContainerImpl.class);
        final PositionUtil positionUtil = new PositionUtil(new TableNameUtil());
        final XMLUtil xmlUtil = XMLUtil.create();
        this.ds = DataStylesBuilder.create(Locale.US).build();
        this.table =
                Table.create(this.ce, positionUtil, WriteUtil.create(), xmlUtil, "my_table", 10,
                        100, this.stc, this.ds, false);
        this.xmlUtil = xmlUtil;
        this.sb = new StringBuilder();

        this.tb = PowerMock.createMock(TableBuilder.class);
        this.ta = PowerMock.createMock(TableAppender.class);

        this.tableWithMockBuilder = new Table("test", this.ce, this.tb, this.ta);
    }

    @Test
    public final void testContentEntry() throws IOException {
        final List<TableColumnStyle> tcss = new ArrayList<TableColumnStyle>(4);
        for (int c = 0; c < 3; c++) {
            final TableColumnStyle tcs = TableColumnStyle.builder("test" + c).build();
            tcss.add(tcs);
        }

        PowerMock.resetAll();
        for (int c = 0; c < 3; c++) {
            final TableColumnStyle tcs = tcss.get(c);
            EasyMock.expect(this.stc.addContentFontFaceContainerStyle(tcs)).andReturn(true);
            EasyMock.expect(this.stc.addContentStyle(tcs.getDefaultCellStyle())).andReturn(true);
            EasyMock.expect(this.stc.addContentStyle(tcs)).andReturn(true);
        }

        PowerMock.replayAll();
        for (int c = 0; c < 3; c++) {
            final TableColumnStyle tcs = tcss.get(c);
            this.table.setColumnStyle(c, tcs);
        }
        this.table.getRow(100);
        this.assertTableXMLEquals("<table:table table:name=\"my_table\" table:style-name=\"ta1\" " +
                "table:print=\"false\">" + "<office:forms form:automatic-focus=\"false\" " +
                "form:apply-design-mode=\"false\"/>" +
                "<table:table-column table:style-name=\"test0\" " +
                "table:default-cell-style-name=\"Default\"/>" +
                "<table:table-column table:style-name=\"test1\" " +
                "table:default-cell-style-name=\"Default\"/>" + "<table:table-column " +
                "table:style-name=\"test2\" table:default-cell-style-name=\"Default\"/>" +
                "<table:table-column table:style-name=\"co1\" " +
                "table:default-cell-style-name=\"Default\" " +
                "table:number-columns-repeated=\"1021\"/>" + "<table:table-row " +
                "table:number-rows-repeated=\"100\" table:style-name=\"ro1\">" +
                "<table:table-cell/>" + "</table:table-row>" +
                "<table:table-row table:style-name=\"ro1\">" + "</table:table-row>" +
                "</table:table>");

        PowerMock.verifyAll();
    }

    @Test
    public final void testGetRow() throws IOException {
        PowerMock.resetAll();

        PowerMock.replayAll();
        final List<TableRowImpl> rows = Lists.newArrayList();
        for (int r = 0; r < 7; r++) { // 8 times
            rows.add(this.table.nextRow());
        }

        for (int r = 0; r < 7; r++) { // 8 times
            Assert.assertEquals(rows.get(r), this.table.getRow(r));
        }

        PowerMock.verifyAll();
    }

    @Test
    public final void testGetRowHundred() throws IOException {
        PowerMock.resetAll();

        PowerMock.replayAll();
        for (int r = 0; r < 7; r++) { // 8 times
            this.table.nextRow();
        }
        this.table.getRow(100);
        final int rowCount = this.table.getRowCount();

        PowerMock.verifyAll();
        Assert.assertEquals(101, rowCount);
    }

    @Test(expected = IllegalArgumentException.class)
    public final void testGetRowNegative() throws IOException {
        PowerMock.resetAll();
        PowerMock.replayAll();
        this.table.getRow(-1);
        PowerMock.verifyAll();
    }

    @Test
    public final void testLastRow() throws IOException {
        PowerMock.resetAll();

        PowerMock.replayAll();
        final int initialRowCount = this.table.getRowCount();
        for (int r = 0; r < 7; r++) { // 8 times
            this.table.nextRow();
        }
        final int rowCount = this.table.getRowCount();

        PowerMock.verifyAll();
        Assert.assertEquals(0, initialRowCount);
        Assert.assertEquals(7, rowCount);
    }

    @Test
    public final void testRowsSpanned() throws IOException {
        PowerMock.resetAll();

        PowerMock.replayAll();
        this.table.setRowsSpanned(10, 9, 8);
        final String ROW = "<table:table-row table:style-name=\"ro1\"><table:table-cell " +
                "table:number-columns-repeated=\"9\"/><table:covered-table-cell/></table:table" +
                "-row>";
        this.assertTableXMLEquals("<table:table table:name=\"my_table\" table:style-name=\"ta1\" " +
                "table:print=\"false\">" + "<office:forms form:automatic-focus=\"false\" " +
                "form:apply-design-mode=\"false\"/>" +
                "<table:table-column table:style-name=\"co1\" " +
                "table:number-columns-repeated=\"1024\" " +
                "table:default-cell-style-name=\"Default\"/>" + "<table:table-row " +
                "table:number-rows-repeated=\"10\" table:style-name=\"ro1\">" +
                "<table:table-cell/>" + "</table:table-row>" +
                "<table:table-row table:style-name=\"ro1\">" + "<table:table-cell " +
                "table:number-columns-repeated=\"9\"/>" + "<table:table-cell " +
                "table:number-rows-spanned=\"8\"/>" + "</table:table-row>" + ROW + ROW + ROW + ROW +
                ROW + ROW + ROW + "</table:table>");

        PowerMock.verifyAll();
    }

    @Test
    public final void testMerge() throws IOException {
        PowerMock.resetAll();
        this.tb.setCellMerge(EasyMock.eq(this.tableWithMockBuilder),
                EasyMock.isA(TableAppender.class), EasyMock.eq(1), EasyMock.eq(1), EasyMock.eq(2),
                EasyMock.eq(3));

        PowerMock.replayAll();
        this.tableWithMockBuilder.setCellMerge(1, 1, 2, 3);

        PowerMock.verifyAll();
    }

    @Test
    @SuppressWarnings("deprecated")
    public final void testMergePos() throws IOException, ParseException {
        PowerMock.resetAll();
        this.tb.setCellMerge(EasyMock.eq(this.tableWithMockBuilder),
                EasyMock.isA(TableAppender.class), EasyMock.eq("A1"), EasyMock.eq(2),
                EasyMock.eq(3));

        PowerMock.replayAll();
        this.tableWithMockBuilder.setCellMerge("A1", 2, 3);

        PowerMock.verifyAll();
    }

    @Test
    public final void testStyle() {
        final TableStyle ts = TableStyle.builder("b").build();

        PowerMock.resetAll();
        EasyMock.expect(this.stc.addContentStyle(ts)).andReturn(true);
        EasyMock.expect(this.stc.addPageStyle(ts.getPageStyle())).andReturn(true);

        PowerMock.replayAll();
        this.table.setStyle(ts);
        Assert.assertEquals("my_table", this.table.getName());
        Assert.assertEquals("b", this.table.getStyleName());

        PowerMock.verifyAll();
    }

    @Test
    public final void testColumnStyle() throws IOException {
        PowerMock.resetAll();

        this.ta.appendAllAvailableRows(this.xmlUtil, this.sb);
        this.tableWithMockBuilder.setColumnStyle(0, null);

        PowerMock.replayAll();
        this.tableWithMockBuilder.flushAllAvailableRows(this.xmlUtil, this.sb);
        this.tableWithMockBuilder.setColumnStyle(0, null);

        PowerMock.verifyAll();
        Assert.assertEquals("", this.sb.toString());
    }

    @Test
    public final void testName() throws IOException {
        PowerMock.resetAll();
        this.ta.appendAllAvailableRows(this.xmlUtil, this.sb);

        PowerMock.replayAll();
        this.tableWithMockBuilder.flushAllAvailableRows(this.xmlUtil, this.sb);

        PowerMock.verifyAll();
        Assert.assertEquals("", this.sb.toString());
    }

    @Test
    public final void testConfigItem() {
        PowerMock.resetAll();
        this.tb.setConfigItem("item", "type", "value");

        PowerMock.replayAll();
        this.tableWithMockBuilder.setConfigItem("item", "type", "value");

        PowerMock.verifyAll();
    }

    @Test
    public final void testUpdateConfigItem() {
        PowerMock.resetAll();
        this.tb.updateConfigItem(ZOOM_VALUE.getName(), "value");

        PowerMock.replayAll();
        this.tableWithMockBuilder.updateConfigItem(ZOOM_VALUE, "value");

        PowerMock.verifyAll();
    }

    @Test
    public final void testAddAutoFilter() throws IOException {
        final Capture<AutoFilter> af = EasyMock.newCapture();

        PowerMock.resetAll();
        this.ce.addAutoFilter(EasyMock.capture(af));

        PowerMock.replayAll();
        this.tableWithMockBuilder.addAutoFilter(1, 2, 3, 4);

        PowerMock.verifyAll();
        TestHelper.assertXMLEquals("<table:database-range table:name=\"this\" " +
                "table:display-filter-buttons=\"true\" table:target-range-address=\"test" +
                ".C2:E4\"/>", af
                .getValue());
    }

    @Test
    public final void testAsyncFlushBeginTable() throws IOException {
        PowerMock.resetAll();
        this.tb.asyncFlushBeginTable(this.ta);

        PowerMock.replayAll();
        this.tableWithMockBuilder.asyncFlushBeginTable();

        PowerMock.verifyAll();
    }

    @Test
    public final void testAsyncFlushEndTable() throws IOException {
        PowerMock.resetAll();
        this.tb.asyncFlushEndTable(this.ta);

        PowerMock.replayAll();
        this.tableWithMockBuilder.asyncFlushEndTable();

        PowerMock.verifyAll();
    }

    @Test
    public final void testFlushRemainingRowsFrom() throws IOException {
        PowerMock.resetAll();
        this.ta.appendRemainingRowsFrom(this.xmlUtil, this.sb, 0);

        PowerMock.replayAll();
        this.tableWithMockBuilder.flushRemainingRowsFrom(this.xmlUtil, this.sb, 0);

        PowerMock.verifyAll();
    }

    @Test
    public final void testGetWalker() throws IOException {
        final TableRowImpl row = PowerMock.createMock(TableRowImpl.class);
        final TableCell cell = PowerMock.createMock(TableCell.class);

        PowerMock.resetAll();
        EasyMock.expect(this.tb.getRow(this.tableWithMockBuilder, this.ta, 0)).andReturn(row);
        EasyMock.expect(row.getOrCreateCell(0)).andReturn(cell);

        PowerMock.replayAll();
        this.tableWithMockBuilder.getWalker();

        PowerMock.verifyAll();
    }

    @Test
    public final void testGetConfigEntry() {
        final ConfigItemMapEntry entry = PowerMock.createMock(ConfigItemMapEntry.class);

        PowerMock.resetAll();
        EasyMock.expect(this.tb.getConfigEntry()).andReturn(entry);

        PowerMock.replayAll();
        final ConfigItemMapEntry e = this.tableWithMockBuilder.getConfigEntry();

        PowerMock.verifyAll();
        Assert.assertEquals(entry, e);
    }

    @Test(expected = IOException.class)
    public final void testFlushNoObserver() throws IOException {
        PowerMock.resetAll();

        PowerMock.replayAll();
        this.table.asyncFlush();

        PowerMock.verifyAll();
    }

    @Test
    public final void testFlush() throws IOException {
        final NamedOdsFileWriter now = PowerMock.createMock(NamedOdsFileWriter.class);

        PowerMock.resetAll();
        now.update(EasyMock.isA(BeginTableFlusher.class));
        now.update(EasyMock.isA(EndTableFlusher.class));

        PowerMock.replayAll();
        this.table.addObserver(now);
        this.table.asyncFlush();

        PowerMock.verifyAll();
    }

    @Test
    public final void testFlushSomeAvailableRows() throws IOException {
        final NamedOdsFileWriter writer = PowerMock.createMock(NamedOdsFileWriter.class);
        final StringBuilder app = new StringBuilder();
        final BooleanStyle bs = this.ds.getBooleanDataStyle();

        PowerMock.resetAll();
        writer.update(EasyMock.isA(BeginTableFlusher.class));
        EasyMock.expect(this.stc.addDataStyle(bs)).andReturn(true);
        EasyMock.expect(this.stc.addChildCellStyle(TableCellStyle.DEFAULT_CELL_STYLE, bs))
                .andReturn(null);

        PowerMock.replayAll();
        this.table.addObserver(writer);
        final TableRowImpl row = this.table.nextRow();
        row.getOrCreateCell(0).setBooleanValue(true);
        this.table.flushSomeAvailableRowsFrom(this.xmlUtil, app, 0);

        PowerMock.verifyAll();
        DomTester.assertEquals("<table:table table:name=\"my_table\" table:style-name=\"ta1\" " +
                        "table:print=\"false\"><office:forms form:automatic-focus=\"false\" " +
                        "form:apply-design-mode=\"false\"/><table:table-column " +
                        "table:style-name=\"co1\" " + "table:number-columns-repeated=\"1024\" " +
                        "table:default-cell-style-name=\"Default\"/><table:table-row " +
                        "table:style-name=\"ro1\"><table:table-cell office:value-type=\"boolean\"" +
                        " office:boolean-value=\"true\"/></table:table-row></table:table>",
                app.toString() + "</table:table>");
    }

    private void assertTableXMLEquals(final String xml) throws IOException {
        final StringBuilder sb = new StringBuilder();
        this.table.appendXMLContent(this.xmlUtil, sb);
        DomTester.assertEquals(xml, sb.toString());
    }
}
