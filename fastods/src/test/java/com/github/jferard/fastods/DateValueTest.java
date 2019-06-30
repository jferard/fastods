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

import org.junit.Test;
import org.powermock.api.easymock.PowerMock;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * @author Julien Férard
 */
public class DateValueTest {
    @Test
    public final void testSetConstructor() {
        PowerMock.resetAll();
        final TableCell cell = PowerMock.createMock(TableCell.class);
        cell.setDateValue(new Date(10));

        PowerMock.replayAll();
        final Date date = new Date(10);
        final CellValue dv = new DateValue(date);
        date.setTime(0);
        dv.setToCell(cell);

        PowerMock.verifyAll();
    }

    @Test
    public final void testFromDate() throws FastOdsException {
        PowerMock.resetAll();
        final TableCell cell = PowerMock.createMock(TableCell.class);
        cell.setDateValue(new Date(10));

        PowerMock.replayAll();
        final Date date = new Date(10);
        final CellValue dv = DateValue.from(date);
        date.setTime(0);
        dv.setToCell(cell);

        PowerMock.verifyAll();
    }

    @Test
    public final void testFromCalendar() throws FastOdsException {
        PowerMock.resetAll();
        final TableCell cell = PowerMock.createMock(TableCell.class);
        cell.setDateValue(new GregorianCalendar(0, 0, 0, 0, 0, 127).getTime());

        PowerMock.replayAll();
        final Calendar cal = new GregorianCalendar(0, 0, 0, 0, 0, 127);
        final CellValue dv = DateValue.from(cal);
        cal.setTimeInMillis(0);
        dv.setToCell(cell);

        PowerMock.verifyAll();
    }

    @Test
    public final void testFromNumber() throws FastOdsException {
        PowerMock.resetAll();
        final TableCell cell = PowerMock.createMock(TableCell.class);
        cell.setDateValue(new Date(10));

        PowerMock.replayAll();
        final CellValue dv = DateValue.from(10);
        dv.setToCell(cell);

        PowerMock.verifyAll();
    }

    @Test(expected = FastOdsException.class)
    public final void testFromString() throws FastOdsException {
        final CellValue dv = DateValue.from("10");
    }
}
