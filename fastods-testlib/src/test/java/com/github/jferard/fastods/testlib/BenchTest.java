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

package com.github.jferard.fastods.testlib;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.logging.Logger;

public class BenchTest {
    private Bench bench;

    @Before
    public void setUp() {
        this.bench = new Bench(Logger.getLogger("l"), "test", 10, 20) {
            int i = 0;

            @Override
            public long test() {
                return this.i++;
            }
        };
    }

    @Test
    public void testValues() {
        Assert.assertEquals(20, this.bench.getColCount());
        Assert.assertEquals(10, this.bench.getRowCount());
        final int r = this.bench.getRandom().nextInt(10);
        Assert.assertTrue(0 <= r && r < 10);
    }

    @Test
    public void testEmptyWithoutWarmup() {
        Assert.assertEquals(-1, this.bench.getWithoutWarmUp().getAvgTime());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testEmptyWithWarmup() {
        this.bench.getWithWarmUp();
    }

    @Test
    public void testTenWithoutWarmup() throws IOException {
        for (int i = 0; i < 10; i++) {
            this.bench.iteration();
        }
        final Computations withoutWarmup = this.bench.getWithoutWarmUp();
        Assert.assertEquals(9l, withoutWarmup.getWorstTime());
        Assert.assertEquals(0l, withoutWarmup.getBestTime());
        Assert.assertEquals(45 / 10, withoutWarmup.getAvgTime());
    }

    @Test
    public void testTenWithWarmup() throws IOException {
        for (int i = 0; i < 10; i++) {
            this.bench.iteration();
        }
        final Computations withoutWarmup = this.bench.getWithWarmUp();
        Assert.assertEquals(9l, withoutWarmup.getWorstTime());
        Assert.assertEquals(2l, withoutWarmup.getBestTime());
        Assert.assertEquals(44 / 8, withoutWarmup.getAvgTime());
    }
}