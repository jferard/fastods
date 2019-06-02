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
package com.github.jferard.fastods.util;

import com.github.jferard.fastods.Table;
import org.easymock.EasyMock;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.powermock.api.easymock.PowerMock;

import java.text.ParseException;

public class PositionUtilTest {
    PositionUtil util;

    @Before
    public void setUp() {
        this.util = new PositionUtil(new EqualityUtil(), new TableNameUtil());
    }

    @Test
    public final void testA5() throws ParseException {
        final Position position = this.util.newPosition("A5");
        Assert.assertEquals(0, position.getColumn());
        Assert.assertEquals(4, position.getRow());
    }

    @Test
    public final void testAB6666() throws ParseException {
        final Position position = this.util.newPosition("AB6666");
        Assert.assertEquals(27, position.getColumn());
        Assert.assertEquals(6665, position.getRow());
    }

    @Test
    public final void testA1() throws ParseException {
        final Position position = this.util.newPosition("A1");
        Assert.assertEquals(0, position.getColumn());
        Assert.assertEquals(0, position.getRow());
    }

    @Test
    public final void testB6() throws ParseException {
        final Position position = this.util.newPosition("B6");
        Assert.assertEquals(1, position.getColumn());
        Assert.assertEquals(5, position.getRow());
    }

    @Test
    public final void testdBd6() throws ParseException {
        final Position position = this.util.newPosition("$B$6");
        Assert.assertEquals(1, position.getColumn());
        Assert.assertEquals(5, position.getRow());
    }

    @Test(expected = ParseException.class)
    public final void testdBd6d() throws ParseException {
        this.util.newPosition("$B$6$");
    }

    @Test(expected = ParseException.class)
    public final void testddBd6d() throws ParseException {
        this.util.newPosition("$$B$6$");
    }

    @Test(expected = ParseException.class)
    public final void testMinusAB6666() throws ParseException {
        this.util.newPosition("-AB6666");
    }

    @Test(expected = ParseException.class)
    public final void testWeirdPos1() throws ParseException {
        this.util.newPosition("_6");
    }

    @Test(expected = ParseException.class)
    public final void testWeirdPos2() throws ParseException {
        this.util.newPosition("@6");
    }

    @Test(expected = ParseException.class)
    public final void testWeirdPos3() throws ParseException {
        this.util.newPosition("A@6");
    }

    @Test(expected = ParseException.class)
    public final void testWeirdPos4() throws ParseException {
        this.util.newPosition("A_6");
    }

    @Test(expected = ParseException.class)
    public final void testWeirdPos5() throws ParseException {
        Position p = this.util.newPosition("AA$.");
        System.out.println(p);
    }

    @Test(expected = ParseException.class)
    public final void testWeirdPos6() throws ParseException {
        this.util.newPosition("AA$A");
    }

    @Test(expected = ParseException.class)
    public final void testWeirdPos7() throws ParseException {
        this.util.newPosition("AA$9.");
    }

    @Test(expected = ParseException.class)
    public final void testWeirdPos8() throws ParseException {
        this.util.newPosition("AA$9A");
    }

    @Test
    public final void testEquals() throws ParseException {
        final Position position1 = this.util.newPosition("A5");
        Assert.assertEquals(position1, position1);
        Assert.assertNotEquals(position1, null);
        final Position position2 = this.util.newPosition(4, 0);
        Assert.assertEquals(position1, position2);
        final Position position3 = this.util.newPosition(5, 0);
        Assert.assertNotEquals(position1, position3);
        final Position position4 = this.util.newPosition(4, 1);
        Assert.assertNotEquals(position1, position4);
        Assert.assertEquals(28632995, position1.hashCode());
    }

    @Test
    public final void testAddress() {
        final Position position1 = this.util.newPosition(0, 0);
        Assert.assertEquals("A1", position1.toString());
        final Position position2 = this.util.newPosition(1, 2);
        Assert.assertEquals("C2", position2.toString());
        final Position position3 = this.util.newPosition(0, 25);
        Assert.assertEquals("Z1", position3.toString());
        final Position position4 = this.util.newPosition(0, 26);
        Assert.assertEquals("AA1", position4.toString());
        final Position position5 = this.util.newPosition(0, 52);
        Assert.assertEquals("BA1", position5.toString());
        final Position position6 = this.util.newPosition(0, 1023);
        Assert.assertEquals("AMJ1", position6.toString());
    }

    @Test
    public final void testRangeAddress() {
        Assert.assertEquals("A1:K11", this.util.toRangeAddress(0, 0, 10, 10));
    }

    @Test
    public final void testCellAndRangeAddressInTable() {
        final Table t = PowerMock.createMock(Table.class);

        PowerMock.resetAll();
        EasyMock.expect(t.getName()).andReturn("n").times(3);

        PowerMock.replayAll();
        final String cellAddress = this.util.toCellAddress(t, 0, 1023);
        final String rangeAddress = this.util.toRangeAddress(t, 0, 0, 10, 10);

        PowerMock.verifyAll();
        Assert.assertEquals("n.AMJ1", cellAddress);
        Assert.assertEquals("n.A1:n.K11", rangeAddress);
    }

    @Test
    public void testCheckTableName() {
        this.util.checkTableName("no problem");
    }

    @Test
    public void testGetPosition() {
        final Table t = PowerMock.createMock(Table.class);

        PowerMock.resetAll();
        EasyMock.expect(t.getName()).andReturn("no problem");

        PowerMock.replayAll();
        final String cellAddress =
                this.util.newPosition(t, 0, 0).toString();

        PowerMock.verifyAll();
        Assert.assertEquals("'no problem'.A1", cellAddress);
    }
}
