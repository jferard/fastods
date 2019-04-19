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
    @Test
    public void testEquals() {
        Assert.assertEquals(AbsoluteLength.pt(10.0), AbsoluteLength.pt(10.0));
//        Assert.assertNotEquals(AbsoluteLength.pt(10.0), AbsoluteLength.cm(10.0));
        Assert.assertNotEquals(AbsoluteLength.in(10.0), AbsoluteLength.in(11.0));
        Assert.assertNotEquals(AbsoluteLength.in(11.0), AbsoluteLength.in(10.0));
        Assert.assertFalse(AbsoluteLength.mm(10.0).equals("10"));
    }

    @Test
    public void testHashCode() {
        Assert.assertEquals(AbsoluteLength.pc(10.0).hashCode(), AbsoluteLength.pc(10.0).hashCode());
    }

    @Test
    public void testIsNull() {
        Assert.assertTrue(AbsoluteLength.cm(0.0).isNull());
        Assert.assertFalse(AbsoluteLength.cm(1.0).isNull());
        Assert.assertFalse(AbsoluteLength.cm(-1.0).isNull());
    }
    
    @Test
    public void testEqualsCmMm() {
        Assert.assertEquals(AbsoluteLength.cm(1.0), AbsoluteLength.mm(10.0));
    }

    @Test
    public void testToString() {
        Assert.assertEquals("3.881mm", AbsoluteLength.pt(11.0).toString());
        Assert.assertEquals("3.528mm", AbsoluteLength.pt(10.0).toString());
    }

}