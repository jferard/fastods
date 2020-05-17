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

package com.github.jferard.fastods.ref;

import org.junit.Assert;
import org.junit.Test;

public class LocalCellRefTest {
    @Test
    public void testA1() {
        Assert.assertEquals("A1", new LocalCellRef(0, 0, 0).toString());

    }

    @Test
    public void testdA1() {
        Assert.assertEquals("$A1", new LocalCellRef(0, 0, 1).toString());

    }

    @Test
    public void testAd1() {
        Assert.assertEquals("A$1", new LocalCellRef(0, 0, 2).toString());

    }

    @Test
    public void testdAd1() {
        Assert.assertEquals("$A$1", new LocalCellRef(0, 0, 3).toString());

    }

    @Test
    public void testSameEquals() {
        final LocalCellRef r = new LocalCellRef(0, 0, 3);
        Assert.assertEquals(r, r);

    }

    @Test
    public void testDifferentClasses() {
        final LocalCellRef r = new LocalCellRef(0, 0, 3);
        final Object o = new Object();
        Assert.assertNotEquals(o, r);
        Assert.assertNotEquals(r, o);
    }

    @Test
    public void testDifferentRow() {
        final LocalCellRef r1 = new LocalCellRef(1, 2, 3);
        final LocalCellRef r2 = new LocalCellRef(2, 2, 3);
        Assert.assertNotEquals(r1, r2);
        Assert.assertNotEquals(r2, r1);
    }

    @Test
    public void testDifferentColumn() {
        final LocalCellRef r1 = new LocalCellRef(1, 2, 3);
        final LocalCellRef r2 = new LocalCellRef(1, 3, 3);
        Assert.assertNotEquals(r1, r2);
        Assert.assertNotEquals(r2, r1);
    }

    @Test
    public void testDifferentStatus() {
        final LocalCellRef r1 = new LocalCellRef(1, 2, 3);
        final LocalCellRef r2 = new LocalCellRef(1, 2, 2);
        Assert.assertNotEquals(r1, r2);
        Assert.assertNotEquals(r2, r1);
    }

    @Test
    public void testHashCode() {
        final LocalCellRef r1 = new LocalCellRef(1, 2, 3);
        Assert.assertEquals(30817, r1.hashCode());
    }
}