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

import org.junit.Assert;
import org.junit.Test;
import org.powermock.api.easymock.PowerMock;

/**
 * Created by jferard on 09/05/17.
 */
public class FloatValueTest {
    @Test
    public void testEquals() {
        final CellValue fv1 = new FloatValue(10.0);
        final CellValue fv2 = new FloatValue(10.0);
        final CellValue fv3 = new FloatValue(11.0);
        final CellValue bv1 = new BooleanValue(true);
        Assert.assertEquals(fv1, fv1);
        Assert.assertEquals(fv1, fv2);
        Assert.assertEquals(fv1.hashCode(), fv2.hashCode());
        Assert.assertNotEquals(fv1, fv3);
        Assert.assertNotEquals(fv1.hashCode(), fv3.hashCode());
        Assert.assertNotEquals(fv1, bv1);
        Assert.assertNotEquals(fv1.hashCode(), bv1.hashCode());
        Assert.assertNotEquals(fv1, "fv1");
        Assert.assertNotEquals(fv1.hashCode(), "fv1".hashCode());
    }

    @Test
    public void testSet() {
        final CellValue fv1 = new FloatValue(10.0);
        final TableCell cell = PowerMock.createMock(TableCell.class);

        PowerMock.resetAll();
        cell.setFloatValue(10.0);

        PowerMock.replayAll();
        fv1.setToCell(cell);
        PowerMock.verifyAll();
    }

    @Test
    public void testFromFloatValue() throws FastOdsException {
        final CellValue fv1 = FloatValue.from(10.0);
        final CellValue fv2 = FloatValue.from(fv1);
        Assert.assertEquals(fv1, fv2);
        Assert.assertEquals(fv2, fv1);
    }

    @Test(expected = FastOdsException.class)
    public void testFromObject() throws FastOdsException {
        FloatValue.from(new Object());
    }

    @Test
    public void testNumber() throws FastOdsException {
        Assert.assertEquals(FloatValue.from(13), new FloatValue(13));
    }
}