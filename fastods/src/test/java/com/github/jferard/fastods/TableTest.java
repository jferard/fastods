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

import static com.github.jferard.fastods.odselement.config.ConfigElement.ZOOM_VALUE;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import org.easymock.Capture;
import org.easymock.EasyMock;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.powermock.api.easymock.PowerMock;

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
import com.github.jferard.fastods.util.IntegerRepresentationCache;
import com.github.jferard.fastods.util.Protection;
import com.github.jferard.fastods.util.SVGRectangle;
import com.github.jferard.fastods.util.XMLUtil;

public class TableTest {
    private DataStyles ds;
    private StylesContainer stc;
    private Table table;
    private XMLUtil xmlUtil;
    private StringBuilder sb;
    private ContentElement ce;
    private TableModel model;
    private Table tableWithMockModel;
    private TableAppender appender;

    @Before
    public void setUp() {
        this.ce = PowerMock.createMock(ContentElement.class);
        this.stc = PowerMock.createMock(StylesContainerImpl.class);
        final PositionUtil positionUtil = new PositionUtil(new TableNameUtil());
        final XMLUtil xmlUtil = XMLUtil.create();
        this.ds = DataStylesBuilder.create(Locale.US).build();
        this.table =
                Table.create(this.ce, positionUtil, IntegerRepresentationCache.create(), xmlUtil,
                        "my_table", 10,
                        100, this.stc, this.ds, false, new ValidationsContainer());
        this.xmlUtil = xmlUtil;
        this.sb = new StringBuilder();

        this.model = PowerMock.createMock(TableModel.class);
        this.appender = PowerMock.createMock(TableAppender.class);

        this.tableWithMockModel = new Table("test", this.ce, this.model, this.appender);
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
//            EasyMock.expect(this.stc.addContentFontFaceContainerStyle(tcs)).andReturn(true);
//            EasyMock.expect(this.stc.addContentStyle(tcs.getDefaultCellStyle())).andReturn(true);
            EasyMock.expect(this.stc.addContentStyle(tcs)).andReturn(true);
        }

        PowerMock.replayAll();
        for (int c = 0; c < 3; c++) {
            final TableColumnStyle tcs = tcss.get(c);
            this.table.setColumnStyle(c, tcs);
        }
        this.table.getRow(100);
        this.assertTableXMLEquals("<table:table table:name=\"my_table\" table:style-name=\"ta1\" " +
                "table:print=\"false\">" +
                "<table:table-column table:style-name=\"test0\" " +
                "table:default-cell-style-name=\"Default\"/>" +
                "<table:table-column table:style-name=\"test1\" " +
                "table:default-cell-style-name=\"Default\"/>" + "<table:table-column " +
                "table:style-name=\"test2\" table:default-cell-style-name=\"Default\"/>" +
                "<table:table-column table:style-name=\"co1\" " +
                "table:default-cell-style-name=\"Default\" " +
                "table:number-columns-repeated=\"97\"/>" + "<table:table-row " +
                "table:number-rows-repeated=\"100\" table:style-name=\"ro1\">" +
                "<table:table-cell/>" + "</table:table-row>" +
                "<table:table-row table:style-name=\"ro1\">" +
                "<table:table-cell/>" + "</table:table-row>" +
                "</table:table>");

        PowerMock.verifyAll();
    }

    @Test
    public final void testGetRow() throws IOException {
        PowerMock.resetAll();

        PowerMock.replayAll();
        final List<TableRowImpl> rows = new ArrayList<TableRowImpl>();
        for (int r = 0; r < 7; r++) { // 8 times
            rows.add(this.table.getRow(r));
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
            this.table.getRow(r);
        }
        this.table.getRow(100);
        final int rowCount = this.table.getRowCount();

        PowerMock.verifyAll();
        Assert.assertEquals(101, rowCount);
    }

    @Test
    public final void testGetRowNegative() throws IOException {
        PowerMock.resetAll();
        PowerMock.replayAll();
        Assert.assertThrows(IllegalArgumentException.class, () -> this.table.getRow(-1));
        PowerMock.verifyAll();
    }

    @Test
    public final void testLastRow() throws IOException {
        PowerMock.resetAll();

        PowerMock.replayAll();
        final int initialRowCount = this.table.getRowCount();
        for (int r = 0; r < 7; r++) { // 8 times
            this.table.getRow(r);
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
                "table:print=\"false\">" +
                "<table:table-column table:style-name=\"co1\" " +
                "table:number-columns-repeated=\"100\" " +
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
        this.model.setCellMerge(this.tableWithMockModel,
                this.appender, 1, 1, 2, 3);

        PowerMock.replayAll();
        this.tableWithMockModel.setCellMerge(1, 1, 2, 3);

        PowerMock.verifyAll();
    }

    @Test
    public final void testMergePos() throws IOException {
        PowerMock.resetAll();
        this.model.setCellMerge(EasyMock.eq(this.tableWithMockModel),
                EasyMock.isA(TableAppender.class), EasyMock.eq(0),
                EasyMock.eq(1), EasyMock.eq(2),
                EasyMock.eq(3));

        PowerMock.replayAll();
        this.tableWithMockModel.setCellMerge(0, 1, 2, 3);

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

        this.appender.appendAllAvailableRows(this.xmlUtil, this.sb);
        this.tableWithMockModel.setColumnStyle(0, null);

        PowerMock.replayAll();
        this.tableWithMockModel.flushAllAvailableRows(this.xmlUtil, this.sb);
        this.tableWithMockModel.setColumnStyle(0, null);

        PowerMock.verifyAll();
        Assert.assertEquals("", this.sb.toString());
    }

    @Test
    public final void testName() throws IOException {
        PowerMock.resetAll();
        this.appender.appendAllAvailableRows(this.xmlUtil, this.sb);

        PowerMock.replayAll();
        this.tableWithMockModel.flushAllAvailableRows(this.xmlUtil, this.sb);

        PowerMock.verifyAll();
        Assert.assertEquals("", this.sb.toString());
    }

    @Test
    public final void testConfigItem() {
        PowerMock.resetAll();
        this.model.setConfigItem("item", "type", "value");

        PowerMock.replayAll();
        this.tableWithMockModel.setConfigItem("item", "type", "value");

        PowerMock.verifyAll();
    }

    @Test
    public final void testUpdateConfigItem() {
        PowerMock.resetAll();
        this.model.updateConfigItem(ZOOM_VALUE.getName(), "value");

        PowerMock.replayAll();
        this.tableWithMockModel.updateConfigItem(ZOOM_VALUE, "value");

        PowerMock.verifyAll();
    }

    @Test
    public final void testAddAutoFilter() throws IOException {
        final Capture<AutoFilter> af = EasyMock.newCapture();

        PowerMock.resetAll();
        this.ce.addAutoFilter(EasyMock.capture(af));

        PowerMock.replayAll();
        this.tableWithMockModel.addAutoFilter("range", 1, 2, 3, 4);

        PowerMock.verifyAll();
        TestHelper.assertXMLEquals("<table:database-range table:name=\"range\" " +
                "table:display-filter-buttons=\"true\" " +
                "table:target-range-address=\"test.C2:test.E4\"/>", af.getValue());
    }

    @Test
    public final void testAsyncFlushBeginTable() throws IOException {
        PowerMock.resetAll();
        this.model.asyncFlushBeginTable(this.appender);

        PowerMock.replayAll();
        this.tableWithMockModel.asyncFlushBeginTable();

        PowerMock.verifyAll();
    }

    @Test
    public final void testAsyncFlushEndTable() throws IOException {
        PowerMock.resetAll();
        this.model.asyncFlushEndTable(this.appender);

        PowerMock.replayAll();
        this.tableWithMockModel.asyncFlushEndTable();

        PowerMock.verifyAll();
    }

    @Test
    public final void testFlushRemainingRowsFrom() throws IOException {
        PowerMock.resetAll();
        this.appender.appendRemainingRowsFrom(this.xmlUtil, this.sb, 0);

        PowerMock.replayAll();
        this.tableWithMockModel.flushRemainingRowsFrom(this.xmlUtil, this.sb, 0);

        PowerMock.verifyAll();
    }

    @Test
    public final void testGetWalker() throws IOException {
        final TableRowImpl row = PowerMock.createMock(TableRowImpl.class);
        final TableCell cell = PowerMock.createMock(TableCell.class);

        PowerMock.resetAll();
        EasyMock.expect(this.model.getRow(this.tableWithMockModel, this.appender, 0))
                .andReturn(row);
        EasyMock.expect(row.getOrCreateCell(0)).andReturn(cell);

        PowerMock.replayAll();
        this.tableWithMockModel.getWalker();

        PowerMock.verifyAll();
    }

    @Test
    public final void testGetConfigEntry() {
        final ConfigItemMapEntry entry = PowerMock.createMock(ConfigItemMapEntry.class);

        PowerMock.resetAll();
        EasyMock.expect(this.model.getConfigEntry()).andReturn(entry);

        PowerMock.replayAll();
        final ConfigItemMapEntry e = this.tableWithMockModel.getConfigEntry();

        PowerMock.verifyAll();
        Assert.assertEquals(entry, e);
    }

    @Test
    @Deprecated
    public final void testFlushNoObserver() {
        PowerMock.resetAll();

        PowerMock.replayAll();
        Assert.assertThrows(IOException.class, () -> this.table.asyncFlush());

        PowerMock.verifyAll();
    }

    @Test
    @Deprecated
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
        final StringBuilder sb = new StringBuilder();
        final BooleanStyle bs = this.ds.getBooleanDataStyle();

        PowerMock.resetAll();
        writer.update(EasyMock.isA(BeginTableFlusher.class));
        EasyMock.expect(this.stc.addDataStyle(bs)).andReturn(true);
        EasyMock.expect(this.stc.addChildCellStyle(TableCellStyle.DEFAULT_CELL_STYLE, bs))
                .andReturn(null);

        PowerMock.replayAll();
        this.table.addObserver(writer);
        final TableCellWalker walker = this.table.getWalker();
        walker.setBooleanValue(true);
        this.table.flushSomeAvailableRowsFrom(this.xmlUtil, sb, 0);

        PowerMock.verifyAll();
        DomTester.assertEquals("<table:table table:name=\"my_table\" table:style-name=\"ta1\" " +
                        "table:print=\"false\"><table:table-column " +
                        "table:style-name=\"co1\" " + "table:number-columns-repeated=\"100\" " +
                        "table:default-cell-style-name=\"Default\"/><table:table-row " +
                        "table:style-name=\"ro1\"><table:table-cell office:value-type=\"boolean\"" +
                        " office:boolean-value=\"true\"/></table:table-row></table:table>",
                sb.toString() + "</table:table>");
    }

    @Test
    public final void testMergeWithPosString() throws IOException, ParseException {
        PowerMock.resetAll();
        this.model.setCellMerge(this.tableWithMockModel, this.appender, "A3", 2, 2);

        PowerMock.replayAll();
        this.tableWithMockModel.setCellMerge("A3", 2, 2);
        PowerMock.verifyAll();
    }

    @Test
    public final void testAddPrintRange() {
        PowerMock.resetAll();
        this.model.addPrintRange(1, 2, 3, 4);

        PowerMock.replayAll();
        this.tableWithMockModel.addPrintRange(1, 2, 3, 4);

        PowerMock.verifyAll();
    }

    @Test
    public final void testNextRow() throws IOException {
        final TableRowImpl row = PowerMock.createMock(TableRowImpl.class);

        PowerMock.resetAll();
        EasyMock.expect(this.model.nextRow(this.tableWithMockModel, this.appender)).andReturn(row);

        PowerMock.replayAll();
        final TableRowImpl row1 = this.tableWithMockModel.nextRow();

        PowerMock.verifyAll();
        Assert.assertEquals(row, row1);
    }

    @Test
    public final void testSetColumnAttribute() throws IOException {
        PowerMock.resetAll();
        this.model.setColumnAttribute(1, "attr", "value");

        PowerMock.replayAll();
        this.tableWithMockModel.setColumnAttribute(1, "attr", "value");

        PowerMock.verifyAll();
    }

    @Test
    public final void testSetColumnStyle() {
        final TableCellStyle cellStyle = TableCellStyle.builder("test").fontWeightBold().build();

        PowerMock.resetAll();
        this.model.setColumnDefaultCellStyle(1, cellStyle);

        PowerMock.replayAll();
        this.tableWithMockModel.setColumnDefaultCellStyle(1, cellStyle);

        PowerMock.verifyAll();
    }

    @Test
    public final void testSetAttribute() {
        PowerMock.resetAll();
        this.model.setAttribute("attr", "value");

        PowerMock.replayAll();
        this.tableWithMockModel.setAttribute("attr", "value");

        PowerMock.verifyAll();
    }

    @Test
    public final void testAddShape() {
        final Shape s =
                DrawFrame.builder("df", new DrawImage("href"), SVGRectangle.cm(1, 2, 3, 4)).build();

        PowerMock.resetAll();
        this.model.addShape(s);

        PowerMock.replayAll();
        this.tableWithMockModel.addShape(s);

        PowerMock.verifyAll();
    }

    @Test
    public final void testProtect() throws NoSuchAlgorithmException {
        final Protection protection = new Protection("b", "c");

        PowerMock.resetAll();
        this.model.protect(protection);

        PowerMock.replayAll();
        this.tableWithMockModel.protect(protection);

        PowerMock.verifyAll();
    }

    @Test
    public final void testHeader() throws IOException {
        PowerMock.resetAll();
        this.model.setHeaderRowsCount(3);
        this.model.setHeaderColumnsCount(1);

        PowerMock.replayAll();
        this.tableWithMockModel.setHeaderRowsCount(3);
        this.tableWithMockModel.setHeaderColumnsCount(1);

        PowerMock.verifyAll();
    }

    private void assertTableXMLEquals(final String xml) throws IOException {
        final StringBuilder sb = new StringBuilder();
        this.table.appendXMLContent(this.xmlUtil, sb);
        DomTester.assertEquals(xml, sb.toString());
    }
}
