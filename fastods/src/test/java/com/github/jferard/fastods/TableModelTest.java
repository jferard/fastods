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

import com.github.jferard.fastods.datastyle.DataStyles;
import com.github.jferard.fastods.datastyle.DataStylesBuilder;
import com.github.jferard.fastods.odselement.StylesContainer;
import com.github.jferard.fastods.odselement.StylesContainerImpl;
import com.github.jferard.fastods.odselement.config.ConfigItem;
import com.github.jferard.fastods.odselement.config.ConfigItemMapEntrySet;
import com.github.jferard.fastods.ref.PositionUtil;
import com.github.jferard.fastods.ref.TableNameUtil;
import com.github.jferard.fastods.style.TableCellStyle;
import com.github.jferard.fastods.style.TableColumnStyle;
import com.github.jferard.fastods.style.TableStyle;
import com.github.jferard.fastods.testlib.DomTester;
import com.github.jferard.fastods.util.FastFullList;
import com.github.jferard.fastods.util.IntegerRepresentationCache;
import com.github.jferard.fastods.util.Protection;
import com.github.jferard.fastods.util.SVGRectangle;
import com.github.jferard.fastods.util.XMLUtil;
import org.apache.jena.ext.com.google.common.collect.ImmutableMap;
import org.easymock.EasyMock;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.powermock.api.easymock.PowerMock;

import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

public class TableModelTest {
    private DataStyles ds;
    private StylesContainer stc;
    private TableModel model;
    private XMLUtil xmlUtil;
    private Table table;
    private TableAppender appender;
    private ConfigItemMapEntrySet ce;

    @Before
    public void setUp() {
        final PositionUtil positionUtil = new PositionUtil(new TableNameUtil());
        final XMLUtil xmlUtil = XMLUtil.create();

        this.stc = PowerMock.createMock(StylesContainerImpl.class);
        this.ds = DataStylesBuilder.create(Locale.US).build();
        this.ce = ConfigItemMapEntrySet.createSet("mytable");
        this.model =
                new TableModel(positionUtil, IntegerRepresentationCache.create(), xmlUtil,
                        this.stc, this.ds,
                        false, "mytable", 10, 100, this.ce, 2, new ValidationsContainer());
        this.xmlUtil = xmlUtil;

        this.table = PowerMock.createMock(Table.class);
        this.appender = PowerMock.createMock(TableAppender.class);
    }

    @Test
    public final void testColumnStyles() {
        final List<TableColumnStyle> tcss = new ArrayList<TableColumnStyle>();
        for (int c = 0; c < 10; c++) {
            final TableColumnStyle tcs = TableColumnStyle.builder("test" + c).build();
            tcss.add(tcs);
        }

        PowerMock.resetAll();
        for (int c = 0; c < 10; c++) {
            final TableColumnStyle tcs = tcss.get(c);
            EasyMock.expect(this.stc.addContentStyle(tcs)).andReturn(true);
        }
        PowerMock.replayAll();

        for (int c = 0; c < 10; c++) {
            final TableColumnStyle tcs = tcss.get(c);
            this.model.setColumnStyle(c, tcs);
        }
        //        Assert.assertEquals(tcss, this.builder.getColumnStyles());
        PowerMock.verifyAll();
    }

    @Test
    public final void testGetRow() throws IOException {
        final List<TableRowImpl> rows = new ArrayList<TableRowImpl>();
        for (int r = 0; r < 7; r++) { // 8 times
            rows.add(this.model.nextRow(this.table, this.appender));
        }

        PowerMock.resetAll();
        PowerMock.replayAll();
        for (int r = 0; r < 7; r++) { // 8 times
            Assert.assertEquals(rows.get(r), this.model.getRow(this.table, this.appender, r));
        }
        PowerMock.verifyAll();
    }

    @Test
    public final void testGetRowFromStringPos() throws IOException, ParseException {
        final List<TableRowImpl> rows = new ArrayList<TableRowImpl>();
        for (int r = 0; r < 7; r++) { // 8 times
            rows.add(this.model.nextRow(this.table, this.appender));
        }
        PowerMock.resetAll();

        PowerMock.replayAll();
        final TableRowImpl row = this.model.getRow(this.table, this.appender, "A5");

        PowerMock.verifyAll();
        Assert.assertEquals(rows.get(4), row);
    }

    @Test
    public final void testGetRowHundred() throws IOException {
        PowerMock.resetAll();

        PowerMock.replayAll();
        for (int r = 0; r < 7; r++) { // 8 times
            this.model.nextRow(this.table, this.appender);
        }
        this.model.getRow(this.table, this.appender, 100);
        Assert.assertEquals(101, this.model.getRowCount());

        PowerMock.verifyAll();
    }

    @Test
    public final void testGetRowNegative() throws IOException {
        PowerMock.resetAll();
        PowerMock.replayAll();
        Assert.assertThrows(IllegalArgumentException.class,
                () -> this.model.getRow(this.table, this.appender, -1));
        PowerMock.verifyAll();
    }

    @Test
    public final void testLastRow() throws IOException {
        PowerMock.resetAll();
        PowerMock.replayAll();
        final int initialRowCount = this.model.getRowCount();
        for (int r = 0; r < 7; r++) { // 8 times
            this.model.nextRow(this.table, this.appender);
        }
        final int rowCount = this.model.getRowCount();

        PowerMock.verifyAll();
        Assert.assertEquals(0, initialRowCount);
        Assert.assertEquals(7, rowCount);
    }

    @Test
    public final void testRowsSpanned() throws IOException {
        PowerMock.resetAll();

        PowerMock.replayAll();
        this.model.setRowsSpanned(this.table, this.appender, 10, 11, 12);

        PowerMock.verifyAll();
    }

    @Test
    public final void testMergeWithPosString() throws IOException, ParseException {
        PowerMock.resetAll();
        PowerMock.replayAll();
        this.model.setCellMerge(this.table, this.appender, "A3", 2, 2);
        PowerMock.verifyAll();
    }

    @Test
    public final void testMerge() throws IOException {
        PowerMock.resetAll();
        PowerMock.replayAll();
        this.model.setCellMerge(this.table, this.appender, 2, 1, 2, 2);
        PowerMock.verifyAll();
    }

    @Test
    public final void testMergeCovered() throws IOException {
        PowerMock.resetAll();
        PowerMock.replayAll();
        this.model.setCellMerge(this.table, this.appender, 0, 0, 22, 22);
        assert this.model.getRow(this.table, this.appender, 2).getOrCreateCell(1).isCovered();
        Assert.assertThrows(IllegalArgumentException.class,
                () -> this.model.setCellMerge(this.table, this.appender, 2, 1, 2, 2));
        PowerMock.verifyAll();
    }

    @Test
    public final void testMergeWithObserver1() throws IOException {
        final NamedOdsFileWriter writer = PowerMock.createMock(NamedOdsFileWriter.class);

        PowerMock.resetAll();
        this.model.addObserver(writer);
        writer.update(EasyMock.isA(BeginTableFlusher.class));

        PowerMock.replayAll();
        this.model.setCellMerge(this.table, this.appender, 2, 1, 2, 2);

        PowerMock.verifyAll();
    }

    @Test
    public final void testNameAndStyle() {
        final TableStyle ts = TableStyle.builder("b").build();

        PowerMock.resetAll();
        EasyMock.expect(this.stc.addContentStyle(ts)).andReturn(true);
        EasyMock.expect(this.stc.addPageStyle(ts.getPageStyle())).andReturn(true);

        PowerMock.replayAll();
        this.model.setName("tname");
        this.model.setStyle(ts);
        final String name = this.model.getName();
        final String styleName = this.model.getStyleName();

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
        this.model.addObserver(o);

        //        final TableRow row = this.builder.getRowSecure(this.table, this.appender,10,
        //        true);
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
        this.model.addObserver(o);
        this.model.asyncFlushBeginTable(this.appender);
        this.model.asyncFlushEndTable(this.appender);

        PowerMock.verifyAll();
    }

    @Test
    public void testSetting() {
        PowerMock.resetAll();

        PowerMock.replayAll();
        this.model.setConfigItem("item", "string", "value");
        this.model.updateConfigItem("item", "value");
        final ConfigItemMapEntrySet configEntry =
                (ConfigItemMapEntrySet) this.model.getConfigEntry();

        PowerMock.verifyAll();
        Assert.assertEquals(this.ce, configEntry);
        final ConfigItem item = (ConfigItem) configEntry.getByName("item");
        Assert.assertEquals("item", item.getName());
        Assert.assertEquals("string", item.getType());
        Assert.assertEquals("value", item.getValue());
    }

    @Test
    public void testSetRowsSpannedNeg() throws IOException {
        PowerMock.resetAll();

        PowerMock.replayAll();
        Assert.assertThrows(IllegalArgumentException.class,
                () -> this.model.setRowsSpanned(this.table, this.appender, 2, 2, -1));

        PowerMock.verifyAll();

    }

    @Test
    public void testSetRowsSpanned1() throws IOException {
        PowerMock.resetAll();

        PowerMock.replayAll();
        this.model.setRowsSpanned(this.table, this.appender, 2, 2, 1);

        PowerMock.verifyAll();

    }

    @Test
    public void testSetRowsSpannedCovered() throws IOException {
        PowerMock.resetAll();

        PowerMock.replayAll();
        this.model.setCellMerge(this.table, this.appender, 1, 1, 5, 5);
        assert this.model.getRow(this.table, this.appender, 2).getOrCreateCell(2).isCovered();
        Assert.assertThrows(IllegalArgumentException.class,
                () -> this.model.setRowsSpanned(this.table, this.appender, 2, 2, 2));

        PowerMock.verifyAll();

    }

    @Test
    public void testSetRowsSpanned() throws IOException {
        PowerMock.resetAll();

        PowerMock.replayAll();
        this.model.setRowsSpanned(this.table, this.appender, 2, 2, 2);

        PowerMock.verifyAll();

    }

    @Test
    public void testColumnStyle() {
        PowerMock.resetAll();

        PowerMock.replayAll();
        Assert.assertThrows(IllegalArgumentException.class,
                () -> this.model.setColumnStyle(-1, null));

        PowerMock.verifyAll();

    }

    @Test
    public void testNotify() throws IOException {
        final NamedOdsFileWriter o = PowerMock.createMock(NamedOdsFileWriter.class);

        PowerMock.resetAll();
        o.update(EasyMock.isA(BeginTableFlusher.class));
        o.update(EasyMock.isA(PreprocessedRowsFlusher.class));

        PowerMock.replayAll();
        this.model.addObserver(o);
        this.model.getRow(this.table, this.appender, 0);
        this.model.getRow(this.table, this.appender, 1024);

        PowerMock.verifyAll();
    }

    @Test
    public final void testFindDefaultCellStyle() {
        PowerMock.resetAll();
        PowerMock.replayAll();
        final TableCellStyle defaultCellStyle = this.model.findDefaultCellStyle(10);

        PowerMock.verifyAll();
        Assert.assertEquals(TableCellStyle.DEFAULT_CELL_STYLE,
                defaultCellStyle);
    }

    @Test
    public final void testFindDefaultCellStyleCustom() {
        final TableCellStyle cellStyle = TableCellStyle.builder("test").fontWeightBold().build();

        PowerMock.resetAll();
        EasyMock.expect(this.stc.addContentFontFaceContainerStyle(cellStyle)).andReturn(true);

        PowerMock.replayAll();
        this.model.setColumnDefaultCellStyle(1, cellStyle);
        final TableCellStyle defaultCellStyle = this.model.findDefaultCellStyle(1);

        PowerMock.verifyAll();
        Assert.assertEquals(cellStyle,
                defaultCellStyle);
    }

    @Test
    public final void testAddShape() {
        final Shape s =
                DrawFrame.builder("df", new DrawImage("href"), SVGRectangle.cm(1, 2, 3, 4)).build();

        PowerMock.resetAll();
        PowerMock.replayAll();
        this.model.addShape(s);

        PowerMock.verifyAll();
        Assert.assertEquals(Collections.singletonList(s), this.model.getShapes());
    }

    @Test
    public final void testSetColumnAttribute() throws IOException {
        PowerMock.resetAll();

        PowerMock.replayAll();
        this.model.setColumnAttribute(1, "attr", "value");
        final FastFullList<TableColumnImpl> columns = this.model.getColumns();

        PowerMock.verifyAll();
        final StringBuilder sb = new StringBuilder();
        final XMLUtil util = XMLUtil.create();
        Assert.assertNull(columns.get(0));
        columns.get(1).appendXMLToTable(util, sb, 1);
        DomTester.assertEquals(
                "<table:table-column " +
                        "table:style-name=\"co1\" table:default-cell-style-name=\"Default\" " +
                        "attr=\"value\"/>", sb.toString());
    }

    @Test
    public final void testSetAttribute() {
        PowerMock.resetAll();
        PowerMock.replayAll();
        this.model.setAttribute("attr", "value");

        PowerMock.verifyAll();
        Assert.assertEquals(ImmutableMap.of("attr", "value"),
                this.model.getCustomValueByAttribute());
    }

    @Test
    public final void testAddForm() {
        final Span form1 = new Span("a");
        final Span form2 = new Span("b");

        PowerMock.resetAll();
        PowerMock.replayAll();
        this.model.addForm(form1);
        this.model.addForm(form2);

        PowerMock.verifyAll();
        Assert.assertEquals(Arrays.asList(form1, form2), this.model.getForms());
    }

    @Test
    public final void testSetColumnStyle() {
        final TableCellStyle cellStyle = TableCellStyle.builder("test").fontWeightBold().build();

        PowerMock.resetAll();
        EasyMock.expect(this.stc.addContentFontFaceContainerStyle(cellStyle)).andReturn(true);

        PowerMock.replayAll();
        this.model.setColumnDefaultCellStyle(1, cellStyle);

        PowerMock.verifyAll();
        final FastFullList<TableColumnImpl> columns = this.model.getColumns();
        Assert.assertEquals(2, columns.usedSize());
        Assert.assertNull(columns.get(0));
        Assert.assertEquals(cellStyle, columns.get(1).getColumnDefaultCellStyle());
        Assert.assertNull(columns.get(2));
    }

    @Test
    public final void testAddPrintRange() {
        PowerMock.resetAll();

        PowerMock.replayAll();
        this.model.addPrintRange(1, 2, 3, 4);
        this.model.addPrintRange(5, 6, 7, 8);

        PowerMock.verifyAll();
        Assert.assertEquals(Arrays.asList("C2:E4", "G6:I8"), this.model.getPrintRanges());
    }

    @Test
    public final void testProtect() throws IOException {
        PowerMock.resetAll();

        PowerMock.replayAll();
        this.model.protect(new Protection("b", "c"));
        final Protection protection = this.model.getProtection();

        PowerMock.verifyAll();
        final StringBuilder sb = new StringBuilder();
        protection.appendAttributes(XMLUtil.create(), sb);
        Assert.assertEquals(
                " table:protected=\"true\" table:protection-key=\"b\" table:protection-key-digest-algorithm=\"c\"",
                sb.toString());
    }

    @Test
    public final void testHeader() throws IOException {
        PowerMock.resetAll();

        PowerMock.replayAll();
        this.model.setHeaderRowsCount(3);
        this.model.setHeaderColumnsCount(1);

        PowerMock.verifyAll();
        Assert.assertEquals(3, this.model.getHeaderRowsCount());
        Assert.assertEquals(1, this.model.getHeaderColumnsCount());
    }
}
