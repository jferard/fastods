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

public class BooleanValueTest {
    @Test
    public final void testSetConstructor() {
        PowerMock.resetAll();
        final TableCell cell = PowerMock.createMock(TableCell.class);
        cell.setBooleanValue(true);

        PowerMock.replayAll();
        final CellValue cv = new BooleanValue(true);
        cv.setToCell(cell);

        PowerMock.verifyAll();
    }

    @Test
    public final void testFromBoolean() throws FastOdsException {
        Assert.assertEquals(new BooleanValue(true), BooleanValue.from(true));
        Assert.assertNotEquals(new BooleanValue(true), BooleanValue.from(false));
        Assert.assertEquals(new BooleanValue(false), BooleanValue.from(false));
        Assert.assertNotEquals(new BooleanValue(false), BooleanValue.from(true));
    }

    @Test
    public final void testFromBooleanValue() throws FastOdsException {
        final BooleanValue bv1 = new BooleanValue(true);
        final BooleanValue bv2 = BooleanValue.from(bv1);
        Assert.assertEquals(bv1, bv2);
    }

    @Test
    public final void testFromBooleanObject() {
        Assert.assertThrows(FastOdsException.class, () -> BooleanValue.from(new Object()));
    }

    @Test
    public final void testEquals() throws FastOdsException {
        final BooleanValue bv1 = new BooleanValue(true);
        final BooleanValue bv2 = BooleanValue.from(bv1);
        Assert.assertEquals(bv1, bv1);
        Assert.assertEquals(bv1, bv2);
        Assert.assertNotEquals(bv1, new Object());
    }
}