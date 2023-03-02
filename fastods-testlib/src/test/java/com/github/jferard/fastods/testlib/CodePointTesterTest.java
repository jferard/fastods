/*
 * FastODS - A very fast and lightweight (no dependency) library for creating ODS
 *    (Open Document Spreadsheet, mainly for Calc) files in Java.
 *    It's a Martin Schulz's SimpleODS fork
 *    Copyright (C) 2016-2023 J. Férard <https://github.com/jferard>
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
import org.junit.Test;

public class CodePointTesterTest {
    @Test
    public void testRandom() {
        Assert.assertEquals(
                "#xc5 | #x18a | #x24f | #x314 | #x3d9 | #x49e | #x563 | #x628 | #x6ed | #x7b2",
                CodePointTester.codePointsAsString(new CodePointTester.CategoryFilter() {
                    @Override
                    public boolean check(final int codePoint) {
                        return codePoint > 10 && codePoint < 1999 && codePoint % 197 == 0;
                    }
                }));
    }

    @Test
    public void testAscii() {
        Assert.assertEquals("[#x0-~]",
                CodePointTester.codePointsAsString(new CodePointTester.CategoryFilter() {
                    @Override
                    public boolean check(final int codePoint) {
                        return codePoint < 127;
                    }
                }));
    }

    @Test
    public void testTwoBlocks() {
        Assert.assertEquals("[#x0-~] | [#x101-#x1ff]",
                CodePointTester.codePointsAsString(new CodePointTester.CategoryFilter() {
                    @Override
                    public boolean check(final int codePoint) {
                        return (codePoint < 127) || (256 < codePoint) && (codePoint < 512);
                    }
                }));
    }

    @Test
    public void testNone() {
        Assert.assertEquals("",
                CodePointTester.codePointsAsString(new CodePointTester.CategoryFilter() {
                    @Override
                    public boolean check(final int codePoint) {
                        return false;
                    }
                }));
    }
}