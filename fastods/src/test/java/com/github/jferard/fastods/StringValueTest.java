/*
 * FastODS - A very fast and lightweight (no dependency) library for creating ODS
 *    (Open Document Spreadsheet, mainly for Calc) files in Java.
 *    It's a Martin Schulz's SimpleODS fork
 *    Copyright (C) 2016-2018 J. Férard <https://github.com/jferard>
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

public class StringValueTest {
    @Test
    public void testHashCode() {
        final StringValue v = new StringValue("ok");
        Assert.assertEquals("ok".hashCode(), v.hashCode());
    }

    @Test
    public void testEquals() {
        final StringValue v = new StringValue("ok");
        Assert.assertTrue(v.equals(v));
        Assert.assertFalse(v.equals(1));
        Assert.assertTrue(v.equals(new StringValue("ok")));
        Assert.assertFalse(v.equals(new StringValue("ko")));
    }

    @Test
    public void testCell() {
        final TableCell cell = PowerMock.createMock(TableCell.class);
        final StringValue v = new StringValue("ok");

        // PLAY
        cell.setStringValue("ok");

        PowerMock.replayAll();
        v.setToCell(cell);
        PowerMock.verifyAll();
    }
}