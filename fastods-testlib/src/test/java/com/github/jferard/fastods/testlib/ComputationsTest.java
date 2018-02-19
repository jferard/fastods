/*
 * FastODS - A very fast and lightweight (no dependency) library for creating ODS
 *    (Open Document Spreadsheet, mainly for Calc) files in Java.
 *    It's a Martin Schulz's SimpleODS fork
 *    Copyright (C) 2016-2018 J. Férard <https://github.com/jferard>
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

package com.github.jferard.fastods.testlib;

import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;

public class ComputationsTest {
    @Test
    public void test() {
        final Computations c = new Computations("comp", Arrays.asList(1L, 2L, 3L));
        Assert.assertEquals(2L, c.getAvgTime());
        Assert.assertEquals(1L, c.getBestTime());
        Assert.assertEquals(3L, c.getWorstTime());
        Assert.assertEquals("Computations[name = comp, avg = 2, best = 1, worst = 3]", c.toString());
    }

    @Test
    public void testEmpty() {
        final Computations c = new Computations("comp", Collections.<Long>emptyList());
        Assert.assertEquals(-1L, c.getAvgTime());
        Assert.assertEquals(-1L, c.getBestTime());
        Assert.assertEquals(-1L, c.getWorstTime());
        Assert.assertEquals("Computations[name = comp, avg = -1, best = -1, worst = -1]", c.toString());
    }
}