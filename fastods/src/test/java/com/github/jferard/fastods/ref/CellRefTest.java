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

public class CellRefTest {
    private TableNameUtil tableNameUtil;

    @Before
    public void setUp() {
        this.tableNameUtil = new TableNameUtil();
    }

    @Test
    public void testD3() {
        Assert.assertEquals("D3", CellRef.create(2, 3, 0).toString());
    }

    @Test
    public void testD3AbsCol() {
        Assert.assertEquals("$D3", CellRef.create(2, 3, 1).toString());
    }

    @Test
    public void testD3AbsRow() {
        Assert.assertEquals("D$3", CellRef.create(2, 3, 2).toString());
    }

    @Test
    public void testD3AbsTable() {
        Assert.assertEquals("D3", CellRef.create(2, 3, 4).toString());
    }

    @Test
    public void testD3Table2() {
        Assert.assertEquals("t.D3",
                new CellRef(TableRef.builder(this.tableNameUtil).table("t").build(),
                        new LocalCellRef(2, 3, 4)).toString());
    }

    @Test
    public void testD3AbsTable3() {
        Assert.assertEquals("$'t''t'.D3",
                new CellRef(TableRef.builder(this.tableNameUtil).absTable("t't").build(),
                        new LocalCellRef(2, 3, 4)).toString());
    }

    @Test
    public void testFilename() {
        Assert.assertEquals("'f'''#$t.D3",
                new CellRef(TableRef.builder(this.tableNameUtil).file("f'").absTable("t").build(),
                        new LocalCellRef(2, 3, 4)).toString());
    }

}