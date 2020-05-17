/*
 * FastODS - A very fast and lightweight (no dependency) library for creating ODS
 *    (Open Document Spreadsheet, mainly for Calc) files in Java.
 *    It's a Martin Schulz's SimpleODS fork
 *    Copyright (C) 2016-2020 J. Férard <https://github.com/jferard>
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

import com.github.jferard.fastods.attribute.CellType;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.powermock.api.easymock.PowerMock;

import java.util.Calendar;
import java.util.Date;

public class CellValueTest {
    private TableCell cell;
    private ToCellValueConverter converter;

    @Before
    public void setUp() {
        this.converter = new ObjectToCellValueConverter("USD");
        this.cell = PowerMock.createMock(TableCell.class);
        PowerMock.resetAll();
    }

    @After
    public void tearDown() {
        PowerMock.verifyAll();
    }

    @Test
    public final void testBooleanFromTypeAndObject() throws FastOdsException {
        final CellValue v = this.converter.from(CellType.BOOLEAN, true);

        // PLAY
        this.cell.setBooleanValue(true);

        PowerMock.replayAll();
        v.setToCell(this.cell);
    }

    @Test
    public final void testVoidFromObject() {
        final CellValue v = this.converter.from(null);

        // PLAY
        this.cell.setVoidValue();

        PowerMock.replayAll();
        v.setToCell(this.cell);
    }

    @Test
    public final void testStringFromObject() {
        final CellValue v = this.converter.from("str");

        // PLAY
        this.cell.setStringValue("str");

        PowerMock.replayAll();
        v.setToCell(this.cell);
    }

    @Test
    public final void testNumberFromObject() {
        final CellValue v = this.converter.from(10);

        // PLAY
        this.cell.setFloatValue((Number) 10);

        PowerMock.replayAll();
        v.setToCell(this.cell);
    }

    @Test
    public final void testBooleanFromObject() {
        final CellValue v = this.converter.from(true);

        // PLAY
        this.cell.setBooleanValue(true);

        PowerMock.replayAll();
        v.setToCell(this.cell);
    }

    @Test
    public final void testDateFromObject() {
        final Date d = new Date(0);
        final CellValue v = this.converter.from(d);

        // PLAY
        this.cell.setDateValue(d);

        PowerMock.replayAll();
        v.setToCell(this.cell);
    }

    @Test
    public final void testCalendarFromObject() {
        final Calendar c = Calendar.getInstance();
        final CellValue v = this.converter.from(c);

        // PLAY
        this.cell.setDateValue(c.getTime());

        PowerMock.replayAll();
        v.setToCell(this.cell);
    }

    @Test
    public final void testOtherFromObject() {
        final Character j = 'j';
        final CellValue v = this.converter.from(j);

        // PLAY
        this.cell.setStringValue("j");

        PowerMock.replayAll();
        v.setToCell(this.cell);
    }
}