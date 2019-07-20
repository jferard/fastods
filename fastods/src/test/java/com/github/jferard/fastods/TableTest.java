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
import com.github.jferard.fastods.style.TableCellStyle;
import com.github.jferard.fastods.style.TableColumnStyle;
import com.github.jferard.fastods.style.TableStyle;
import com.github.jferard.fastods.testlib.DomTester;
import com.github.jferard.fastods.util.FastFullList;
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
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class TableTest {
    private DataStyles ds;
    private StylesContainer stc;
    private Table table;
    private XMLUtil xmlUtil;
    private StringBuilder sb;
    private ContentElement ce;

    @Before
    public void setUp() {
        this.ce = PowerMock.createMock(ContentElement.class);
        this.stc = PowerMock.createMock(StylesContainer.class);
        final PositionUtil positionUtil = new PositionUtil(new TableNameUtil());
        final XMLUtil xmlUtil = XMLUtil.create();
        this.ds = DataStylesBuilder.create(Locale.US).build();
        this.table = Table
                .create(this.ce, positionUtil, WriteUtil.create(), xmlUtil, "my_table", 10, 100,
                        this.stc, this.ds, false);
        this.xmlUtil = xmlUtil;
        this.sb = new StringBuilder();
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
        final TableBuilder tb = PowerMock.createMock(TableBuilder.class);
        final Table t = new Table("test", this.ce, tb);

        PowerMock.resetAll();
        tb.setCellMerge(EasyMock.eq(t), EasyMock.isA(TableAppender.class), EasyMock.eq(1),
                EasyMock.eq(1), EasyMock.eq(2), EasyMock.eq(3));

        PowerMock.replayAll();
        t.setCellMerge(1, 1, 2, 3);

        PowerMock.verifyAll();
    }

    @Test
    @SuppressWarnings("deprecated")
    public final void testMergePos() throws IOException, ParseException {
        final TableBuilder tb = PowerMock.createMock(TableBuilder.class);
        final Table t = new Table("test", this.ce, tb);

        PowerMock.resetAll();
        tb.setCellMerge(EasyMock.eq(t), EasyMock.isA(TableAppender.class), EasyMock.eq("A1"),
                EasyMock.eq(2), EasyMock.eq(3));

        PowerMock.replayAll();
        t.setCellMerge("A1", 2, 3);

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
        final TableBuilder tb = PowerMock.createMock(TableBuilder.class);
        final Table t = new Table("test", this.ce, tb);

        PowerMock.resetAll();
        EasyMock.expect(tb.getName()).andReturn("tb");
        EasyMock.expect(tb.getStyleName()).andReturn("tb-style");
        EasyMock.expect(tb.getColumnStyles())
                .andReturn(FastFullList.<TableColumnStyle>builder().build());
        EasyMock.expect(tb.getTableRowsUsedSize()).andReturn(0);
        t.setColumnStyle(0, null);

        PowerMock.replayAll();
        t.flushAllAvailableRows(this.xmlUtil, this.sb);
        t.setColumnStyle(0, null);

        PowerMock.verifyAll();
    }

    @Test
    public final void testName() throws IOException {
        final TableBuilder tb = PowerMock.createMock(TableBuilder.class);
        final Table t = new Table("test", this.ce, tb);

        PowerMock.resetAll();
        EasyMock.expect(tb.getName()).andReturn("tb");
        EasyMock.expect(tb.getStyleName()).andReturn("tb-style");
        EasyMock.expect(tb.getColumnStyles())
                .andReturn(FastFullList.<TableColumnStyle>builder().build());
        EasyMock.expect(tb.getTableRowsUsedSize()).andReturn(0);

        PowerMock.replayAll();
        t.flushAllAvailableRows(this.xmlUtil, this.sb);

        PowerMock.verifyAll();
    }

    @Test
    public final void testConfigItem() {
        final TableBuilder tb = PowerMock.createMock(TableBuilder.class);
        final Table t = new Table("test", this.ce, tb);

        PowerMock.resetAll();
        tb.setConfigItem("item", "type", "value");

        PowerMock.replayAll();
        t.setConfigItem("item", "type", "value");

        PowerMock.verifyAll();
    }

    @Test
    public final void testUpdateConfigItem() {
        final TableBuilder tb = PowerMock.createMock(TableBuilder.class);
        final Table t = new Table("test", this.ce, tb);

        PowerMock.resetAll();
        tb.updateConfigItem("item", "value");

        PowerMock.replayAll();
        t.updateConfigItem("item", "value");

        PowerMock.verifyAll();
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
        final NamedOdsFileWriter now = PowerMock.createMock(NamedOdsFileWriter.class);
        final StringBuilder app = new StringBuilder();
        final BooleanStyle bs = this.ds.getBooleanDataStyle();

        PowerMock.resetAll();
        EasyMock.expect(this.stc.addDataStyle(bs)).andReturn(true);
        EasyMock.expect(this.stc.addChildCellStyle(TableCellStyle.DEFAULT_CELL_STYLE, bs))
                .andReturn(null);

        PowerMock.replayAll();
        this.table.addObserver(now);
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
        this.table.appendXMLToContentEntry(this.xmlUtil, sb);
        DomTester.assertEquals(xml, sb.toString());
    }
}
