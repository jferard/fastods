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
import com.github.jferard.fastods.util.EqualityUtil;
import com.github.jferard.fastods.util.PositionUtil;
import com.github.jferard.fastods.util.TableNameUtil;
import com.github.jferard.fastods.util.WriteUtil;
import com.github.jferard.fastods.util.XMLUtil;
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
    private TableCellWalkerImpl cellWalker;
    private TableRow row;
    private XMLUtil util;
    private StringBuilder sb;
    private TableCell cell;

    @Before
    public void setUp() {
        this.row = PowerMock.createMock(TableRow.class);
        this.cell = PowerMock.createMock(TableCell.class);
        this.cellWalker = new TableCellWalkerImpl(this.row);
        this.util = XMLUtil.create();
        this.sb = new StringBuilder();
        PowerMock.resetAll();
    }

    @Test(expected = UnsupportedOperationException.class)
    public final void testAppendXML() throws IOException {
        // REPLAY
        PowerMock.replayAll();
        this.cellWalker.appendXMLToTableRow(this.util, this.sb);
        PowerMock.verifyAll();
    }

    @Test(expected = UnsupportedOperationException.class)
    public final void testMarkRowsSpanned() throws IOException {
        // REPLAY
        PowerMock.replayAll();
        this.cellWalker.markRowsSpanned(5);
        PowerMock.verifyAll();
    }

    @Test(expected = UnsupportedOperationException.class)
    public final void testMarkColumnsSpanned() throws IOException {
        // REPLAY
        PowerMock.replayAll();
        this.cellWalker.markColumnsSpanned(5);
        PowerMock.verifyAll();
    }

    @Test
    public final void testBoolean() throws IOException {
        // PLAY
        expect(this.row.getOrCreateCell(10)).andReturn(this.cell);
        this.cell.setBooleanValue(true);
        expect(this.row.getColumnCount()).andReturn(20);

        // REPLAY
        PowerMock.replayAll();
        this.cellWalker.to(10);
        this.cellWalker.setBooleanValue(true);
        this.cellWalker.next();
        PowerMock.verifyAll();
    }

    @Test
    public final void testText() throws IOException {
        final Text t = Text.content("a");

        // PLAY
        expect(this.row.getOrCreateCell(10)).andReturn(this.cell);
        this.cell.setText(t);
        expect(this.row.getColumnCount()).andReturn(20);

        // REPLAY
        PowerMock.replayAll();
        this.cellWalker.to(10);
        this.cellWalker.setText(t);
        this.cellWalker.next();
        PowerMock.verifyAll();
    }

    @Test
    public final void testCellMerge() throws IOException {

        // PLAY
        this.row.setCellMerge(10, 5, 6);

        // REPLAY
        PowerMock.replayAll();
        this.cellWalker.to(10);
        this.cellWalker.setCellMerge(5, 6);
        PowerMock.verifyAll();
    }

    @Test
    public final void testColumnsSpanned() throws IOException {

        // PLAY
        this.row.setColumnsSpanned(10, 12);

        // REPLAY
        PowerMock.replayAll();
        this.cellWalker.to(10);
        this.cellWalker.setColumnsSpanned(12);
        PowerMock.verifyAll();
    }

    @Test
    public final void testRowsSpanned() throws IOException {

        // PLAY
        this.row.setRowsSpanned(10, 12);

        // REPLAY
        PowerMock.replayAll();
        this.cellWalker.to(10);
        this.cellWalker.setRowsSpanned(12);
        PowerMock.verifyAll();
    }

    @Test
    public final void testVoidValue() {
        expect(this.row.getOrCreateCell(10)).andReturn(this.cell);
        this.cell.setVoidValue();

        PowerMock.replayAll();
        this.cellWalker.to(10);
        this.cellWalker.setVoidValue();
        PowerMock.verifyAll();
    }

    @Test
    public final void testTimeValue() throws IOException {
        // PLAY
        expect(this.row.getOrCreateCell(10)).andReturn(this.cell);
        this.cell.setTimeValue(1000);

        // REPLAY
        PowerMock.replayAll();
        this.cellWalker.to(10);
        this.cellWalker.setTimeValue(1000);
        PowerMock.verifyAll();
    }

    @Test
    public final void testTooltip() throws IOException {
        // PLAY
        expect(this.row.getOrCreateCell(10)).andReturn(this.cell);
        this.cell.setTooltip("1000");

        // REPLAY
        PowerMock.replayAll();
        this.cellWalker.to(10);
        this.cellWalker.setTooltip("1000");
        PowerMock.verifyAll();
    }

    @Test
    public final void testFormula() throws IOException {
        // PLAY
        expect(this.row.getOrCreateCell(10)).andReturn(this.cell);
        this.cell.setFormula("1000");

        // REPLAY
        PowerMock.replayAll();
        this.cellWalker.to(10);
        this.cellWalker.setFormula("1000");
        PowerMock.verifyAll();
    }

    @Test
    public final void testIsCovered() throws IOException {
        // PLAY
        expect(this.row.getOrCreateCell(10)).andReturn(this.cell);
        expect(this.cell.isCovered()).andReturn(true);

        // REPLAY
        PowerMock.replayAll();
        this.cellWalker.to(10);
        Assert.assertTrue(this.cellWalker.isCovered());
        PowerMock.verifyAll();
    }

    @Test
    public final void testSetCovered() throws IOException {
        // PLAY
        expect(this.row.getOrCreateCell(10)).andReturn(this.cell);
        this.cell.setCovered();

        // REPLAY
        PowerMock.replayAll();
        this.cellWalker.to(10);
        this.cellWalker.setCovered();
        PowerMock.verifyAll();
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public final void testNext() throws IOException {
        // PLAY
        expect(this.row.getColumnCount()).andReturn(20);

        // REPLAY
        PowerMock.replayAll();
        this.cellWalker.to(20);
        this.cellWalker.next();
        PowerMock.verifyAll();
    }

    @Test
    public final void testPrevious() throws IOException {
        // PLAY

        // REPLAY
        PowerMock.replayAll();
        this.cellWalker.to(5);
        this.cellWalker.previous();
        PowerMock.verifyAll();
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public final void testPreviousIOOBE() throws IOException {
        // PLAY

        // REPLAY
        PowerMock.replayAll();
        this.cellWalker.previous();
        PowerMock.verifyAll();
    }

    @Test
    public final void testCalendar() {
        final Calendar c = Calendar.getInstance();
        c.setTimeInMillis(1234567891011L);

        expect(this.row.getOrCreateCell(10)).andReturn(this.cell);
        this.cell.setDateValue(c);

        PowerMock.replayAll();
        this.cellWalker.to(10);
        this.cellWalker.setDateValue(c);
        PowerMock.verifyAll();
    }

    @Test
    public final void testCurrencyFloat() {
        expect(this.row.getOrCreateCell(10)).andReturn(this.cell);
        this.cell.setCurrencyValue(10.0f, "€");
        PowerMock.replayAll();
        this.cellWalker.to(10);
        this.cellWalker.setCurrencyValue(10.0f, "€");
        PowerMock.verifyAll();
    }

    @Test
    public final void testCurrencyInt() {
        expect(this.row.getOrCreateCell(10)).andReturn(this.cell);
        this.cell.setCurrencyValue(9, "€");
        PowerMock.replayAll();
        this.cellWalker.to(10);
        this.cellWalker.setCurrencyValue(9, "€");
        PowerMock.verifyAll();
    }

    @Test
    public final void testCurrencyNumber() {
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
        c.setTimeInMillis(1234567891011L);
        final Date date = c.getTime();

        expect(this.row.getOrCreateCell(10)).andReturn(this.cell);
        this.cell.setDateValue(date);
        PowerMock.replayAll();
        this.cellWalker.to(10);
        this.cellWalker.setDateValue(date);
        PowerMock.verifyAll();
    }

    @Test
    public final void testDouble() {
        expect(this.row.getOrCreateCell(10)).andReturn(this.cell);
        this.cell.setFloatValue(10.999);
        PowerMock.replayAll();
        this.cellWalker.to(10);
        this.cellWalker.setFloatValue(10.999);
        PowerMock.verifyAll();
    }

    @Test
    public final void testFloat() {
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
        final TableRow row = this.initRealRow();
        final TableCellWalkerImpl cell = new TableCellWalkerImpl(row);
        PowerMock.replayAll();
        cell.to(-1);
        PowerMock.verifyAll();
    }

    @Test
    @SuppressWarnings("deprecation")
    public final void testObject() {
        expect(this.row.getOrCreateCell(10)).andReturn(this.cell);
        this.cell.setObjectValue(null);
        PowerMock.replayAll();
        this.cellWalker.to(10);
        this.cellWalker.setObjectValue(null);
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

    private TableRow initRealRow() {
        final StylesContainer stc = PowerMock.createMock(StylesContainer.class);
        final PositionUtil positionUtil = new PositionUtil(new EqualityUtil(), new TableNameUtil());
        final XMLUtil xmlUtil = XMLUtil.create();
        final DataStyles ds = DataStylesBuilder.create(Locale.US).build();
        final WriteUtil writeUtil = WriteUtil.create();
        return new TableRow(writeUtil, xmlUtil, stc, ds, false, null, 10, 100);
    }
}
