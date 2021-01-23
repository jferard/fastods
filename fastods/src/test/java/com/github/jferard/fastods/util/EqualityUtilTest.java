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
package com.github.jferard.fastods.util;

import org.junit.Assert;
import org.junit.Test;
import org.powermock.api.easymock.PowerMock;

public class EqualityUtilTest {
    @Test
    public final void testEquals() {
        Assert.assertTrue(EqualityUtil.equal(null, null));
        final Object s = "object";
        Assert.assertFalse(EqualityUtil.equal(null, s));
        Assert.assertFalse(EqualityUtil.equal(s, null));
        Assert.assertTrue(EqualityUtil.equal(s, s));
    }

    @Test
    public final void testDifferent() {
        PowerMock.resetAll();
        PowerMock.replayAll();
        Assert.assertFalse(EqualityUtil.different(null, null));
        final Object s = "object";
        Assert.assertTrue(EqualityUtil.different(null, s));
        Assert.assertTrue(EqualityUtil.different(s, null));
        Assert.assertFalse(EqualityUtil.different(s, s));
    }

    @Test
    public final void testHashObjects() {
        final Object[] integers = {null, 1, null, 2};
        Assert.assertEquals(924484, EqualityUtil.hashObjects(integers));
    }

    @Test
    public final void testHashInts() {
        Assert.assertEquals(994, EqualityUtil.hashInts(1, 2));
    }
}
