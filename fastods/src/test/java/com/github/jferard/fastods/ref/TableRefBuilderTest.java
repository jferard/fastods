/*
 * FastODS - A very fast and lightweight (no dependency) library for creating ODS
 *    (Open Document Spreadsheet, mainly for Calc) files in Java.
 *    It's a Martin Schulz's SimpleODS fork
 *    Copyright (C) 2016-2023 J. Férard <https://github.com/jferard>
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

public class TableRefBuilderTest {
    private TableNameUtil tableNameUtil;

    @Before
    public void setUp() {
        this.tableNameUtil = new TableNameUtil();
    }

    @Test
    public void testAbsTable() {
        final TableRef tr = new TableRefBuilder(this.tableNameUtil).absTable("at").build();
        final TableRef expectedTableRef = new TableRef(this.tableNameUtil, null, "at", 4);
        Assert.assertEquals(expectedTableRef, tr);
    }

    @Test
    public void testTable() {
        final TableRef tr = new TableRefBuilder(this.tableNameUtil).table("at").build();
        final TableRef expectedTableRef = new TableRef(this.tableNameUtil, null, "at", 0);
        Assert.assertEquals(expectedTableRef, tr);
    }

    @Test
    public void testTableName() {
        Assert.assertEquals("table",
                new TableRefBuilder(this.tableNameUtil).table("table").build().toString());
    }

    @Test
    public void testSameEquals() {
        Assert.assertEquals("table",
                new TableRefBuilder(this.tableNameUtil).table("table").build().toString());
    }

    @Test
    public void testAbsTableName() {
        Assert.assertEquals("$table",
                new TableRefBuilder(this.tableNameUtil).absTable("table").build().toString());
    }

    @Test
    public void testFilename() {
        Assert.assertNull(new TableRefBuilder(this.tableNameUtil).file("fname").build());
    }

    @Test
    public void testTableNameQuote() {
        Assert.assertEquals("$'tab''le'",
                new TableRefBuilder(this.tableNameUtil).absTable("tab'le").build().toString());
    }

    @Test
    public void testFilenameAndTableName() {
        Assert.assertEquals("'fname'#table",
                new TableRefBuilder(this.tableNameUtil).file("fname").table("table").build()
                        .toString());
    }

    @Test
    public void testFilenameAndAbsTableName() {
        Assert.assertEquals("'fname'#$table",
                new TableRefBuilder(this.tableNameUtil).file("fname").absTable("table").build()
                        .toString());
    }
}