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

public class StringValueTest {
    @Test
    public void testHashCode() {
        final StringValue v = new StringValue("ok");
        Assert.assertEquals("ok".hashCode(), v.hashCode());
    }

    @Test
    public void testFromStringValue() {
        final CellValue sv1 = StringValue.from("ok");
        final CellValue sv2 = StringValue.from(sv1);
        Assert.assertEquals(sv1, sv2);
    }

    @Test
    public void testFromTextValue() {
        final CellValue tv1 = new TextValue(Text.content("ok"));
        final CellValue sv2 = StringValue.from(tv1);
        Assert.assertEquals(tv1, sv2);
    }

    @Test
    public void testFromObject() {
        final Object o = new Object();
        final CellValue sv1 = StringValue.from(o);
        Assert.assertEquals(new StringValue(o.toString()), sv1);
    }

    @Test
    public void testEquals() {
        final StringValue v = new StringValue("ok");
        Assert.assertEquals(v, v);
        Assert.assertNotEquals(1, v);
        Assert.assertNotEquals(v, 1);
        Assert.assertEquals(v, new StringValue("ok"));
        Assert.assertEquals(new StringValue("ok"), v);
        Assert.assertNotEquals(v, new StringValue("ko"));
        Assert.assertNotEquals(new StringValue("ko"), v);
    }

    @Test
    public void testToString() {
        final StringValue v = new StringValue("ok");
        Assert.assertEquals(v.toString(), "StringValue[ok]");
    }

    @Test
    public void testCell() {
        PowerMock.replayAll();
        final TableCell cell = PowerMock.createMock(TableCell.class);
        cell.setStringValue("ok");

        PowerMock.replayAll();
        final CellValue v = new StringValue("ok");
        v.setToCell(cell);

        PowerMock.verifyAll();
    }
}