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

public class RangeRefBuilderTest {
    private static final LocalCellRef A1 = new LocalCellRef(0, 0, 0);

    private TableNameUtil tableNameUtil;
    private RangeRefBuilder builder;

    @Before
    public void setUp() {
        this.tableNameUtil = new TableNameUtil();
        this.builder = new RangeRefBuilder(this.tableNameUtil);
    }

    @Test
    public void testToAbs() {
        final RangeRef r = this.builder.toAbsColumn(2).toAbsRow(1).absTable("at").file("f").build();
        final RangeRef expectedRef =
                new RangeRef(new TableRef(this.tableNameUtil, "f", "at", 4), A1,
                        new LocalCellRef(1, 2, 3));
        Assert.assertEquals(expectedRef, r);
    }

    @Test
    public void testFromTo() {
        final RangeRef r = this.builder.fromColumn(5).fromRow(5).toColumn(7).toRow(8).build();
        final LocalCellRef f = new LocalCellRef(5, 5, 0);
        final LocalCellRef t = new LocalCellRef(8, 7, 0);
        final RangeRef expectedRef = new RangeRef(null, f, t);
        Assert.assertEquals(expectedRef, r);
    }

    @Test
    public void testFromAbsTo() {
        final RangeRef r = this.builder.fromAbsColumn(5).fromAbsRow(5).toColumn(7).toRow(8).build();
        final LocalCellRef f = new LocalCellRef(5, 5, 3);
        final LocalCellRef t = new LocalCellRef(8, 7, 0);
        final RangeRef expectedRef = new RangeRef(null, f, t);
        Assert.assertEquals(expectedRef, r);
    }

    @Test
    public void testTo() {
        final RangeRef r = this.builder.toColumn(7).toRow(8).table("at").build();
        final TableRef tableRef = new TableRef(this.tableNameUtil, null, "at", 0);
        final LocalCellRef localCellRef = new LocalCellRef(8, 7, 0);
        final RangeRef expectedRef = new RangeRef(tableRef, A1, localCellRef);
        Assert.assertEquals(expectedRef, r);
    }

    @Test
    public void testTableFile() {
        final Table table = PowerMock.createMock(Table.class);
        final File file = new File("f");

        PowerMock.resetAll();
        EasyMock.expect(table.getName()).andReturn("table");


        PowerMock.replayAll();
        final RangeRef r = this.builder.toColumn(7).toRow(8).table(table).file(file).build();

        PowerMock.verifyAll();
        final TableRef tableRef = new TableRef(this.tableNameUtil, "f", "table", 0);
        final LocalCellRef localCellRef = new LocalCellRef(8, 7, 0);
        final RangeRef expectedRef = new RangeRef(tableRef, A1, localCellRef);
        Assert.assertEquals(expectedRef, r);
    }

    @Test
    public void testAbsTable() {
        final Table table = PowerMock.createMock(Table.class);

        PowerMock.resetAll();
        EasyMock.expect(table.getName()).andReturn("table");


        PowerMock.replayAll();
        final RangeRef r = this.builder.absTable(table).build();

        PowerMock.verifyAll();
        final TableRef tableRef = new TableRef(this.tableNameUtil, null, "table", 4);
        final LocalCellRef localCellRef = new LocalCellRef(0, 0, 0);
        final RangeRef expectedRef = new RangeRef(tableRef, A1, localCellRef);
        Assert.assertEquals(expectedRef, r);
    }
}