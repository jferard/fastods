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
package com.github.jferard.fastods;

import com.github.jferard.fastods.datastyle.DataStyles;
import com.github.jferard.fastods.datastyle.DataStylesBuilder;
import com.github.jferard.fastods.odselement.StylesContainer;
import com.github.jferard.fastods.style.TableColumnStyle;
import com.github.jferard.fastods.style.TableStyle;
import com.github.jferard.fastods.testlib.DomTester;
import com.github.jferard.fastods.util.EqualityUtil;
import com.github.jferard.fastods.util.FullList;
import com.github.jferard.fastods.util.PositionUtil;
import com.github.jferard.fastods.util.TableNameUtil;
import com.github.jferard.fastods.util.WriteUtil;
import com.github.jferard.fastods.util.XMLUtil;
import com.google.common.collect.Lists;
import org.easymock.EasyMock;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.powermock.api.easymock.PowerMock;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class TableTest {
    private DataStyles ds;
    private StylesContainer stc;
    private Table table;
    private XMLUtil xmlUtil;
    private StringBuilder sb;

    @Before
    public void setUp() {
        this.stc = PowerMock.createMock(StylesContainer.class);
        final PositionUtil positionUtil = new PositionUtil(new EqualityUtil(), new TableNameUtil());
        final XMLUtil xmlUtil = XMLUtil.create();
        this.ds = DataStylesBuilder.create(Locale.US).build();
        this.table = Table.create(positionUtil, WriteUtil.create(), xmlUtil, "mytable", 10, 100,
                this.stc, this.ds);
        this.xmlUtil = xmlUtil;
        this.sb = new StringBuilder();
        PowerMock.resetAll();
    }

    @After
    public void tearDown() {
        PowerMock.verifyAll();
    }

    @Test
    public final void testContentEntry() throws IOException, FastOdsException {
        // CREATE
        final List<TableColumnStyle> tcss = new ArrayList<TableColumnStyle>(4);
        for (int c = 0; c < 3; c++) {
            final TableColumnStyle tcs = TableColumnStyle.builder("test" + Integer.toString(c)).build();
            tcss.add(tcs);
        }

        // PLAY
        for (int c = 0; c < 3; c++) {
            final TableColumnStyle tcs = tcss.get(c);
            EasyMock.expect(this.stc.addContentStyle(tcs.getDefaultCellStyle())).andReturn(true);
            EasyMock.expect(this.stc.addContentStyle(tcs)).andReturn(true);
        }

        PowerMock.replayAll();
        for (int c = 0; c < 3; c++) {
            final TableColumnStyle tcs = tcss.get(c);
            this.table.setColumnStyle(c, tcs);
        }
        this.table.getRow(100);
        this.assertTableXMLEquals(
                "<table:table table:name=\"mytable\" table:style-name=\"ta1\" table:print=\"false\">" +
                        "<office:forms form:automatic-focus=\"false\" form:apply-design-mode=\"false\"/>" +
                        "<table:table-column table:style-name=\"test0\" table:default-cell-style-name=\"Default\"/>"
                        + "<table:table-column table:style-name=\"test1\" " +
                        "table:default-cell-style-name=\"Default\"/>" + "<table:table-column " +
                        "table:style-name=\"test2\" table:default-cell-style-name=\"Default\"/>" +
                        "<table:table-column table:style-name=\"co1\" table:default-cell-style-name=\"Default\" " +
                        "table:number-columns-repeated=\"1021\"/>" + "<table:table-row " +
                        "table:number-rows-repeated=\"100\" table:style-name=\"ro1\">" + "<table:table-cell/>" +
                        "</table:table-row>" + "<table:table-row table:style-name=\"ro1\">" + "</table:table-row>" +
                        "</table:table>");

    }

    @Test
    public final void testDataWrapper() throws IOException {
        final DataWrapper data = PowerMock.createMock(DataWrapper.class);
        EasyMock.expect(data.addToTable(this.table)).andReturn(true);
        PowerMock.replayAll();
        this.table.addData(data);
    }

    @Test
    public final void testGetRow() throws FastOdsException, IOException {
        PowerMock.replayAll();
        final List<TableRow> rows = Lists.newArrayList();
        for (int r = 0; r < 7; r++) { // 8 times
            rows.add(this.table.nextRow());
        }

        for (int r = 0; r < 7; r++) { // 8 times
            Assert.assertEquals(rows.get(r), this.table.getRow(r));
        }
    }

    @Test
    public final void testGetRowFromStringPos() throws FastOdsException, IOException {
        PowerMock.replayAll();
        final List<TableRow> rows = Lists.newArrayList();
        for (int r = 0; r < 7; r++) { // 8 times
            rows.add(this.table.nextRow());
        }

        Assert.assertEquals(rows.get(4), this.table.getRow("A5"));
        PowerMock.verifyAll();
    }

    @Test
    public final void testGetRowHundred() throws FastOdsException, IOException {
        PowerMock.replayAll();
        for (int r = 0; r < 7; r++) { // 8 times
            this.table.nextRow();
        }
        this.table.getRow(100);
        Assert.assertEquals(100, this.table.getLastRowNumber());
    }

    @Test(expected = FastOdsException.class)
    public final void testGetRowNegative() throws FastOdsException, IOException {
        PowerMock.replayAll();
        this.table.getRow(-1);
    }

    @Test
    public final void testLastRow() throws IOException {
        PowerMock.replayAll();
        Assert.assertEquals(-1, this.table.getLastRowNumber());
        for (int r = 0; r < 7; r++) { // 8 times
            this.table.nextRow();
        }
        Assert.assertEquals(6, this.table.getLastRowNumber());
    }

    @Test
    public final void testRowsSpanned() throws IOException {
        // PLAY

        PowerMock.replayAll();
        this.table.setRowsSpanned(10, 11, 12);
        this.assertTableXMLEquals(
                "<table:table table:name=\"mytable\" table:style-name=\"ta1\" table:print=\"false\">" +
                        "<office:forms form:automatic-focus=\"false\" form:apply-design-mode=\"false\"/>" +
                        "<table:table-column table:style-name=\"co1\" table:number-columns-repeated=\"1024\" " +
                        "table:default-cell-style-name=\"Default\"/>" + "<table:table-row " +
                        "table:number-rows-repeated=\"10\" table:style-name=\"ro1\">" + "<table:table-cell/>" +
                        "</table:table-row>" + "<table:table-row table:style-name=\"ro1\">" + "<table:table-cell " +
                        "table:number-columns-repeated=\"11\"/>" + "<table:table-cell " +
                        "table:number-rows-spanned=\"12\"/>" + "</table:table-row>" + "<table:table-row " +
                        "table:style-name=\"ro1\">" + "<table:table-cell table:number-columns-repeated=\"11\"/>" +
                        "<table:covered-table-cell/>" + "</table:table-row>" + "<table:table-row " +
                        "table:style-name=\"ro1\">" + "<table:table-cell table:number-columns-repeated=\"11\"/>" +
                        "<table:covered-table-cell/>" + "</table:table-row>" + "<table:table-row " +
                        "table:style-name=\"ro1\">" + "<table:table-cell table:number-columns-repeated=\"11\"/>" +
                        "<table:covered-table-cell/>" + "</table:table-row>" + "<table:table-row " +
                        "table:style-name=\"ro1\">" + "<table:table-cell table:number-columns-repeated=\"11\"/>" +
                        "<table:covered-table-cell/>" + "</table:table-row>" + "<table:table-row " +
                        "table:style-name=\"ro1\">" + "<table:table-cell table:number-columns-repeated=\"11\"/>" +
                        "<table:covered-table-cell/>" + "</table:table-row>" + "<table:table-row " +
                        "table:style-name=\"ro1\">" + "<table:table-cell table:number-columns-repeated=\"11\"/>" +
                        "<table:covered-table-cell/>" + "</table:table-row>" + "<table:table-row " +
                        "table:style-name=\"ro1\">" + "<table:table-cell table:number-columns-repeated=\"11\"/>" +
                        "<table:covered-table-cell/>" + "</table:table-row>" + "<table:table-row " +
                        "table:style-name=\"ro1\">" + "<table:table-cell table:number-columns-repeated=\"11\"/>" +
                        "<table:covered-table-cell/>" + "</table:table-row>" + "<table:table-row " +
                        "table:style-name=\"ro1\">" + "<table:table-cell table:number-columns-repeated=\"11\"/>" +
                        "<table:covered-table-cell/>" + "</table:table-row>" + "<table:table-row " +
                        "table:style-name=\"ro1\">" + "<table:table-cell table:number-columns-repeated=\"11\"/>" +
                        "<table:covered-table-cell/>" + "</table:table-row>" + "<table:table-row " +
                        "table:style-name=\"ro1\">" + "<table:table-cell table:number-columns-repeated=\"11\"/>" +
                        "<table:covered-table-cell/>" + "</table:table-row>" + "</table:table>");
    }

    @Test
    public final void testMerge() throws IOException {
        final TableBuilder tb = PowerMock.createMock(TableBuilder.class);
        final Table t = new Table("test", tb);

        tb.setCellMerge(EasyMock.eq(t), EasyMock.isA(TableAppender.class), EasyMock.eq(1), EasyMock.eq(1),
                EasyMock.eq(2), EasyMock.eq(3));

        PowerMock.replayAll();
        t.setCellMerge(1, 1, 2, 3);
    }

    @Test
    public final void testMergePos() throws IOException, FastOdsException {
        final TableBuilder tb = PowerMock.createMock(TableBuilder.class);
        final Table t = new Table("test", tb);

        tb.setCellMerge(EasyMock.eq(t), EasyMock.isA(TableAppender.class), EasyMock.eq("A1"), EasyMock.eq(2),
                EasyMock.eq(3));

        PowerMock.replayAll();
        t.setCellMerge("A1", 2, 3);
    }

    @Test
    public final void testNameAndStyle() {
        // PLAY
        final TableStyle ts = TableStyle.builder("b").build();
        EasyMock.expect(this.stc.addContentStyle(ts)).andReturn(true);
        EasyMock.expect(this.stc.addPageStyle(ts.getPageStyle())).andReturn(true);

        PowerMock.replayAll();

        this.table.setName("tname");
        this.table.setStyle(ts);
        Assert.assertEquals("tname", this.table.getName());
        Assert.assertEquals("b", this.table.getStyleName());
    }

    @Test(expected = IllegalStateException.class)
    public final void testColumnStyle() throws IOException, FastOdsException {
        final TableBuilder tb = PowerMock.createMock(TableBuilder.class);
        final Table t = new Table("test", tb);

        EasyMock.expect(tb.getName()).andReturn("tb");
        EasyMock.expect(tb.getStyleName()).andReturn("tb-style");
        EasyMock.expect(tb.getColumnStyles()).andReturn(FullList.<TableColumnStyle>builder().build());
        EasyMock.expect(tb.getTableRowsUsedSize()).andReturn(0);

        PowerMock.replayAll();
        t.flushAllAvailableRows(this.xmlUtil, this.sb);
        t.setColumnStyle(0, null);
    }

    @Test(expected = IllegalStateException.class)
    public final void testName() throws IOException, FastOdsException {
        final TableBuilder tb = PowerMock.createMock(TableBuilder.class);
        final Table t = new Table("test", tb);

        EasyMock.expect(tb.getName()).andReturn("tb");
        EasyMock.expect(tb.getStyleName()).andReturn("tb-style");
        EasyMock.expect(tb.getColumnStyles()).andReturn(FullList.<TableColumnStyle>builder().build());
        EasyMock.expect(tb.getTableRowsUsedSize()).andReturn(0);

        PowerMock.replayAll();
        t.flushAllAvailableRows(this.xmlUtil, this.sb);
        t.setName("ko");
    }

    @Test
    public final void testConfigItem() throws IOException, FastOdsException {
        final TableBuilder tb = PowerMock.createMock(TableBuilder.class);
        final Table t = new Table("test", tb);

        tb.setConfigItem("item", "type", "value");

        PowerMock.replayAll();
        t.setConfigItem("item", "type", "value");
    }

    private void assertTableXMLEquals(final String xml) throws IOException {
        final StringBuilder sb = new StringBuilder();
        this.table.appendXMLToContentEntry(this.xmlUtil, sb);
        DomTester.assertEquals(xml, sb.toString());
    }
}
