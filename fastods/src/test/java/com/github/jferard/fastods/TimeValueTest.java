/*
 * FastODS - A very fast and lightweight (no dependency) library for creating ODS
 *    (Open Document Spreadsheet, mainly for Calc) files in Java.
 *    It's a Martin Schulz's SimpleODS fork
 *    Copyright (C) 2016-2022 J. Férard <https://github.com/jferard>
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

import org.junit.Assert;
import org.junit.Test;
import org.powermock.api.easymock.PowerMock;

public class TimeValueTest {
    @Test
    public void testSetFromLong() throws FastOdsException {
        PowerMock.resetAll();
        final TableCell cell = PowerMock.createMock(TableCell.class);
        cell.setTimeValue(0, 0, 0, 0, 0, 123.456);

        PowerMock.replayAll();
        final TimeValue tv = TimeValue.from(123456L);
        tv.setToCell(cell);

        PowerMock.verifyAll();
    }

    @Test
    public void testFromTimeValue() throws FastOdsException {
        final TimeValue tv1 = TimeValue.from(123456L);
        final TimeValue tv2 = TimeValue.from(tv1);
        Assert.assertEquals(tv1, tv2);
    }

    @Test
    public void testFromString() {
        Assert.assertThrows(FastOdsException.class, () -> TimeValue.from(""));
    }

    @Test
    public void testFromNegLong() throws FastOdsException {
        PowerMock.resetAll();
        final TableCell cell = PowerMock.createMock(TableCell.class);
        cell.setNegTimeValue(0, 0, 0, 0, 0, 123.456);

        PowerMock.replayAll();
        final TimeValue tv = TimeValue.from(-123456L);
        tv.setToCell(cell);

        PowerMock.verifyAll();
    }

    @Test
    public void testEqualsBase() throws FastOdsException {
        final TimeValue tv1 = new TimeValue(false, 0, 0, 1, 2, 3, 4);
        Assert.assertEquals(tv1, tv1);
        Assert.assertNotEquals(tv1, new Object());
    }

    @Test
    public void testEquals() throws FastOdsException {
        final TimeValue tv = new TimeValue(false, 0, 0, 1, 2, 3, 4);
        final long l = ((((1 * 24 + 2) * 60) + 3) * 60 + 4) * 1000;

        Assert.assertEquals(tv, TimeValue.from(l));
        Assert.assertNotEquals(tv, TimeValue.from(-l));
    }

    @Test
    public void testEqualsNeg() throws FastOdsException {
        final TimeValue tv = new TimeValue(true, 0, 0, 1, 2, 3, 4);
        final long l = ((((1 * 24 + 2) * 60) + 3) * 60 + 4) * 1000;
        Assert.assertEquals(tv, TimeValue.from(-l));
        Assert.assertNotEquals(tv, TimeValue.from(l));
    }

    @Test
    public void testNotEquals() throws FastOdsException {
        final TimeValue tv1 = new TimeValue(true, 0, 0, 1, 2, 3, 4);
        final TimeValue tv2 = new TimeValue(false, 0, 0, 1, 2, 3, 4);
        final TimeValue tv3 = new TimeValue(true, 0, 1, 1, 2, 3, 4);
        final TimeValue tv4 = new TimeValue(true, 0, 0, 0, 2, 3, 4);

        Assert.assertNotEquals(tv1, tv2);
        Assert.assertNotEquals(tv1, tv3);
        Assert.assertNotEquals(tv1, tv4);
        Assert.assertNotEquals(tv2, tv3);
        Assert.assertNotEquals(tv2, tv4);
        Assert.assertNotEquals(tv3, tv4);
    }

    @Test
    public void testHashcode() throws FastOdsException {
        final TimeValue tv1 = new TimeValue(false, 0, 0, 1, 2, 3, 4);
        Assert.assertEquals(93784, tv1.hashCode());

        final long l = ((((1 * 24 + 2) * 60) + 3) * 60 + 4) * 1000;
        Assert.assertEquals(93784, TimeValue.from(l).hashCode());
    }

    @Test
    public void testHashcodeNeg() throws FastOdsException {
        final TimeValue tv2 = new TimeValue(true, 0, 0, 1, 2, 3, 4);
        Assert.assertEquals(94745, tv2.hashCode());

        final long l = ((((1 * 24 + 2) * 60) + 3) * 60 + 4) * 1000;
        Assert.assertEquals(94745, TimeValue.from(-l).hashCode());
    }

    @Test
    public void testToString() {
        final TimeValue tv1 = new TimeValue(false, 0, 0, 1, 2, 3, 4);
        Assert.assertEquals("TimeValue[P0Y0M1DT2H3M4.0S]", tv1.toString());
    }

    @Test
    public void testToStringNeg() {
        final TimeValue tv2 = new TimeValue(true, 0, 0, 1, 2, 3, 4);
        Assert.assertEquals("TimeValue[-P0Y0M1DT2H3M4.0S]", tv2.toString());
    }
}