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

import org.junit.Assert;
import org.junit.Test;

/**
 * Created by jferard on 09/04/17.
 */
public class AbsoluteLengthTest {

    private static final double VALUE = 10.0;
    private static final double OTHER_VALUE = 11.0;

    @Test
    public void testEquals() {
        Assert.assertEquals(AbsoluteLength.pt(VALUE), AbsoluteLength.pt(VALUE));
        Assert.assertNotEquals(AbsoluteLength.pt(VALUE), AbsoluteLength.cm(VALUE));
        Assert.assertNotEquals(AbsoluteLength.in(VALUE), AbsoluteLength.in(OTHER_VALUE));
        Assert.assertNotEquals(AbsoluteLength.in(OTHER_VALUE), AbsoluteLength.in(VALUE));
        Assert.assertNotEquals("10", AbsoluteLength.mm(VALUE));
        Assert.assertNotEquals(AbsoluteLength.mm(VALUE), "10");
    }

    @Test
    public void testHashCode() {
        Assert.assertEquals(AbsoluteLength.pc(VALUE).hashCode(),
                AbsoluteLength.pc(VALUE).hashCode());
    }

    @Test
    public void testIsNull() {
        Assert.assertTrue(AbsoluteLength.cm(0.0).isNull());
        Assert.assertFalse(AbsoluteLength.cm(1.0).isNull());
        Assert.assertFalse(AbsoluteLength.cm(-1.0).isNull());
    }

    @Test
    public void testEqualsCmMm() {
        Assert.assertEquals(AbsoluteLength.cm(1.0), AbsoluteLength.mm(VALUE));
    }

    @Test
    public void testToString() {
        Assert.assertEquals("3.881mm", AbsoluteLength.pt(OTHER_VALUE).toString());
        Assert.assertEquals("3.528mm", AbsoluteLength.pt(VALUE).toString());
    }

}