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

package com.github.jferard.fastods.attribute;

import org.junit.Assert;
import org.junit.Test;

/**
 * Created by jferard on 09/04/17.
 */
public class SimpleLengthTest {
    private static final double VALUE = 10.0;
    private static final double OTHER_VALUE = 11.0;

    @Test
    public void testHashCode() {
        Assert.assertEquals(SimpleLength.pc(VALUE).hashCode(), SimpleLength.pc(VALUE).hashCode());
    }

    @Test
    public void testIsNull() {
        Assert.assertFalse(SimpleLength.em(0.0).isNotNull());
        Assert.assertTrue(SimpleLength.em(1.0).isNotNull());
        Assert.assertTrue(SimpleLength.em(-1.0).isNotNull());
    }

    @Test
    public void testToString() {
        Assert.assertEquals("11pt", SimpleLength.pt(OTHER_VALUE).toString());
        Assert.assertEquals("10pt", SimpleLength.pt(VALUE).toString());
    }

    @Test
    public void testEquals() {
        final SimpleLength l1 = SimpleLength.pt(10);
        final SimpleLength l2minus = SimpleLength.pt(10 + 0.0000000000000001);
        Assert.assertEquals(l1, l1);
        Assert.assertEquals(l1, l2minus); // 0 <= d <= MD
        Assert.assertEquals(l2minus, l1); // -MD <= d < 0
        Assert.assertEquals(SimpleLength.pt(VALUE), SimpleLength.pt(VALUE));
    }

    @Test
    public void testNotEquals() {
        Assert.assertNotEquals(SimpleLength.pt(VALUE), SimpleLength.cm(VALUE));
        Assert.assertNotEquals(SimpleLength.in(VALUE), SimpleLength.in(OTHER_VALUE));
        Assert.assertNotEquals(SimpleLength.in(OTHER_VALUE), SimpleLength.in(VALUE));
        Assert.assertNotEquals(new Object(), SimpleLength.mm(VALUE));
        Assert.assertNotEquals(SimpleLength.mm(VALUE), new Object());
        final SimpleLength l1 = SimpleLength.pt(10);
        final SimpleLength l3minus = SimpleLength.pt(10 + 0.003);
        final SimpleLength l4 = SimpleLength.cm(10);
        Assert.assertNotEquals(l1, l3minus); // 0 <= MD <= d
        Assert.assertNotEquals(l3minus, l1); // d <= -MD < 0
        Assert.assertNotEquals(l1, l4);
        Assert.assertNotEquals(l4, l1);
    }
}