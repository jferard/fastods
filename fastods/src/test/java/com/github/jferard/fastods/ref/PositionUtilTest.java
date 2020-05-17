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

import com.github.jferard.fastods.Table;
import org.easymock.EasyMock;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.powermock.api.easymock.PowerMock;

import java.io.IOException;
import java.text.ParseException;

public class PositionUtilTest {
    PositionUtil util;

    @Before
    public void setUp() {
        this.util = new PositionUtil(new TableNameUtil());
    }

    @Test
    public final void testA5() throws ParseException, IOException {
        final CellRef CellRef = this.util.newCellRef("A5");
        Assert.assertEquals(0, CellRef.getColumn());
        Assert.assertEquals(4, CellRef.getRow());
    }

    @Test
    public final void testAB6666() throws ParseException, IOException {
        final CellRef CellRef = this.util.newCellRef("AB6666");
        Assert.assertEquals(27, CellRef.getColumn());
        Assert.assertEquals(6665, CellRef.getRow());
    }

    @Test
    public final void testA1() throws ParseException, IOException {
        final CellRef CellRef = this.util.newCellRef("A1");
        Assert.assertEquals(0, CellRef.getColumn());
        Assert.assertEquals(0, CellRef.getRow());
    }

    @Test
    public final void testB6() throws ParseException, IOException {
        final CellRef CellRef = this.util.newCellRef("B6");
        Assert.assertEquals(1, CellRef.getColumn());
        Assert.assertEquals(5, CellRef.getRow());
    }

    @Test
    public final void testdBd6() throws ParseException, IOException {
        final CellRef CellRef = this.util.newCellRef("$B$6");
        Assert.assertEquals(1, CellRef.getColumn());
        Assert.assertEquals(5, CellRef.getRow());
    }

    @Test(expected = ParseException.class)
    public final void testdBd6d() throws ParseException, IOException {
        this.util.newCellRef("$B$6$");
    }

    @Test(expected = ParseException.class)
    public final void testddBd6d() throws ParseException, IOException {
        this.util.newCellRef("$$B$6$");
    }

    @Test(expected = ParseException.class)
    public final void testMinusAB6666() throws ParseException, IOException {
        this.util.newCellRef("-AB6666");
    }

    @Test(expected = ParseException.class)
    public final void testWeirdPos1() throws ParseException, IOException {
        this.util.newCellRef("_6");
    }

    @Test(expected = ParseException.class)
    public final void testWeirdPos2() throws ParseException, IOException {
        this.util.newCellRef("@6");
    }

    @Test(expected = ParseException.class)
    public final void testWeirdPos3() throws ParseException, IOException {
        this.util.newCellRef("A@6");
    }

    @Test(expected = ParseException.class)
    public final void testWeirdPos4() throws ParseException, IOException {
        this.util.newCellRef("A_6");
    }

    @Test(expected = ParseException.class)
    public final void testWeirdPos5() throws ParseException, IOException {
        this.util.newCellRef("AA$.");
    }

    @Test(expected = ParseException.class)
    public final void testWeirdPos6() throws ParseException, IOException {
        this.util.newCellRef("AA$A");
    }

    @Test(expected = ParseException.class)
    public final void testWeirdPos7() throws ParseException, IOException {
        this.util.newCellRef("AA$9.");
    }

    @Test(expected = ParseException.class)
    public final void testWeirdPos8() throws ParseException, IOException {
        this.util.newCellRef("AA$9A");
    }

    @Test
    public final void testEquals() throws ParseException, IOException {
        final CellRef CellRef1 = this.util.newCellRef("A5");
        Assert.assertEquals(CellRef1, CellRef1);
        Assert.assertNotEquals(CellRef1, null);
        final CellRef CellRef2 = this.util.newCellRef(4, 0);
        Assert.assertEquals(CellRef1, CellRef2);
        final CellRef CellRef3 = this.util.newCellRef(5, 0);
        Assert.assertNotEquals(CellRef1, CellRef3);
        final CellRef CellRef4 = this.util.newCellRef(4, 1);
        Assert.assertNotEquals(CellRef1, CellRef4);
        Assert.assertEquals(34596, CellRef1.hashCode());
    }

    @Test
    public final void testAddress() {
        final CellRef CellRef1 = this.util.newCellRef(0, 0);
        Assert.assertEquals("A1", CellRef1.toString());
        final CellRef CellRef2 = this.util.newCellRef(1, 2);
        Assert.assertEquals("C2", CellRef2.toString());
        final CellRef CellRef3 = this.util.newCellRef(0, 25);
        Assert.assertEquals("Z1", CellRef3.toString());
        final CellRef CellRef4 = this.util.newCellRef(0, 26);
        Assert.assertEquals("AA1", CellRef4.toString());
        final CellRef CellRef5 = this.util.newCellRef(0, 52);
        Assert.assertEquals("BA1", CellRef5.toString());
        final CellRef CellRef6 = this.util.newCellRef(0, 1023);
        Assert.assertEquals("AMJ1", CellRef6.toString());
    }

    @Test
    public final void testRangeAddress() {
        Assert.assertEquals("A1:K11", this.util.toRangeAddress(0, 0, 10, 10));
    }

    @Test
    public final void testCellAndRangeAddressInTable() {
        final Table t = PowerMock.createMock(Table.class);

        PowerMock.resetAll();
        EasyMock.expect(t.getName()).andReturn("n").times(2);

        PowerMock.replayAll();
        final String cellAddress = this.util.toCellAddress(t, 0, 1023);
        final String rangeAddress = this.util.toRangeAddress(t, 0, 0, 10, 10);

        PowerMock.verifyAll();
        Assert.assertEquals("n.AMJ1", cellAddress);
        Assert.assertEquals("n.A1:K11", rangeAddress);
    }

    @Test
    public void testCheckTableName() {
        this.util.checkTableName("no problem");
    }

    @Test
    public void testGetCellRef() {
        final Table t = PowerMock.createMock(Table.class);

        PowerMock.resetAll();
        EasyMock.expect(t.getName()).andReturn("no problem");

        PowerMock.replayAll();
        final String cellAddress = this.util.newCellRef(t, 0, 0).toString();

        PowerMock.verifyAll();
        Assert.assertEquals("'no problem'.A1", cellAddress);
    }
}
