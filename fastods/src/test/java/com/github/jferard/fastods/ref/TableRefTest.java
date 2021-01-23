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

package com.github.jferard.fastods.ref;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class TableRefTest {
    private TableNameUtil tableNameUtil;

    @Before
    public void setUp() {
        this.tableNameUtil = new TableNameUtil();
    }

    @Test
    public void testTableName() {
        Assert.assertEquals("table", new TableRef(this.tableNameUtil, null, "table", 0).toString());
    }

    @Test
    public void testAbsTableName() {
        Assert.assertEquals("$table",
                new TableRef(this.tableNameUtil, null, "table", 4).toString());
    }

    @Test
    public void testTableNameQuote() {
        Assert.assertEquals("$'tab''le'",
                new TableRef(this.tableNameUtil, null, "tab'le", 4).toString());
    }

    @Test
    public void testFilenameAndTableName() {
        Assert.assertEquals("'fname'#table",
                new TableRef(this.tableNameUtil, "fname", "table", 0).toString());
    }

    @Test
    public void testFilenameAndAbsTableName() {
        Assert.assertEquals("'fname'#$table",
                new TableRef(this.tableNameUtil, "fname", "table", 4).toString());
    }

    @Test
    public void testSameEquals() {
        final TableRef t = new TableRef(this.tableNameUtil, "fname", "table", 0);
        Assert.assertEquals(t, t);
    }

    @Test
    public void testNotEqualsDifferentObject() {
        final TableRef t = new TableRef(this.tableNameUtil, "fname", "table", 0);
        final Object o = new Object();
        Assert.assertNotEquals(o, t);
        Assert.assertNotEquals(t, o);
    }

    @Test
    public void testNotEqualsFname() {
        final TableRef t1 = new TableRef(this.tableNameUtil, "fname", "table", 0);
        final TableRef t2 = new TableRef(this.tableNameUtil, null, "table", 0);
        Assert.assertNotEquals(t1, t2);
        Assert.assertNotEquals(t2, t1);
    }

    @Test
    public void testNotEqualsTname() {
        final TableRef t1 = new TableRef(this.tableNameUtil, "fname", "table1", 0);
        final TableRef t2 = new TableRef(this.tableNameUtil, "fname", "table2", 0);
        Assert.assertNotEquals(t1, t2);
        Assert.assertNotEquals(t2, t1);
    }

    @Test
    public void testNotEqualsStatus() {
        final TableRef t1 = new TableRef(this.tableNameUtil, "fname", "table", 0);
        final TableRef t2 = new TableRef(this.tableNameUtil, "fname", "table", 1);
        Assert.assertNotEquals(t1, t2);
        Assert.assertNotEquals(t2, t1);
    }

    @Test
    public void testHashCode() {
        final TableRef t1 = new TableRef(this.tableNameUtil, "fname", "table", 0);
        Assert.assertEquals(-1603120638, t1.hashCode());
    }
}