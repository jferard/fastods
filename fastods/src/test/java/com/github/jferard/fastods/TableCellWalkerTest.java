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

import com.github.jferard.fastods.attribute.SimpleLength;
import com.github.jferard.fastods.datastyle.BooleanStyle;
import com.github.jferard.fastods.datastyle.BooleanStyleBuilder;
import com.github.jferard.fastods.datastyle.DataStyles;
import com.github.jferard.fastods.style.TableCellStyle;
import com.github.jferard.fastods.style.TableColumnStyle;
import com.github.jferard.fastods.style.TableRowStyle;
import com.github.jferard.fastods.util.XMLUtil;
import org.easymock.EasyMock;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.powermock.api.easymock.PowerMock;

import java.io.IOException;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import static org.easymock.EasyMock.expect;

public class TableCellWalkerTest {
    private static final long TIME_IN_MILLIS = 1234567891011L;

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    private TableCellWalker cellWalker;
    private Table table;
    private XMLUtil util;
    private StringBuilder sb;
    private TableCell cell;
    private ToCellValueConverter converter;
    private TableRowImpl row;

    @Before
    public void setUp() {
        this.table = PowerMock.createMock(Table.class);

        this.converter = new ObjectToCellValueConverter("USD");
        this.cell = PowerMock.createMock(TableCell.class);
        this.row = PowerMock.createMock(TableRowImpl.class);
        this.util = XMLUtil.create();
        this.sb = new StringBuilder();
    }

    @Test
    public void testNextRow() throws IOException {
        PowerMock.resetAll();
        this.initWalker(0);
        EasyMock.expect(this.table.getRow(1)).andReturn(this.row);
        EasyMock.expect(this.row.getOrCreateCell(0)).andReturn(this.cell);

        PowerMock.replayAll();
        this.cellWalker = new TableCellWalker(this.table);
        this.cellWalker.nextRow();

        PowerMock.verifyAll();
    }

    @Test
    public void testToRow() throws IOException {
        PowerMock.resetAll();
        this.initWalker(0);
        EasyMock.expect(this.table.getRow(10)).andReturn(this.row);
        EasyMock.expect(this.row.getOrCreateCell(0)).andReturn(this.cell);

        PowerMock.replayAll();
        this.cellWalker = new TableCellWalker(this.table);
        this.cellWalker.toRow(10);

        PowerMock.verifyAll();
    }

    @Test
    public void testToRowFail() throws IOException {
        PowerMock.resetAll();
        this.initWalker(0);
        EasyMock.expect(this.table.getRow(10)).andReturn(this.row);
        EasyMock.expect(this.row.getOrCreateCell(0)).andReturn(this.cell);

        PowerMock.replayAll();
        this.cellWalker = new TableCellWalker(this.table);
        this.thrown.expect(IndexOutOfBoundsException.class);
        this.cellWalker.toRow(-10);

        PowerMock.verifyAll();
    }

    @Test
    public void testPreviousRowFail() throws IOException {
        PowerMock.resetAll();
        this.initWalker(0);
        EasyMock.expect(this.table.getRow(10)).andReturn(this.row);
        EasyMock.expect(this.row.getOrCreateCell(0)).andReturn(this.cell);

        PowerMock.replayAll();
        this.cellWalker = new TableCellWalker(this.table);

        this.thrown.expect(IndexOutOfBoundsException.class);
        this.cellWalker.previousRow();

        PowerMock.verifyAll();
    }

    @Test
    public void testPreviousRow() throws IOException {
        PowerMock.resetAll();
        this.initWalker(0);
        EasyMock.expect(this.table.getRow(10)).andReturn(this.row);
        EasyMock.expect(this.row.getOrCreateCell(0)).andReturn(this.cell);
        EasyMock.expect(this.table.getRow(9)).andReturn(this.row);
        EasyMock.expect(this.row.getOrCreateCell(0)).andReturn(this.cell);

        PowerMock.replayAll();
        this.cellWalker = new TableCellWalker(this.table);
        this.cellWalker.toRow(10);
        this.cellWalker.previousRow();

        PowerMock.verifyAll();
    }

    @Test
    public void lastRow() throws IOException {
        PowerMock.resetAll();
        this.initWalker(0);
        EasyMock.expect(this.table.getRowCount()).andReturn(7);
        EasyMock.expect(this.table.getRow(6)).andReturn(this.row);
        EasyMock.expect(this.row.getOrCreateCell(0)).andReturn(this.cell);

        PowerMock.replayAll();
        this.cellWalker = new TableCellWalker(this.table);
        this.cellWalker.lastRow();

        PowerMock.verifyAll();
    }

    @Test
    public void last() throws IOException {
        PowerMock.resetAll();
        this.initWalker(0);
        EasyMock.expect(this.row.getColumnCount()).andReturn(6);
        EasyMock.expect(this.row.getOrCreateCell(5)).andReturn(this.cell);

        PowerMock.replayAll();
        this.cellWalker = new TableCellWalker(this.table);
        this.cellWalker.last();

        PowerMock.verifyAll();
    }

    @Test
    public final void testMarkRowsSpanned() throws IOException {
        PowerMock.resetAll();
        this.initWalker(0);
        this.cell.markRowsSpanned(5);

        PowerMock.replayAll();
        this.cellWalker = new TableCellWalker(this.table);
        this.cellWalker.markRowsSpanned(5);

        PowerMock.verifyAll();
    }

    @Test
    public final void testMarkColumnsSpanned() throws IOException {
        PowerMock.resetAll();
        this.initWalker(0);
        this.cell.markColumnsSpanned(5);

        PowerMock.replayAll();
        this.cellWalker = new TableCellWalker(this.table);
        this.cellWalker.markColumnsSpanned(5);

        PowerMock.verifyAll();
    }

    @Test
    public final void testBoolean() throws IOException {
        PowerMock.resetAll();
        this.to(0, 8);
        this.cell.setBooleanValue(true);
        expect(this.row.getOrCreateCell(9)).andReturn(this.cell);

        PowerMock.replayAll();
        this.cellWalker = new TableCellWalker(this.table);
        this.cellWalker.to(8);
        this.cellWalker.setBooleanValue(true);
        this.cellWalker.next();

        PowerMock.verifyAll();
    }

    @Test
    public final void testText() throws IOException {
        final Text t = Text.content("a");

        PowerMock.resetAll();
        this.to(0, 8);
        this.cell.setText(t);
        expect(this.row.getOrCreateCell(9)).andReturn(this.cell);

        PowerMock.replayAll();
        this.cellWalker = new TableCellWalker(this.table);
        this.cellWalker.to(8);
        this.cellWalker.setText(t);
        this.cellWalker.next();

        PowerMock.verifyAll();
    }

    @Test
    public final void testCellMerge() throws IOException {
        PowerMock.resetAll();
        this.to(0, 10);
        this.table.setCellMerge(0, 10, 5, 6);

        PowerMock.replayAll();
        this.cellWalker = new TableCellWalker(this.table);
        this.cellWalker.to(10);
        this.cellWalker.setCellMerge(5, 6);

        PowerMock.verifyAll();
    }

    @Test
    public final void testCellMergeErrorNegCol() throws IOException {
        PowerMock.resetAll();
        this.initWalker(0);

        PowerMock.replayAll();
        this.cellWalker = new TableCellWalker(this.table);

        this.thrown.expect(IllegalArgumentException.class);
        this.cellWalker.setCellMerge(0, -1);

        PowerMock.verifyAll();
    }

    @Test
    public final void testCellMergeErrorNegRow() throws IOException {
        PowerMock.resetAll();
        this.initWalker(0);

        PowerMock.replayAll();
        this.cellWalker = new TableCellWalker(this.table);

        this.thrown.expect(IllegalArgumentException.class);
        this.cellWalker.setCellMerge(-1, 0);

        PowerMock.verifyAll();
    }

    @Test
    public final void testCellMergePass() throws IOException {
        PowerMock.resetAll();
        this.initWalker(0);

        PowerMock.replayAll();
        this.cellWalker = new TableCellWalker(this.table);
        this.cellWalker.setCellMerge(1, 1);

        PowerMock.verifyAll();
    }

    @Test
    public final void testMatrixFormula() throws IOException {
        PowerMock.resetAll();
        this.initWalker(0);
        this.cell.setMatrixFormula("f");

        PowerMock.replayAll();
        this.cellWalker = new TableCellWalker(this.table);
        this.cellWalker.setMatrixFormula("f");

        PowerMock.verifyAll();
    }

    @Test
    public final void testMatrixFormulaFull() throws IOException {
        PowerMock.resetAll();
        this.initWalker(0);
        this.cell.setMatrixFormula("f", 5, 4);

        PowerMock.replayAll();
        this.cellWalker = new TableCellWalker(this.table);
        this.cellWalker.setMatrixFormula("f", 5, 4);

        PowerMock.verifyAll();
    }

    @Test
    public final void testStringValue() throws IOException {
        PowerMock.resetAll();
        this.initWalker(0);
        this.cell.setStringValue("S");

        PowerMock.replayAll();
        this.cellWalker = new TableCellWalker(this.table);
        this.cellWalker.setStringValue("S");

        PowerMock.verifyAll();
    }

    @Test
    public final void testColumnsSpanned() throws IOException {
        PowerMock.resetAll();
        this.to(0, 10);
        this.row.setColumnsSpanned(10, 8);

        PowerMock.replayAll();
        this.cellWalker = new TableCellWalker(this.table);
        this.cellWalker.to(10);
        this.cellWalker.setColumnsSpanned(8);

        PowerMock.verifyAll();
    }

    @Test
    public final void testRowsSpanned() throws IOException {
        PowerMock.resetAll();
        this.to(0, 10);
        this.row.setRowsSpanned(10, 8);

        PowerMock.replayAll();
        this.cellWalker = new TableCellWalker(this.table);
        this.cellWalker.to(10);
        this.cellWalker.setRowsSpanned(8);

        PowerMock.verifyAll();
    }

    @Test
    public final void testVoidValue() throws IOException {
        PowerMock.resetAll();
        this.to(0, 10);
        this.cell.setVoidValue();

        PowerMock.replayAll();
        this.cellWalker = new TableCellWalker(this.table);
        this.cellWalker.to(10);
        this.cellWalker.setVoidValue();

        PowerMock.verifyAll();
    }

    @Test
    public final void testTimeValue() throws IOException {
        PowerMock.resetAll();
        this.to(0, 10);
        this.cell.setTimeValue(1000);

        PowerMock.replayAll();
        this.cellWalker = new TableCellWalker(this.table);
        this.cellWalker.to(10);
        this.cellWalker.setTimeValue(1000);

        PowerMock.verifyAll();
    }

    @Test
    public final void testTooltip() throws IOException {
        PowerMock.resetAll();
        this.to(0, 10);
        this.cell.setTooltip("1000");

        PowerMock.replayAll();
        this.cellWalker = new TableCellWalker(this.table);
        this.cellWalker.to(10);
        this.cellWalker.setTooltip("1000");

        PowerMock.verifyAll();
    }

    @Test
    public final void testFormula() throws IOException {
        PowerMock.resetAll();
        this.to(0, 10);
        this.cell.setFormula("1000");

        PowerMock.replayAll();
        this.cellWalker = new TableCellWalker(this.table);
        this.cellWalker.to(10);
        this.cellWalker.setFormula("1000");

        PowerMock.verifyAll();
    }

    @Test
    public final void testIsCovered() throws IOException {
        PowerMock.resetAll();
        this.to(0, 10);
        expect(this.cell.isCovered()).andReturn(true);

        PowerMock.replayAll();
        this.cellWalker = new TableCellWalker(this.table);
        this.cellWalker.to(10);
        final boolean covered = this.cellWalker.isCovered();

        PowerMock.verifyAll();
        Assert.assertTrue(covered);
    }

    @Test
    public final void testSetCovered() throws IOException {
        PowerMock.resetAll();
        this.to(0, 10);
        this.cell.setCovered();

        PowerMock.replayAll();
        this.cellWalker = new TableCellWalker(this.table);
        this.cellWalker.to(10);
        this.cellWalker.setCovered();

        PowerMock.verifyAll();
    }

    @Test
    public final void testNext() throws IOException {
        PowerMock.resetAll();
        this.to(0, 10);
        EasyMock.expect(this.row.getOrCreateCell(11)).andReturn(this.cell);

        PowerMock.replayAll();
        this.cellWalker = new TableCellWalker(this.table);
        this.cellWalker.to(10);
        this.cellWalker.next();

        PowerMock.verifyAll();
    }

    @Test
    public final void testPrevious() throws IOException {
        PowerMock.resetAll();
        this.to(0, 5);
        expect(this.row.getOrCreateCell(4)).andReturn(this.cell);
        this.cell.setBooleanValue(true);

        PowerMock.replayAll();
        this.cellWalker = new TableCellWalker(this.table);
        this.cellWalker.to(5);
        this.cellWalker.previous();
        this.cellWalker.setBooleanValue(true);

        PowerMock.verifyAll();
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public final void testPreviousIOOBE() throws IOException {
        PowerMock.resetAll();
        this.initWalker(0);

        PowerMock.replayAll();
        this.cellWalker = new TableCellWalker(this.table);
        this.cellWalker.previous();

        PowerMock.verifyAll();
    }

    @Test
    public final void testCalendar() throws IOException {
        final Calendar c = Calendar.getInstance();
        c.setTimeInMillis(TIME_IN_MILLIS);

        PowerMock.resetAll();
        this.to(0, 10);
        this.cell.setDateValue(c);

        PowerMock.replayAll();
        this.cellWalker = new TableCellWalker(this.table);
        this.cellWalker.to(10);
        this.cellWalker.setDateValue(c);

        PowerMock.verifyAll();
    }

    @Test
    public final void testCurrencyFloat() throws IOException {
        PowerMock.resetAll();
        this.to(0, 10);
        this.cell.setCurrencyValue(10.0f, "€");

        PowerMock.replayAll();
        this.cellWalker = new TableCellWalker(this.table);
        this.cellWalker.to(10);
        this.cellWalker.setCurrencyValue(10.0f, "€");

        PowerMock.verifyAll();
    }

    @Test
    public final void testCurrencyInt() throws IOException {
        PowerMock.resetAll();
        this.to(0, 10);
        this.cell.setCurrencyValue(9, "€");

        PowerMock.replayAll();
        this.cellWalker = new TableCellWalker(this.table);
        this.cellWalker.to(10);
        this.cellWalker.setCurrencyValue(9, "€");

        PowerMock.verifyAll();
    }

    @Test
    public final void testCurrencyNumber() throws IOException {
        PowerMock.resetAll();
        this.to(0, 10);
        this.cell.setCurrencyValue(10.0, "€");

        PowerMock.replayAll();
        this.cellWalker = new TableCellWalker(this.table);
        this.cellWalker.to(10);
        this.cellWalker.setCurrencyValue(10.0, "€");

        PowerMock.verifyAll();
    }

    @Test
    public final void testDate() throws IOException {
        final Calendar c = Calendar.getInstance();
        c.setTimeInMillis(TIME_IN_MILLIS);
        final Date date = c.getTime();

        PowerMock.resetAll();
        this.to(0, 10);
        this.cell.setDateValue(date);

        PowerMock.replayAll();
        this.cellWalker = new TableCellWalker(this.table);
        this.cellWalker.to(10);
        this.cellWalker.setDateValue(date);

        PowerMock.verifyAll();
    }

    @Test
    public final void testDouble() throws IOException {
        PowerMock.resetAll();
        this.to(0, 10);
        this.cell.setFloatValue(10.999);

        PowerMock.replayAll();
        this.cellWalker = new TableCellWalker(this.table);
        this.cellWalker.to(10);
        this.cellWalker.setFloatValue(10.999);

        PowerMock.verifyAll();
    }

    @Test
    public final void testFloat() throws IOException {
        PowerMock.resetAll();
        this.to(0, 10);
        this.cell.setFloatValue(9.999f);

        PowerMock.replayAll();
        this.cellWalker = new TableCellWalker(this.table);
        this.cellWalker.to(10);
        this.cellWalker.setFloatValue(9.999f);

        PowerMock.verifyAll();
    }

    @Test
    public final void testInt() throws IOException {
        PowerMock.resetAll();
        this.to(0, 10);
        this.cell.setFloatValue(999);

        PowerMock.replayAll();
        this.cellWalker = new TableCellWalker(this.table);
        this.cellWalker.to(10);
        this.cellWalker.setFloatValue(999);

        PowerMock.verifyAll();
    }

    @Test
    public final void testObject() throws IOException {
        final CellValue value = this.converter.from(null);

        PowerMock.resetAll();
        this.to(0, 10);
        this.cell.setCellValue(value);

        PowerMock.replayAll();
        this.cellWalker = new TableCellWalker(this.table);
        this.cellWalker.to(10);
        this.cellWalker.setCellValue(value);

        PowerMock.verifyAll();
    }

    @Test
    public final void testPercentageFloat() throws IOException {
        PowerMock.resetAll();
        this.to(0, 10);
        this.cell.setPercentageValue(0.98f);

        PowerMock.replayAll();
        this.cellWalker = new TableCellWalker(this.table);
        this.cellWalker.to(10);
        this.cellWalker.setPercentageValue(0.98f);

        PowerMock.verifyAll();
    }

    @Test
    public final void testPercentageNumber() throws IOException {
        PowerMock.resetAll();
        this.to(0, 10);
        this.cell.setPercentageValue(0.98);

        PowerMock.replayAll();
        this.cellWalker = new TableCellWalker(this.table);
        this.cellWalker.to(10);
        this.cellWalker.setPercentageValue(0.98);

        PowerMock.verifyAll();
    }

    @Test
    public final void testPercentageInt() throws IOException {
        PowerMock.resetAll();
        this.to(0, 10);
        this.cell.setPercentageValue(98);

        PowerMock.replayAll();
        this.cellWalker = new TableCellWalker(this.table);
        this.cellWalker.to(10);
        this.cellWalker.setPercentageValue(98);

        PowerMock.verifyAll();
    }

    @Test
    public final void testSetTimeValueMillis() throws IOException {
        PowerMock.resetAll();
        this.initWalker(0);
        this.cell.setTimeValue(TIME_IN_MILLIS);

        PowerMock.replayAll();
        this.cellWalker = new TableCellWalker(this.table);
        this.cellWalker.setTimeValue(TIME_IN_MILLIS);

        PowerMock.verifyAll();
    }

    @Test
    public final void testSetTimeValue() throws IOException {
        PowerMock.resetAll();
        this.initWalker(0);
        this.cell.setTimeValue(1, 2, 3, 4, 5, 6);

        PowerMock.replayAll();
        this.cellWalker = new TableCellWalker(this.table);
        this.cellWalker.setTimeValue(1, 2, 3, 4, 5, 6);

        PowerMock.verifyAll();
    }

    @Test
    public final void testSetNegTimeValue() throws IOException {
        PowerMock.resetAll();
        this.initWalker(0);
        this.cell.setNegTimeValue(1, 2, 3, 4, 5, 6);

        PowerMock.replayAll();
        this.cellWalker = new TableCellWalker(this.table);
        this.cellWalker.setNegTimeValue(1, 2, 3, 4, 5, 6);

        PowerMock.verifyAll();
    }

    @Test
    public final void testSetTooltipLong() throws IOException {
        PowerMock.resetAll();
        this.initWalker(0);
        this.cell.setTooltip("tt", SimpleLength.cm(3), SimpleLength.cm(4), true);

        PowerMock.replayAll();
        this.cellWalker = new TableCellWalker(this.table);
        this.cellWalker.setTooltip("tt", SimpleLength.cm(3), SimpleLength.cm(4), true);

        PowerMock.verifyAll();
    }

    @Test
    public final void testDataStyle() throws IOException {
        final BooleanStyle bs = new BooleanStyleBuilder("bs", Locale.US).build();

        PowerMock.resetAll();
        this.initWalker(0);
        this.cell.setDataStyle(bs);

        PowerMock.replayAll();
        this.cellWalker = new TableCellWalker(this.table);
        this.cellWalker.setDataStyle(bs);

        PowerMock.verifyAll();
    }

    @Test
    public final void testHasValue() throws IOException {
        PowerMock.resetAll();
        this.initWalker(0);
        EasyMock.expect(this.cell.hasValue()).andReturn(true);

        PowerMock.replayAll();
        this.cellWalker = new TableCellWalker(this.table);
        final boolean ret = this.cellWalker.hasValue();

        PowerMock.verifyAll();
        Assert.assertTrue(ret);
    }

    @Test
    public void testSetColumnStyle() throws IOException {
        final TableColumnStyle tcs = PowerMock.createMock(TableColumnStyle.class);

        PowerMock.resetAll();
        this.initWalker(0);
        EasyMock.expect(this.row.getOrCreateCell(1)).andReturn(this.cell);
        this.table.setColumnStyle(1, tcs);

        PowerMock.replayAll();
        this.cellWalker = new TableCellWalker(this.table);
        this.cellWalker.next();
        this.cellWalker.setColumnStyle(tcs);

        PowerMock.verifyAll();
    }

    @Test
    public void testSetFormat() throws IOException {
        final DataStyles rf = PowerMock.createMock(DataStyles.class);

        PowerMock.resetAll();
        this.initWalker(0);
        this.row.setRowFormat(rf);

        PowerMock.replayAll();
        this.cellWalker = new TableCellWalker(this.table);
        this.cellWalker.setRowFormat(rf);

        PowerMock.verifyAll();
    }

    @Test
    public void testSetRowStyle() throws IOException {
        final TableRowStyle trs = PowerMock.createMock(TableRowStyle.class);

        PowerMock.resetAll();
        this.initWalker(0);
        this.row.setRowStyle(trs);

        PowerMock.replayAll();
        this.cellWalker = new TableCellWalker(this.table);
        this.cellWalker.setRowStyle(trs);

        PowerMock.verifyAll();
    }

    @Test
    public void testGetColumnCount() throws IOException {
        PowerMock.resetAll();
        this.initWalker(0);
        EasyMock.expect(this.row.getColumnCount()).andReturn(2);

        PowerMock.replayAll();
        this.cellWalker = new TableCellWalker(this.table);
        final int c = this.cellWalker.getColumnCount();

        PowerMock.verifyAll();
        Assert.assertEquals(2, c);
    }

    @Test
    public void testSetDefaultCellStyle() throws IOException {
        final TableCellStyle tcs = PowerMock.createMock(TableCellStyle.class);

        PowerMock.resetAll();
        this.initWalker(0);
        this.row.setRowDefaultCellStyle(tcs);

        PowerMock.replayAll();
        this.cellWalker = new TableCellWalker(this.table);
        this.cellWalker.setRowDefaultCellStyle(tcs);

        PowerMock.verifyAll();
    }

    @Test
    public void testRowIndex() throws IOException {
        PowerMock.resetAll();
        this.initWalker(0);
        EasyMock.expect(this.table.getRow(1)).andReturn(this.row);
        EasyMock.expect(this.row.getOrCreateCell(0)).andReturn(this.cell);

        PowerMock.replayAll();
        this.cellWalker = new TableCellWalker(this.table);
        this.cellWalker.nextRow();
        final int r = this.cellWalker.rowIndex();

        PowerMock.verifyAll();
        Assert.assertEquals(1, r);
    }

    @Test
    public void testColIndex() throws IOException {
        PowerMock.resetAll();
        this.initWalker(0);

        PowerMock.replayAll();
        this.cellWalker = new TableCellWalker(this.table);
        this.cellWalker.colIndex();

        PowerMock.verifyAll();
    }

    @Test
    public void testRemoveRowStyle() throws IOException {
        PowerMock.resetAll();
        this.initWalker(0);
        this.row.removeRowStyle();

        PowerMock.replayAll();
        this.cellWalker = new TableCellWalker(this.table);
        this.cellWalker.removeRowStyle();

        PowerMock.verifyAll();
    }

    @Test
    public void testHasNext() throws IOException {
        PowerMock.resetAll();
        this.initWalker(0);
        EasyMock.expect(this.row.getColumnCount()).andReturn(5);

        PowerMock.replayAll();
        this.cellWalker = new TableCellWalker(this.table);
        this.cellWalker.hasNext();

        PowerMock.verifyAll();
    }

    @Test
    public void testHasPrevious() throws IOException {
        PowerMock.resetAll();
        this.initWalker(0);

        PowerMock.replayAll();
        this.cellWalker = new TableCellWalker(this.table);
        this.cellWalker.hasPrevious();

        PowerMock.verifyAll();
    }

    @Test
    public void testHasNextRow() throws IOException {
        PowerMock.resetAll();
        this.initWalker(0);
        EasyMock.expect(this.table.getRowCount()).andReturn(5);

        PowerMock.replayAll();
        this.cellWalker = new TableCellWalker(this.table);
        final boolean b = this.cellWalker.hasNextRow();

        PowerMock.verifyAll();
        Assert.assertTrue(b);
    }

    @Test
    public void testHasPreviousRow() throws IOException {
        PowerMock.resetAll();
        this.initWalker(0);

        PowerMock.replayAll();
        this.cellWalker = new TableCellWalker(this.table);
        final boolean b = this.cellWalker.hasPreviousRow();

        PowerMock.verifyAll();
        Assert.assertFalse(b);
    }

    @Test
    public void testSetStyle() throws IOException {
        PowerMock.resetAll();
        this.initWalker(0);
        this.cell.setStyle(TableCellStyle.DEFAULT_CELL_STYLE);

        PowerMock.replayAll();
        this.cellWalker = new TableCellWalker(this.table);
        this.cellWalker.setStyle(TableCellStyle.DEFAULT_CELL_STYLE);

        PowerMock.verifyAll();
    }

    @Test
    public void testAddData() throws IOException {
        final DataWrapper dw = PowerMock.createMock(DataWrapper.class);

        PowerMock.resetAll();
        this.initWalker(0);
        EasyMock.expect(dw.addToTable(EasyMock.isA(TableCellWalker.class))).andReturn(true);

        PowerMock.replayAll();
        this.cellWalker = new TableCellWalker(this.table);
        this.cellWalker.addData(dw);

        PowerMock.verifyAll();
    }

    private void to(final int r, final int c) throws IOException {
        this.initWalker(r);
        expect(this.row.getOrCreateCell(c)).andReturn(this.cell);
    }

    private void initWalker(final int r) throws IOException {
        expect(this.table.getRow(r)).andReturn(this.row);
        expect(this.row.getOrCreateCell(0)).andReturn(this.cell);
    }

}
