/*
 * FastODS - A very fast and lightweight (no dependency) library for creating ODS
 *    (Open Document Spreadsheet, mainly for Calc) files in Java.
 *    It's a Martin Schulz's SimpleODS fork
 *    Copyright (C) 2016-2021 J. Férard <https://github.com/jferard>
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

public class PercentageValueTest {
    @Test
    public void testSetFromDouble() throws FastOdsException {
        PowerMock.resetAll();
        final TableCell cell = PowerMock.createMock(TableCell.class);
        cell.setPercentageValue(0.25);

        PowerMock.replayAll();
        final PercentageValue cv = PercentageValue.from(0.25);
        cv.setToCell(cell);

        PowerMock.verifyAll();
    }

    @Test
    public void testFromPercentageValue() throws FastOdsException {
        final PercentageValue cv1 = PercentageValue.from(0.25);
        final PercentageValue cv2 = PercentageValue.from(cv1);
        Assert.assertEquals(cv1, cv2);
    }

    @Test(expected = FastOdsException.class)
    public void testFromObject() throws FastOdsException {
        PercentageValue.from(new Object());
    }

    @Test
    public void testHashCode() throws FastOdsException {
        final PercentageValue c1 = new PercentageValue(16);
        Assert.assertEquals(16, c1.hashCode());
    }

    @Test
    public void testEquals() throws FastOdsException {
        final PercentageValue c1 = new PercentageValue(16);
        final PercentageValue c2 = PercentageValue.from(c1);
        final PercentageValue c3 = PercentageValue.from(17);
        Assert.assertEquals(c1, c1);
        Assert.assertEquals(c1, c2);
        Assert.assertNotEquals(c1, c3);
        Assert.assertNotEquals(c1, new Object());
    }
}