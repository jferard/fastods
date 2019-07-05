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
import com.github.jferard.fastods.datastyle.BooleanStyleBuilder;
import com.github.jferard.fastods.datastyle.DataStyles;
import com.github.jferard.fastods.datastyle.DataStylesBuilder;
import com.github.jferard.fastods.odselement.StylesContainer;
import com.github.jferard.fastods.util.SimpleLength;
import com.github.jferard.fastods.util.WriteUtil;
import com.github.jferard.fastods.util.XMLUtil;
import org.easymock.EasyMock;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.powermock.api.easymock.PowerMock;

import java.io.IOException;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import static org.easymock.EasyMock.expect;

public class TableCellWalkerImplTest {
    private static final long TIME_IN_MILLIS = 1234567891011L;
    private TableCellWalkerImpl cellWalker;
    private TableRow row;
    private XMLUtil util;
    private StringBuilder sb;
    private TableCell cell;
    private ToCellValueConverter converter;

    @Before
    public void setUp() {
        this.converter = new ObjectToCellValueConverter("USD");
        this.row = PowerMock.createMock(TableRow.class);
        this.cell = PowerMock.createMock(TableCell.class);
        this.cellWalker = new TableCellWalkerImpl(this.row);
        this.util = XMLUtil.create();
        this.sb = new StringBuilder();
    }

    @Test(expected = UnsupportedOperationException.class)
    public final void testAppendXML() {
        PowerMock.resetAll();

        PowerMock.replayAll();
        this.cellWalker.appendXMLToTableRow(this.util, this.sb);

        PowerMock.verifyAll();
    }

    @Test(expected = UnsupportedOperationException.class)
    public final void testMarkRowsSpanned() {
        PowerMock.resetAll();

        PowerMock.replayAll();
        this.cellWalker.markRowsSpanned(5);

        PowerMock.verifyAll();
    }

    @Test(expected = UnsupportedOperationException.class)
    public final void testMarkColumnsSpanned() {
        PowerMock.resetAll();

        PowerMock.replayAll();
        this.cellWalker.markColumnsSpanned(5);

        PowerMock.verifyAll();
    }

    @Test
    public final void testBoolean() {
        PowerMock.resetAll();
        expect(this.row.getOrCreateCell(8)).andReturn(this.cell);
        this.cell.setBooleanValue(true);
        expect(this.row.getColumnCount()).andReturn(10);

        PowerMock.replayAll();
        this.cellWalker.to(8);
        this.cellWalker.setBooleanValue(true);
        this.cellWalker.next();

        PowerMock.verifyAll();
    }

    @Test
    public final void testText() {
        final Text t = Text.content("a");

        PowerMock.resetAll();
        expect(this.row.getOrCreateCell(8)).andReturn(this.cell);
        this.cell.setText(t);
        expect(this.row.getColumnCount()).andReturn(10);

        PowerMock.replayAll();
        this.cellWalker.to(8);
        this.cellWalker.setText(t);
        this.cellWalker.next();

        PowerMock.verifyAll();
    }

    @Test
    public final void testCellMerge() throws IOException {
        PowerMock.resetAll();
        this.row.setCellMerge(10, 5, 6);

        PowerMock.replayAll();
        this.cellWalker.to(10);
        this.cellWalker.setCellMerge(5, 6);

        PowerMock.verifyAll();
    }

    @Test
    public final void testColumnsSpanned() {
        PowerMock.resetAll();
        this.row.setColumnsSpanned(10, 8);

        PowerMock.replayAll();
        this.cellWalker.to(10);
        this.cellWalker.setColumnsSpanned(8);

        PowerMock.verifyAll();
    }

    @Test
    public final void testRowsSpanned() throws IOException {
        PowerMock.resetAll();
        this.row.setRowsSpanned(10, 8);

        PowerMock.replayAll();
        this.cellWalker.to(10);
        this.cellWalker.setRowsSpanned(8);

        PowerMock.verifyAll();
    }

    @Test
    public final void testVoidValue() {
        PowerMock.resetAll();
        expect(this.row.getOrCreateCell(10)).andReturn(this.cell);
        this.cell.setVoidValue();

        PowerMock.replayAll();
        this.cellWalker.to(10);
        this.cellWalker.setVoidValue();

        PowerMock.verifyAll();
    }

    @Test
    public final void testTimeValue() {
        PowerMock.resetAll();
        expect(this.row.getOrCreateCell(10)).andReturn(this.cell);
        this.cell.setTimeValue(1000);

        PowerMock.replayAll();
        this.cellWalker.to(10);
        this.cellWalker.setTimeValue(1000);

        PowerMock.verifyAll();
    }

    @Test
    public final void testTooltip() {
        PowerMock.resetAll();
        expect(this.row.getOrCreateCell(10)).andReturn(this.cell);
        this.cell.setTooltip("1000");

        PowerMock.replayAll();
        this.cellWalker.to(10);
        this.cellWalker.setTooltip("1000");

        PowerMock.verifyAll();
    }

    @Test
    public final void testFormula() {
        PowerMock.resetAll();
        expect(this.row.getOrCreateCell(10)).andReturn(this.cell);
        this.cell.setFormula("1000");

        PowerMock.replayAll();
        this.cellWalker.to(10);
        this.cellWalker.setFormula("1000");

        PowerMock.verifyAll();
    }

    @Test
    public final void testIsCovered() {
        PowerMock.resetAll();
        expect(this.row.getOrCreateCell(10)).andReturn(this.cell);
        expect(this.cell.isCovered()).andReturn(true);

        PowerMock.replayAll();
        this.cellWalker.to(10);
        Assert.assertTrue(this.cellWalker.isCovered());

        PowerMock.verifyAll();
    }

    @Test
    public final void testSetCovered() {
        PowerMock.resetAll();
        expect(this.row.getOrCreateCell(10)).andReturn(this.cell);
        this.cell.setCovered();

        PowerMock.replayAll();
        this.cellWalker.to(10);
        this.cellWalker.setCovered();

        PowerMock.verifyAll();
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public final void testNext() {
        PowerMock.resetAll();
        expect(this.row.getColumnCount()).andReturn(10);

        PowerMock.replayAll();
        this.cellWalker.to(10);
        this.cellWalker.next();

        PowerMock.verifyAll();
    }

    @Test
    public final void testPrevious() {
        PowerMock.resetAll();
        expect(this.row.getOrCreateCell(4)).andReturn(this.cell);
        this.cell.setBooleanValue(true);

        PowerMock.replayAll();
        this.cellWalker.to(5);
        this.cellWalker.previous();
        this.cellWalker.setBooleanValue(true);

        PowerMock.verifyAll();
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public final void testPreviousIOOBE() {
        PowerMock.resetAll();

        PowerMock.replayAll();
        this.cellWalker.previous();

        PowerMock.verifyAll();
    }

    @Test
    public final void testCalendar() {
        final Calendar c = Calendar.getInstance();
        c.setTimeInMillis(TIME_IN_MILLIS);

        PowerMock.resetAll();
        expect(this.row.getOrCreateCell(10)).andReturn(this.cell);
        this.cell.setDateValue(c);

        PowerMock.replayAll();
        this.cellWalker.to(10);
        this.cellWalker.setDateValue(c);

        PowerMock.verifyAll();
    }

    @Test
    public final void testCurrencyFloat() {
        PowerMock.resetAll();
        expect(this.row.getOrCreateCell(10)).andReturn(this.cell);
        this.cell.setCurrencyValue(10.0f, "€");

        PowerMock.replayAll();
        this.cellWalker.to(10);
        this.cellWalker.setCurrencyValue(10.0f, "€");

        PowerMock.verifyAll();
    }

    @Test
    public final void testCurrencyInt() {
        PowerMock.resetAll();
        expect(this.row.getOrCreateCell(10)).andReturn(this.cell);
        this.cell.setCurrencyValue(9, "€");

        PowerMock.replayAll();
        this.cellWalker.to(10);
        this.cellWalker.setCurrencyValue(9, "€");

        PowerMock.verifyAll();
    }

    @Test
    public final void testCurrencyNumber() {
        PowerMock.resetAll();
        expect(this.row.getOrCreateCell(10)).andReturn(this.cell);
        this.cell.setCurrencyValue(10.0, "€");

        PowerMock.replayAll();
        this.cellWalker.to(10);
        this.cellWalker.setCurrencyValue(10.0, "€");

        PowerMock.verifyAll();
    }

    @Test
    public final void testDate() {
        final Calendar c = Calendar.getInstance();
        c.setTimeInMillis(TIME_IN_MILLIS);
        final Date date = c.getTime();

        PowerMock.resetAll();
        expect(this.row.getOrCreateCell(10)).andReturn(this.cell);
        this.cell.setDateValue(date);

        PowerMock.replayAll();
        this.cellWalker.to(10);
        this.cellWalker.setDateValue(date);

        PowerMock.verifyAll();
    }

    @Test
    public final void testDouble() {
        PowerMock.resetAll();
        expect(this.row.getOrCreateCell(10)).andReturn(this.cell);
        this.cell.setFloatValue(10.999);

        PowerMock.replayAll();
        this.cellWalker.to(10);
        this.cellWalker.setFloatValue(10.999);

        PowerMock.verifyAll();
    }

    @Test
    public final void testFloat() {
        PowerMock.resetAll();
        expect(this.row.getOrCreateCell(10)).andReturn(this.cell);
        this.cell.setFloatValue(9.999f);

        PowerMock.replayAll();
        this.cellWalker.to(10);
        this.cellWalker.setFloatValue(9.999f);

        PowerMock.verifyAll();
    }

    @Test
    public final void testInt() {
        expect(this.row.getOrCreateCell(10)).andReturn(this.cell);
        this.cell.setFloatValue(999);
        PowerMock.replayAll();
        this.cellWalker.to(10);
        this.cellWalker.setFloatValue(999);
        PowerMock.verifyAll();
    }

    @Test
    public final void testMove() {
        final TableRow row = this.initRealRow();
        final TableCellWalkerImpl cell = new TableCellWalkerImpl(row);
        PowerMock.replayAll();
        cell.to(49);
        cell.setStringValue("s");
        cell.to(0);
        Assert.assertTrue(cell.hasNext());
        Assert.assertFalse(cell.hasPrevious());
        cell.next();
        cell.lastCell();
        cell.to(49);
        Assert.assertTrue(cell.hasNext());
        cell.next();
        Assert.assertFalse(cell.hasNext());
        cell.to(100);
        Assert.assertFalse(cell.hasPrevious());
        cell.to(50);
        Assert.assertTrue(cell.hasPrevious());
        cell.to(51);
        Assert.assertFalse(cell.hasPrevious());
        PowerMock.verifyAll();
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public final void testMoveNegative() {
        PowerMock.resetAll();
        PowerMock.replayAll();

        final TableRow row = this.initRealRow();
        final TableCellWalker cell = new TableCellWalkerImpl(row);
        cell.to(-1);

        PowerMock.verifyAll();
    }

    @Test
    public final void testObject() {
        final CellValue value = this.converter.from(null);

        expect(this.row.getOrCreateCell(10)).andReturn(this.cell);
        this.cell.setCellValue(value);
        PowerMock.replayAll();
        this.cellWalker.to(10);
        this.cellWalker.setCellValue(value);
        PowerMock.verifyAll();
    }

    @Test
    public final void testPercentageFloat() {
        expect(this.row.getOrCreateCell(10)).andReturn(this.cell);
        this.cell.setPercentageValue(0.98f);
        PowerMock.replayAll();
        this.cellWalker.to(10);
        this.cellWalker.setPercentageValue(0.98f);
        PowerMock.verifyAll();
    }

    @Test
    public final void testPercentageNumber() {
        expect(this.row.getOrCreateCell(10)).andReturn(this.cell);
        this.cell.setPercentageValue(0.98);
        PowerMock.replayAll();
        this.cellWalker.to(10);
        this.cellWalker.setPercentageValue(0.98);
        PowerMock.verifyAll();
    }

    @Test
    public final void testPercentageInt() {
        expect(this.row.getOrCreateCell(10)).andReturn(this.cell);
        this.cell.setPercentageValue(98);
        PowerMock.replayAll();
        this.cellWalker.to(10);
        this.cellWalker.setPercentageValue(98);
        PowerMock.verifyAll();
    }

    @Test
    public final void testSetTimeValueMillis() {
        PowerMock.resetAll();
        EasyMock.expect(this.row.getOrCreateCell(0)).andReturn(this.cell);
        this.cell.setTimeValue(TIME_IN_MILLIS);

        PowerMock.replayAll();
        this.cellWalker.setTimeValue(TIME_IN_MILLIS);

        PowerMock.verifyAll();
    }

    @Test
    public final void testSetTimeValue() {
        PowerMock.resetAll();
        EasyMock.expect(this.row.getOrCreateCell(0)).andReturn(this.cell);
        this.cell.setTimeValue(1, 2, 3, 4, 5, 6);

        PowerMock.replayAll();
        this.cellWalker.setTimeValue(1, 2, 3, 4, 5, 6);

        PowerMock.verifyAll();
    }

    @Test
    public final void testSetNegTimeValue() {
        PowerMock.resetAll();
        EasyMock.expect(this.row.getOrCreateCell(0)).andReturn(this.cell);
        this.cell.setNegTimeValue(1, 2, 3, 4, 5, 6);

        PowerMock.replayAll();
        this.cellWalker.setNegTimeValue(1, 2, 3, 4, 5, 6);

        PowerMock.verifyAll();
    }

    @Test
    public final void testSetTooltipLong() {
        PowerMock.resetAll();
        EasyMock.expect(this.row.getOrCreateCell(0)).andReturn(this.cell);
        this.cell.setTooltip("tt", SimpleLength.cm(3), SimpleLength.cm(4), true);

        PowerMock.replayAll();
        this.cellWalker.setTooltip("tt", SimpleLength.cm(3), SimpleLength.cm(4), true);

        PowerMock.verifyAll();
    }

    @Test
    public final void testDataStyle() {
        final BooleanStyle bs = new BooleanStyleBuilder("bs", Locale.US).build();

        PowerMock.resetAll();
        EasyMock.expect(this.row.getOrCreateCell(0)).andReturn(this.cell);
        this.cell.setDataStyle(bs);

        PowerMock.replayAll();
        this.cellWalker.setDataStyle(bs);

        PowerMock.verifyAll();
    }

    @Test
    public final void testHasValue() {
        PowerMock.resetAll();
        EasyMock.expect(this.row.getOrCreateCell(0)).andReturn(this.cell);
        EasyMock.expect(this.cell.hasValue()).andReturn(true);

        PowerMock.replayAll();
        final boolean ret = this.cellWalker.hasValue();

        PowerMock.verifyAll();
        Assert.assertTrue(ret);
    }

    private TableRow initRealRow() {
        final StylesContainer stc = PowerMock.createMock(StylesContainer.class);
        final XMLUtil xmlUtil = XMLUtil.create();
        final DataStyles ds = DataStylesBuilder.create(Locale.US).build();
        final WriteUtil writeUtil = WriteUtil.create();
        return new TableRow(writeUtil, xmlUtil, stc, ds, false, null, 10, 100);
    }
}
