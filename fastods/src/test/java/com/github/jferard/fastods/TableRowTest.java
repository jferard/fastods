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

import com.github.jferard.fastods.datastyle.DataStyle;
import com.github.jferard.fastods.datastyle.DataStyles;
import com.github.jferard.fastods.datastyle.DataStylesBuilder;
import com.github.jferard.fastods.odselement.StylesContainer;
import com.github.jferard.fastods.odselement.StylesContainerImpl;
import com.github.jferard.fastods.style.TableCellStyle;
import com.github.jferard.fastods.style.TableRowStyle;
import com.github.jferard.fastods.testlib.DomTester;
import com.github.jferard.fastods.util.IntegerRepresentationCache;
import com.github.jferard.fastods.util.Validation;
import com.github.jferard.fastods.util.XMLUtil;
import org.easymock.EasyMock;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.easymock.PowerMock;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.io.IOException;
import java.util.Locale;

@RunWith(PowerMockRunner.class)
@PrepareForTest(TableColdCell.class)
public class TableRowTest {
    public static final long TIME_IN_MILLIS = 1234567891011L;
    private DataStyles ds;
    private TableRowImpl row;
    private StylesContainer stc;
    private Table table;
    private TableCellStyle tcs;
    private XMLUtil xmlUtil;
    private ValidationsContainer vc;

    @Before
    public void setUp() {
        this.stc = PowerMock.createMock(StylesContainerImpl.class);
        this.table = PowerMock.createMock(Table.class);
        final IntegerRepresentationCache cache = IntegerRepresentationCache.create();
        this.xmlUtil = XMLUtil.create();
        this.ds = DataStylesBuilder.create(Locale.US).build();
        this.vc = PowerMock.createMock(ValidationsContainer.class);
        this.row =
                new TableRowImpl(cache, this.xmlUtil, this.stc, this.ds, false, this.table, 10,
                        100, this.vc);
        this.tcs = TableCellStyle.builder("---").build();
        PowerMock.mockStatic(TableColdCell.class);
        PowerMock.resetAll();
    }

    @Test
    public void testSetStyle() {
        final TableRowStyle trs = TableRowStyle.DEFAULT_TABLE_ROW_STYLE;

        PowerMock.resetAll();
        EasyMock.expect(this.stc.addContentStyle(trs)).andReturn(true);

        PowerMock.replayAll();
        this.row.setRowStyle(trs);

        PowerMock.verifyAll();
    }

    @Test
    public void testSetDefaultCellStyle() {
        final TableCellStyle tcs = TableCellStyle.DEFAULT_CELL_STYLE;

        PowerMock.resetAll();
        EasyMock.expect(this.stc.addContentFontFaceContainerStyle(tcs)).andReturn(true);
        EasyMock.expect(this.stc.addContentStyle(tcs)).andReturn(true);

        PowerMock.replayAll();
        this.row.setRowDefaultCellStyle(tcs);

        PowerMock.verifyAll();
    }

    @Test
    public void testIsCovered() {
        PowerMock.resetAll();
        final boolean covered = this.row.isCovered(0);

        Assert.assertFalse(covered);
    }

    @Test
    public void testIsCoveredTrue() {
        PowerMock.resetAll();
        EasyMock.expect(TableColdCell.create(this.xmlUtil))
                .andReturn(new TableColdCell(this.xmlUtil));
        EasyMock.expect(TableColdCell.create(this.xmlUtil))
                .andReturn(new TableColdCell(this.xmlUtil));

        PowerMock.replayAll();
        this.row.setColumnsSpanned(0, 2);
        final boolean covered0 = this.row.isCovered(0);
        final boolean covered1 = this.row.isCovered(1);
        final boolean covered2 = this.row.isCovered(2);

        PowerMock.verifyAll();
        Assert.assertFalse(covered0);
        Assert.assertTrue(covered1);
        Assert.assertFalse(covered2);
    }

    @Test
    public void testColumnsSpannedSpanError() {
        PowerMock.resetAll();
        PowerMock.replayAll();

        Assert.assertThrows(IllegalArgumentException.class,
                () -> this.row.setColumnsSpanned(0, -1));

        PowerMock.verifyAll();
    }

    @Test
    public void testColumnsSpannedCellError() {
        PowerMock.resetAll();
        EasyMock.expect(TableColdCell.create(this.xmlUtil))
                .andReturn(new TableColdCell(this.xmlUtil));
        EasyMock.expect(TableColdCell.create(this.xmlUtil))
                .andReturn(new TableColdCell(this.xmlUtil));

        PowerMock.replayAll();
        this.row.setColumnsSpanned(0, 2);
        assert this.row.isCovered(1);
        Assert.assertThrows(IllegalArgumentException.class,
                () -> this.row.setColumnsSpanned(1, 10));

        PowerMock.verifyAll();
    }

    @Test
    public void testRowsSpannedSpanError() {
        PowerMock.resetAll();
        PowerMock.replayAll();

        Assert.assertThrows(IllegalArgumentException.class, () -> this.row.setRowsSpanned(0, -1));

        PowerMock.verifyAll();
    }

    @Test
    public void testRowsSpannedCellError() {
        PowerMock.resetAll();
        EasyMock.expect(TableColdCell.create(this.xmlUtil))
                .andReturn(new TableColdCell(this.xmlUtil));
        EasyMock.expect(TableColdCell.create(this.xmlUtil))
                .andReturn(new TableColdCell(this.xmlUtil));

        PowerMock.replayAll();
        this.row.setColumnsSpanned(0, 2);
        assert this.row.isCovered(1);
        Assert.assertThrows(IllegalArgumentException.class, () -> this.row.setRowsSpanned(1, 5));

        PowerMock.verifyAll();
    }

    @Test
    public void testCellMergeError() {
        PowerMock.resetAll();
        PowerMock.replayAll();
        Assert.assertThrows(IllegalArgumentException.class, () -> this.row.setCellMerge(0, -1, 2));

        PowerMock.verifyAll();
    }

    @Test
    public void testSetFormat() {
        PowerMock.resetAll();
        PowerMock.replayAll();
        this.row.setRowFormat(null);

        PowerMock.verifyAll();
    }

    @Test
    public final void testRows() throws IOException {
        final TableCellStyle cs = PowerMock.createMock(TableCellStyle.class);

        PowerMock.resetAll();
        EasyMock.expect(this.table.findDefaultCellStyle(5)).andReturn(cs);
        final DataStyle booleanDataStyle = this.ds.getBooleanDataStyle();
        EasyMock.expect(this.stc.addDataStyle(booleanDataStyle)).andReturn(true);
        EasyMock.expect(this.stc.addChildCellStyle(cs, booleanDataStyle)).andReturn(this.tcs);
        EasyMock.expect(cs.getDataStyle()).andReturn(null);

        PowerMock.replayAll();
        this.row.getOrCreateCell(5).setBooleanValue(true);
        this.row.getOrCreateCell(10).setStringValue("a");

        PowerMock.verifyAll();
        this.assertTableXMLEquals(
                "<table:table-row table:style-name=\"ro1\">" + "<table:table-cell " +
                        "table:number-columns-repeated=\"5\"/>" +
                        "<table:table-cell table:style-name=\"---\" " +
                        "office:value-type=\"boolean\" office:boolean-value=\"true\"/>" +
                        "<table:table-cell table:number-columns-repeated=\"4\"/>" +
                        "<table:table-cell office:value-type=\"string\" " +
                        "office:string-value=\"a\"/>" + "</table:table-row>");
    }

    @Test
    public final void testAppendRowOpenTag() throws IOException {
        PowerMock.resetAll();
        EasyMock.expect(
                        this.stc.addContentFontFaceContainerStyle(TableCellStyle.DEFAULT_CELL_STYLE))
                .andReturn(true);
        EasyMock.expect(
                        this.stc.addContentStyle(TableCellStyle.DEFAULT_CELL_STYLE))
                .andReturn(true);

        PowerMock.replayAll();
        this.row.removeRowStyle();
        this.row.setRowDefaultCellStyle(TableCellStyle.DEFAULT_CELL_STYLE);

        PowerMock.verifyAll();
        this.assertTableXMLEquals("<table:table-row table:default-cell-style-name=\"Default" +
                "\"><table:table-cell/></table:table-row>");
    }

    @Test
    public final void testGetWalker() throws IOException {
        PowerMock.resetAll();
        PowerMock.replayAll();
        final RowCellWalker walker = this.row.getWalker();
        walker.setFloatValue(1);

        PowerMock.verifyAll();
        this.assertTableXMLEquals("<table:table-row table:style-name=\"ro1\">" +
                "<table:table-cell office:value-type=\"float\" office:value=\"1\"/>" +
                "</table:table-row>");
    }

    @Test
    public final void testSetRowAttribute() throws IOException {
        PowerMock.resetAll();
        PowerMock.replayAll();
        this.row.setRowAttribute("attr", "value");

        PowerMock.verifyAll();
        this.assertTableXMLEquals("<table:table-row table:style-name=\"ro1\" attr=\"value\">" +
                "<table:table-cell/>" +
                "</table:table-row>");
    }

    @Test
    public final void testSetCustomCell() throws IOException {
        PowerMock.resetAll();
        PowerMock.replayAll();
        this.row.set(1, new AbstractTableCell() {
            @Override
            public void appendXMLToTableRow(final XMLUtil util, final Appendable appendable)
                    throws IOException {
                appendable.append("<mycell/>");
            }
        });

        PowerMock.verifyAll();
        this.assertTableXMLEquals("<table:table-row table:style-name=\"ro1\">" +
                "<table:table-cell/>" +
                "<mycell/>" +
                "</table:table-row>");
    }

    @Test
    public final void testRowIndex() {
        PowerMock.resetAll();
        PowerMock.replayAll();
        Assert.assertEquals(10, this.row.rowIndex());
        PowerMock.verifyAll();
    }

    @Test
    public final void testAddValidationToContainer() throws IOException {
        final Validation v = Validation.builder("v").dontAllowEmptyCells().build();

        PowerMock.resetAll();
        this.vc.addValidation(v);

        PowerMock.replayAll();
        this.row.addValidationToContainer(v);
        PowerMock.verifyAll();

        this.assertTableXMLEquals("<table:table-row table:style-name=\"ro1\">" +
                "<table:table-cell/>" +
                "</table:table-row>");
    }

    private void assertTableXMLEquals(final String xml) throws IOException {
        final StringBuilder sb = new StringBuilder();
        this.row.appendXMLToTable(this.xmlUtil, sb);
        DomTester.assertEquals(xml, sb.toString());
    }
}
