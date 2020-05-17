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

package com.github.jferard.fastods.ref;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class RangeRefTest {
    private TableNameUtil tableNameUtil;

    @Before
    public void setUp() {
        this.tableNameUtil = new TableNameUtil();
    }

    @Test
    public void testA1C5() {
        Assert.assertEquals("A1:C5",
                new RangeRef(null, new LocalCellRef(0, 0, 0), new LocalCellRef(4, 2, 0))
                        .toString());
    }

    @Test
    public void testTableA1C5() {
        Assert.assertEquals("t.A1:C5", new RangeRef(new TableRef(this.tableNameUtil, null, "t", 0),
                new LocalCellRef(0, 0, 0), new LocalCellRef(4, 2, 0)).toString());
    }

    @Test
    public void testA1C5HashCode() {
        Assert.assertEquals(987009,
                new RangeRef(null, new LocalCellRef(0, 0, 0), new LocalCellRef(4, 2, 0))
                        .hashCode());
    }

    @Test
    public void testTableA1C5HashCode() {
        Assert.assertEquals(33071916, new RangeRef(new TableRef(this.tableNameUtil, null, "t", 0),
                new LocalCellRef(0, 0, 0), new LocalCellRef(4, 2, 0)).hashCode());
    }

    @Test
    public void testTableSameEquals() {
        final RangeRef t = new RangeRef(new TableRef(this.tableNameUtil, null, "t", 0),
                new LocalCellRef(0, 0, 0), new LocalCellRef(4, 2, 0));
        Assert.assertEquals(t, t);
    }

    @Test
    public void testTableDifferent() {
        final Object o = new Object();
        final RangeRef t = new RangeRef(new TableRef(this.tableNameUtil, null, "t", 0),
                new LocalCellRef(0, 0, 0), new LocalCellRef(4, 2, 0));
        Assert.assertNotEquals(o, t);
        Assert.assertNotEquals(t, o);
    }

    @Test
    public void testTableDifferentNotTable() {
        Assert.assertNotEquals(new RangeRef(new TableRef(this.tableNameUtil, null, "t", 0),
                        new LocalCellRef(0, 0, 0), new LocalCellRef(4, 2, 0)),
                new RangeRef(null, new LocalCellRef(0, 0, 0), new LocalCellRef(4, 2, 0)));
    }

    @Test
    public void testTableDifferentFrom() {
        Assert.assertNotEquals(
                new RangeRef(null, new LocalCellRef(1, 1, 0), new LocalCellRef(4, 2, 0)),
                new RangeRef(null, new LocalCellRef(0, 0, 0), new LocalCellRef(4, 2, 0)));
    }

    @Test
    public void testTableDifferentTo() {
        Assert.assertNotEquals(
                new RangeRef(null, new LocalCellRef(0, 0, 0), new LocalCellRef(4, 2, 2)),
                new RangeRef(null, new LocalCellRef(0, 0, 0), new LocalCellRef(4, 2, 0)));
    }

    @Test
    public void testGet() {
        final RangeRef r = new RangeRef(null, new LocalCellRef(1, 0, 0), new LocalCellRef(4, 2, 2));
        Assert.assertEquals(1, r.getFromRow());
        Assert.assertEquals(0, r.getFromColumn());
        Assert.assertEquals(4, r.getToRow());
        Assert.assertEquals(2, r.getToColumn());
    }
}