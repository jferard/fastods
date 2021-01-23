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

public class CurrencyValueTest {
    @Test
    public void testSetFromDouble() throws FastOdsException {
        PowerMock.resetAll();
        final TableCell cell = PowerMock.createMock(TableCell.class);
        cell.setCurrencyValue(18.7, "€");

        PowerMock.replayAll();
        final CurrencyValue cv = CurrencyValue.from(18.7, "€");
        cv.setToCell(cell);

        PowerMock.verifyAll();
    }

    @Test
    public void testSetFromInteger() throws FastOdsException {
        PowerMock.resetAll();
        final TableCell cell = PowerMock.createMock(TableCell.class);
        cell.setCurrencyValue((Number) 16, "€");

        PowerMock.replayAll();
        final CurrencyValue cv = CurrencyValue.from(16, "€");
        cv.setToCell(cell);

        PowerMock.verifyAll();
    }

    @Test
    public void testFromCurrencyValue() throws FastOdsException {
        final CurrencyValue cv1 = CurrencyValue.from(16, "€");
        final CurrencyValue cv2 = CurrencyValue.from(cv1, "$");
        Assert.assertEquals(new CurrencyValue(16, "$"), cv2);
    }

    @Test(expected = FastOdsException.class)
    public void testFromObject() throws FastOdsException {
        CurrencyValue.from(new Object(), "€");
    }
}