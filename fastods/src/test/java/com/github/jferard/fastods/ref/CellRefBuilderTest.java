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

package com.github.jferard.fastods.ref;

import com.github.jferard.fastods.Table;
import org.easymock.EasyMock;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.powermock.api.easymock.PowerMock;

import java.io.File;

public class CellRefBuilderTest {
    private TableNameUtil tableNameUtil;
    private CellRefBuilder builder;

    @Before
    public void setUp() {
        this.tableNameUtil = new TableNameUtil();
        this.builder = new CellRefBuilder(this.tableNameUtil);
    }

    @Test
    public void testAbs() {
        final CellRef c = this.builder.absColumn(2).absRow(1).absTable("at").file("f").build();
        final CellRef expectedRef = new CellRef(new TableRef(this.tableNameUtil, "f", "at", 4),
                new LocalCellRef(1, 2, 3));
        Assert.assertEquals(expectedRef, c);
    }

    @Test
    public void test() {
        final CellRef c = this.builder.column(7).row(8).table("at").build();
        final TableRef tableRef = new TableRef(this.tableNameUtil, null, "at", 0);
        final LocalCellRef localCellRef = new LocalCellRef(8, 7, 0);
        final CellRef expectedRef = new CellRef(tableRef, localCellRef);
        Assert.assertEquals(expectedRef, c);
    }

    @Test
    public void testTableFile() {
        final Table table = PowerMock.createMock(Table.class);
        final File file = new File("f");

        PowerMock.resetAll();
        EasyMock.expect(table.getName()).andReturn("table");


        PowerMock.replayAll();
        final CellRef c = this.builder.column(7).row(8).table(table).file(file).build();

        PowerMock.verifyAll();
        final TableRef tableRef = new TableRef(this.tableNameUtil, "f", "table", 0);
        final LocalCellRef localCellRef = new LocalCellRef(8, 7, 0);
        final CellRef expectedRef = new CellRef(tableRef, localCellRef);
        Assert.assertEquals(expectedRef, c);
    }

    @Test
    public void testAbsTable() {
        final Table table = PowerMock.createMock(Table.class);

        PowerMock.resetAll();
        EasyMock.expect(table.getName()).andReturn("table");


        PowerMock.replayAll();
        final CellRef c = this.builder.absTable(table).build();

        PowerMock.verifyAll();
        final TableRef tableRef = new TableRef(this.tableNameUtil, null, "table", 4);
        final LocalCellRef localCellRef = new LocalCellRef(0, 0, 0);
        final CellRef expectedRef = new CellRef(tableRef, localCellRef);
        Assert.assertEquals(expectedRef, c);
    }
}